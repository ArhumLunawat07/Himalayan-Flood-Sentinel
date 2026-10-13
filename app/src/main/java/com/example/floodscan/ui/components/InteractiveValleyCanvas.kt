package com.example.floodscan.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floodscan.data.*
import com.example.ui.theme.*

enum class MapLayerMode {
    SAR_RADAR,
    OPTICAL_NDWI,
    DEM_ELEVATION,
    OSM_INFRASTRUCTURE
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun InteractiveValleyCanvas(
    layerMode: MapLayerMode,
    isPostEvent: Boolean,
    selectedSettlement: Settlement?,
    selectedBridge: BridgeStatus?,
    onSelectSettlement: (Settlement) -> Unit,
    onSelectBridge: (BridgeStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val textMeasurer = rememberTextMeasurer()

    // Bounding box for Bhote Koshi - Trishuli corridor:
    // Lat: 27.90 to 28.32 (Delta ~0.42)
    // Lon: 85.12 to 85.42 (Delta ~0.30)
    val minLat = 27.90
    val maxLat = 28.32
    val minLon = 85.12
    val maxLon = 85.42

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
            .testTag("interactive_valley_canvas")
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.8f, 3.5f)
                    offset = Offset(
                        x = (offset.x + pan.x).coerceIn(-400f * scale, 400f * scale),
                        y = (offset.y + pan.y).coerceIn(-600f * scale, 600f * scale)
                    )
                }
            }
            .pointerInput(scale, offset) {
                detectTapGestures { tapOffset ->
                    val w = size.width
                    val h = size.height

                    // Check tap on settlements
                    var tappedSettlement: Settlement? = null
                    for (set in TrishuliDisasterData.SETTLEMENTS) {
                        val pt = geoToCanvas(set.lat, set.lon, minLat, maxLat, minLon, maxLon, w.toFloat(), h.toFloat(), scale, offset)
                        val dist = (tapOffset - pt).getDistance()
                        if (dist < 40f * scale) {
                            tappedSettlement = set
                            break
                        }
                    }
                    if (tappedSettlement != null) {
                        onSelectSettlement(tappedSettlement)
                        return@detectTapGestures
                    }

                    // Check tap on bridges
                    var tappedBridge: BridgeStatus? = null
                    for (br in TrishuliDisasterData.BRIDGES) {
                        val pt = geoToCanvas(br.lat, br.lon, minLat, maxLat, minLon, maxLon, w.toFloat(), h.toFloat(), scale, offset)
                        val dist = (tapOffset - pt).getDistance()
                        if (dist < 35f * scale) {
                            tappedBridge = br
                            break
                        }
                    }
                    if (tappedBridge != null) {
                        onSelectBridge(tappedBridge)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Draw Background grid & hillshade contours
            drawTopographyContours(w, h, scale, offset, layerMode)

            // 2. Draw Pre-Event Highway (Pasang Lhamu Hwy H09)
            drawRoadNetwork(minLat, maxLat, minLon, maxLon, w, h, scale, offset, layerMode, isPostEvent)

            // 3. Draw River Corridor & Flood Inundation Polygons
            drawRiverAndFloodPolygons(minLat, maxLat, minLon, maxLon, w, h, scale, offset, layerMode, isPostEvent)

            // 4. Draw Bridges
            for (bridge in TrishuliDisasterData.BRIDGES) {
                val pt = geoToCanvas(bridge.lat, bridge.lon, minLat, maxLat, minLon, maxLon, w, h, scale, offset)
                drawBridgeMarker(bridge, pt, isPostEvent, bridge == selectedBridge, scale)
            }

            // 5. Draw Settlements
            for (set in TrishuliDisasterData.SETTLEMENTS) {
                val pt = geoToCanvas(set.lat, set.lon, minLat, maxLat, minLon, maxLon, w, h, scale, offset)
                drawSettlementPin(set, pt, isPostEvent, set == selectedSettlement, scale, textMeasurer)
            }

            // 6. Draw Compass & Scale Indicator
            drawMapTelemetryDecorations(size, scale)
        }
    }
}

private fun geoToCanvas(
    lat: Double,
    lon: Double,
    minLat: Double,
    maxLat: Double,
    minLon: Double,
    maxLon: Double,
    w: Float,
    h: Float,
    scale: Float,
    offset: Offset
): Offset {
    val padding = 40f
    // Invert lat because higher lat is North (top)
    val normY = ((maxLat - lat) / (maxLat - minLat)).toFloat()
    val normX = ((lon - minLon) / (maxLon - minLon)).toFloat()

    val rawX = padding + normX * (w - 2 * padding)
    val rawY = padding + normY * (h - 2 * padding)

    val centerX = w / 2f
    val centerY = h / 2f

    val scaledX = (rawX - centerX) * scale + centerX + offset.x
    val scaledY = (rawY - centerY) * scale + centerY + offset.y
    return Offset(scaledX, scaledY)
}

private fun DrawScope.drawTopographyContours(
    w: Float,
    h: Float,
    scale: Float,
    offset: Offset,
    mode: MapLayerMode
) {
    val contourColor = when (mode) {
        MapLayerMode.DEM_ELEVATION -> Color(0xFF7C3AED).copy(alpha = 0.15f)
        MapLayerMode.SAR_RADAR -> Color(0xFFCBD5E1).copy(alpha = 0.4f)
        MapLayerMode.OPTICAL_NDWI -> Color(0xFF16A34A).copy(alpha = 0.15f)
        MapLayerMode.OSM_INFRASTRUCTURE -> Color(0xFF94A3B8).copy(alpha = 0.3f)
    }

    // Grid coordinates
    val step = 80f * scale
    var x = (offset.x % step)
    while (x < w) {
        drawLine(
            color = contourColor,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 1f
        )
        x += step
    }

    var y = (offset.y % step)
    while (y < h) {
        drawLine(
            color = contourColor,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1f
        )
        y += step
    }

    // Canyon wall shading lines representing Langtang / Trishuli mountain ranges
    val wallPath = Path().apply {
        moveTo(w * 0.3f + offset.x, 0f)
        cubicTo(w * 0.45f + offset.x, h * 0.3f, w * 0.25f + offset.x, h * 0.6f, w * 0.15f + offset.x, h)
        lineTo(0f, h)
        lineTo(0f, 0f)
        close()
    }
    drawPath(
        path = wallPath,
        color = if (mode == MapLayerMode.DEM_ELEVATION) Color(0xFFF3E8FF).copy(alpha = 0.5f) else Color(0xFFF1F5F9)
    )
}

private fun DrawScope.drawRiverAndFloodPolygons(
    minLat: Double,
    maxLat: Double,
    minLon: Double,
    maxLon: Double,
    w: Float,
    h: Float,
    scale: Float,
    offset: Offset,
    mode: MapLayerMode,
    isPostEvent: Boolean
) {
    // Continuous river coordinates from Tibetan border down to Trishuli
    val riverCoords = listOf(
        Pair(28.3100, 85.3850),
        Pair(28.2833, 85.3789), // Rasuwagadhi
        Pair(28.2612, 85.3811), // Timure
        Pair(28.2350, 85.3710),
        Pair(28.2120, 85.3520), // Lingling
        Pair(28.1850, 85.3420),
        Pair(28.1611, 85.3333), // Syabrubesi confluence
        Pair(28.1200, 85.2950),
        Pair(28.0850, 85.2580),
        Pair(28.0583, 85.2341), // Ramche
        Pair(28.0294, 85.2155), // Mailung
        Pair(27.9950, 85.2000),
        Pair(27.9733, 85.1842), // Betrawati
        Pair(27.9350, 85.1620),
        Pair(27.9167, 85.1500)  // Trishuli Bazar
    )

    val riverPoints = riverCoords.map {
        geoToCanvas(it.first, it.second, minLat, maxLat, minLon, maxLon, w, h, scale, offset)
    }

    if (riverPoints.size < 2) return

    val normalRiverPath = Path().apply {
        moveTo(riverPoints.first().x, riverPoints.first().y)
        for (i in 1 until riverPoints.size) {
            lineTo(riverPoints[i].x, riverPoints[i].y)
        }
    }

    // 1. Draw Normal baseline channel
    drawPath(
        path = normalRiverPath,
        color = WaterBlue.copy(alpha = 0.8f),
        style = Stroke(width = 4f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 2. If Post-Event, draw massive flood inundation & debris deposition buffer
    if (isPostEvent) {
        val floodWidth = when (mode) {
            MapLayerMode.SAR_RADAR -> 28f * scale // Active microwave backscatter drop
            MapLayerMode.OPTICAL_NDWI -> 24f * scale
            else -> 22f * scale
        }

        val floodColor = when (mode) {
            MapLayerMode.SAR_RADAR -> RadarCyan.copy(alpha = 0.45f) // High contrast SAR water mask
            MapLayerMode.OPTICAL_NDWI -> Color(0xFF00E676).copy(alpha = 0.45f) // High NDWI green/cyan
            MapLayerMode.DEM_ELEVATION -> ElevationPurple.copy(alpha = 0.4f)
            MapLayerMode.OSM_INFRASTRUCTURE -> WarningAmber.copy(alpha = 0.4f)
        }

        // Draw Inundation Corridor
        drawPath(
            path = normalRiverPath,
            color = floodColor,
            style = Stroke(width = floodWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Debris flow core (heavy boulders / mud flow)
        drawPath(
            path = normalRiverPath,
            color = WarningAmber.copy(alpha = 0.35f),
            style = Stroke(
                width = 12f * scale,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f * scale, 12f * scale), 0f),
                cap = StrokeCap.Round
            )
        )
    }
}

private fun DrawScope.drawRoadNetwork(
    minLat: Double,
    maxLat: Double,
    minLon: Double,
    maxLon: Double,
    w: Float,
    h: Float,
    scale: Float,
    offset: Offset,
    mode: MapLayerMode,
    isPostEvent: Boolean
) {
    for (segment in TrishuliDisasterData.SEVERED_ROADS) {
        val points = segment.coordinates.map {
            geoToCanvas(it.first, it.second, minLat, maxLat, minLon, maxLon, w, h, scale, offset)
        }
        if (points.size < 2) continue

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
        }

        if (isPostEvent && segment.status == DamageLevel.DESTROYED) {
            // Severed road: red dashed line with hazard indicator
            drawPath(
                path = path,
                color = DangerRed,
                style = Stroke(
                    width = 4f * scale,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f * scale, 10f * scale), 0f),
                    cap = StrokeCap.Round
                )
            )
        } else {
            // Intact pre-event road: clean slate line
            drawPath(
                path = path,
                color = Color(0xFF64748B),
                style = Stroke(width = 3f * scale, cap = StrokeCap.Round)
            )
        }
    }
}

private fun DrawScope.drawBridgeMarker(
    bridge: BridgeStatus,
    pos: Offset,
    isPostEvent: Boolean,
    isSelected: Boolean,
    scale: Float
) {
    val radius = if (isSelected) 10f * scale else 7f * scale

    if (isPostEvent && bridge.damageState == DamageLevel.DESTROYED) {
        // Red Cross for destroyed bridge
        drawCircle(
            color = DangerRed.copy(alpha = 0.3f),
            radius = radius * 1.8f,
            center = pos
        )
        val s = radius * 0.9f
        drawLine(
            color = DangerRed,
            start = Offset(pos.x - s, pos.y - s),
            end = Offset(pos.x + s, pos.y + s),
            strokeWidth = 3.5f * scale,
            cap = StrokeCap.Round
        )
        drawLine(
            color = DangerRed,
            start = Offset(pos.x + s, pos.y - s),
            end = Offset(pos.x - s, pos.y + s),
            strokeWidth = 3.5f * scale,
            cap = StrokeCap.Round
        )
    } else {
        // Operational or pre-event bridge symbol
        drawCircle(
            color = SafeGreen,
            radius = radius,
            center = pos
        )
        drawCircle(
            color = SpaceBlack,
            radius = radius * 0.5f,
            center = pos
        )
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawSettlementPin(
    set: Settlement,
    pos: Offset,
    isPostEvent: Boolean,
    isSelected: Boolean,
    scale: Float,
    textMeasurer: TextMeasurer
) {
    val pinColor = when {
        !isPostEvent -> Color(0xFF64B5F6)
        set.accessStatus == AccessStatus.ISOLATED_CRITICAL -> DangerRed
        set.accessStatus == AccessStatus.HELI_AIRDROP_ONLY -> WarningAmber
        set.accessStatus == AccessStatus.FOOT_TRAIL_ONLY -> WarningAmberDim
        set.accessStatus == AccessStatus.PARTIAL_SINGLE_LANE -> Color(0xFFFFD54F)
        else -> SafeGreen
    }

    val radius = if (isSelected) 12f * scale else 8f * scale

    // Pulse aura for cut off settlements
    if (isPostEvent && set.isCutOff) {
        drawCircle(
            color = pinColor.copy(alpha = 0.25f),
            radius = radius * 2.2f,
            center = pos
        )
    }

    // Pin circle
    drawCircle(
        color = pinColor,
        radius = radius,
        center = pos
    )
    drawCircle(
        color = SpaceBlack,
        radius = radius * 0.45f,
        center = pos
    )

    // Label text
    val labelText = if (set.isCutOff && isPostEvent) "⚠️ ${set.name}" else set.name
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(labelText),
        style = TextStyle(
            color = if (isSelected) RadarCyan else TextPrimary,
            fontSize = (11 * scale).coerceIn(10f, 15f).sp,
            background = DarkNavy.copy(alpha = 0.8f)
        )
    )

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(pos.x + radius + 4f, pos.y - radius)
    )
}

private fun DrawScope.drawMapTelemetryDecorations(size: Size, scale: Float) {
    // Compass Rose in top-right
    val compassCenter = Offset(size.width - 45f, 45f)
    drawCircle(
        color = SurfaceCard.copy(alpha = 0.85f),
        radius = 24f,
        center = compassCenter
    )
    // North Arrow
    val northPath = Path().apply {
        moveTo(compassCenter.x, compassCenter.y - 18f)
        lineTo(compassCenter.x - 7f, compassCenter.y + 10f)
        lineTo(compassCenter.x, compassCenter.y + 4f)
        lineTo(compassCenter.x + 7f, compassCenter.y + 10f)
        close()
    }
    drawPath(path = northPath, color = RadarCyan)

    // Scale bar in bottom-left
    val barStart = Offset(20f, size.height - 25f)
    val barEnd = Offset(120f, size.height - 25f)
    drawLine(color = TextSecondary, start = barStart, end = barEnd, strokeWidth = 3f)
    drawLine(color = TextSecondary, start = Offset(20f, size.height - 30f), end = Offset(20f, size.height - 20f), strokeWidth = 3f)
    drawLine(color = TextSecondary, start = Offset(120f, size.height - 30f), end = Offset(120f, size.height - 20f), strokeWidth = 3f)
}
