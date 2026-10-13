package com.example.floodscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.floodscan.data.*
import com.example.floodscan.ui.components.InteractiveValleyCanvas
import com.example.floodscan.ui.components.LeafletGisMapView
import com.example.floodscan.ui.components.MapLayerMode
import com.example.floodscan.ui.components.TimelineReplayBar
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TacticalMapScreen(
    onNavigateToCopilot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var useLeafletMap by remember { mutableStateOf(true) }
    var layerMode by remember { mutableStateOf(MapLayerMode.SAR_RADAR) }
    var isPostEvent by remember { mutableStateOf(true) }
    var timelineFrameIndex by remember { mutableIntStateOf(4) } // Default 09:00 peak frame

    var showWatersheds by remember { mutableStateOf(true) }
    var showRiverGauges by remember { mutableStateOf(true) }
    var showRainfallHeatmap by remember { mutableStateOf(true) }
    var showCloudCover by remember { mutableStateOf(true) }

    var selectedSettlement by remember { mutableStateOf<Settlement?>(null) }
    var selectedBridge by remember { mutableStateOf<BridgeStatus?>(null) }
    var selectedGauge by remember { mutableStateOf<RiverGaugeStation?>(null) }
    var selectedWatershed by remember { mutableStateOf<WatershedRegion?>(null) }
    var showSatelliteComparisonModal by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Spatial Map View: Leaflet.js GIS or Vector Valley Canvas
        if (useLeafletMap) {
            LeafletGisMapView(
                timelineFrameIndex = timelineFrameIndex,
                showWatersheds = showWatersheds,
                showRiverGauges = showRiverGauges,
                showRainfallHeatmap = showRainfallHeatmap,
                showCloudCover = showCloudCover,
                selectedGaugeId = selectedGauge?.id,
                onGaugeSelected = {
                    selectedGauge = it
                    selectedSettlement = null
                    selectedBridge = null
                    selectedWatershed = null
                },
                onWatershedSelected = {
                    selectedWatershed = it
                    selectedGauge = null
                    selectedSettlement = null
                    selectedBridge = null
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            InteractiveValleyCanvas(
                layerMode = layerMode,
                isPostEvent = isPostEvent,
                selectedSettlement = selectedSettlement,
                selectedBridge = selectedBridge,
                onSelectSettlement = {
                    selectedSettlement = it
                    selectedBridge = null
                    selectedGauge = null
                    selectedWatershed = null
                },
                onSelectBridge = {
                    selectedBridge = it
                    selectedSettlement = null
                    selectedGauge = null
                    selectedWatershed = null
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Top Overlay: Telemetry Header & Layer Controls
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Live Mission Status Card
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(RadarCyan, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BHOTE KOSHI–TRISHULI GIS",
                                color = RadarCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // GIS Mode Toggle (Leaflet vs Vector Canvas)
                        Surface(
                            color = SurfaceCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, RadarCyanDim),
                            modifier = Modifier
                                .clickable { useLeafletMap = !useLeafletMap }
                                .testTag("toggle_gis_engine")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (useLeafletMap) Icons.Default.Public else Icons.Default.Layers,
                                    contentDescription = null,
                                    tint = RadarCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (useLeafletMap) "LEAFLET GIS" else "CANVAS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RadarCyan,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Key Telemetry Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TelemetryBadge("FLOOD EXTENT", "18.5 km²", RadarCyan)
                        TelemetryBadge("BRIDGES LOST", "7 / 11", DangerRed)
                        TelemetryBadge("RIVER GAUGES", "6 Active", WarningAmber)
                        TelemetryBadge("SAR Δσ⁰", "-5.8 dB", RadarCyanDim)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // GIS Layer Filter Toggles
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    LayerToggleChip(
                        label = "Watersheds",
                        isSelected = showWatersheds,
                        color = ElevationPurple,
                        onClick = { showWatersheds = !showWatersheds },
                        testTag = "toggle_watersheds"
                    )
                }
                item {
                    LayerToggleChip(
                        label = "River Gauges",
                        isSelected = showRiverGauges,
                        color = DangerRed,
                        onClick = { showRiverGauges = !showRiverGauges },
                        testTag = "toggle_gauges"
                    )
                }
                item {
                    LayerToggleChip(
                        label = "Rainfall Heatmap",
                        isSelected = showRainfallHeatmap,
                        color = WarningAmber,
                        onClick = { showRainfallHeatmap = !showRainfallHeatmap },
                        testTag = "toggle_rainfall"
                    )
                }
                item {
                    LayerToggleChip(
                        label = "Cloud Cover",
                        isSelected = showCloudCover,
                        color = Color(0xFF64748B),
                        onClick = { showCloudCover = !showCloudCover },
                        testTag = "toggle_cloud_cover"
                    )
                }
            }
        }

        // Floating Action Button for Satellite Proof Inspector
        FloatingActionButton(
            onClick = { showSatelliteComparisonModal = true },
            containerColor = Color.White,
            contentColor = RadarCyan,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 175.dp)
                .testTag("btn_satellite_evidence")
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SatelliteAlt, contentDescription = "Satellite Imagery Evidence")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Satellite Proof", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Bottom Controls: Historical Timeline Replay Slider
        TimelineReplayBar(
            selectedFrameIndex = timelineFrameIndex,
            onFrameChanged = { timelineFrameIndex = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )

        // Bottom Inspection Sheet for clicked River Gauge Station
        selectedGauge?.let { gauge ->
            Surface(
                color = DarkNavy.copy(alpha = 0.96f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                shadowElevation = 18.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .testTag("gauge_detail_sheet")
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = gauge.name,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "River: ${gauge.riverName} | Elev: ${gauge.elevationM}m",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        IconButton(onClick = { selectedGauge = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val isDanger = gauge.currentStageM >= gauge.dangerThresholdM
                    val statusColor = if (isDanger) DangerRed else (if (gauge.currentStageM >= gauge.warningThresholdM) WarningAmber else SafeGreen)

                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, statusColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "STAGE: ${gauge.currentStageM} METERS",
                                    color = statusColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Warning: ${gauge.warningThresholdM}m | Danger: ${gauge.dangerThresholdM}m",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = gauge.trend,
                                color = statusColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Discharge: ${gauge.currentDischargeM3s} m³/s", color = WarningAmber, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        Text(text = "Precip (1h): ${gauge.rainfallLastHourMm} mm/h", color = RadarCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onNavigateToCopilot("Analyze live telemetry for ${gauge.name}. Current stage is ${gauge.currentStageM}m (Danger: ${gauge.dangerThresholdM}m). What downstream settlements should evacuate immediately?") },
                        colors = ButtonDefaults.buttonColors(containerColor = RadarCyan, contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ask Copilot About Gauge Alert", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Bottom Inspection Sheet for clicked Watershed Region
        selectedWatershed?.let { ws ->
            Surface(
                color = DarkNavy.copy(alpha = 0.96f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                shadowElevation = 18.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
                    .testTag("watershed_detail_sheet")
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = ws.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "नेपाली: ${ws.nepaliName} | Catchment: ${ws.areaSqKm} km²",
                                fontSize = 12.sp,
                                color = RadarCyan
                            )
                        }
                        IconButton(onClick = { selectedWatershed = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Mean Elevation: ${ws.meanElevationM}m ASL",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Dominant Hazard Profile: ${ws.dominantRisk}",
                        fontSize = 12.sp,
                        color = WarningAmber,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onNavigateToCopilot("Provide a watershed hazard assessment for ${ws.name} (${ws.areaSqKm} km²). What are the downstream risks?") },
                        colors = ButtonDefaults.buttonColors(containerColor = RadarCyan, contentColor = SpaceBlack),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Terrain, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Watershed Hazard Copilot Assessment", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Satellite Comparison Evidence Dialog
        if (showSatelliteComparisonModal) {
            AlertDialog(
                onDismissRequest = { showSatelliteComparisonModal = false },
                confirmButton = {
                    TextButton(onClick = { showSatelliteComparisonModal = false }) {
                        Text("Close Inspector", color = RadarCyan)
                    }
                },
                title = {
                    Text("SAR & Optical Satellite Change Detection", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Sentinel-1 SAR Radar (Track 12-Day) vs High-Res Optical",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Image(
                            painter = painterResource(id = R.drawable.satellite_damage_pair_1791472811441),
                            contentDescription = "Satellite SAR Radar vs Optical Damage Analysis",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Scientific Observations:\n" +
                                    "• Blue/Yellow zones on left show active microwave backscatter drop (water/debris inundation).\n" +
                                    "• Red zones show landslide scour and bridge structural shear.\n" +
                                    "• Right panel verifies washed-out bridge abutments and 3.5m mud debris sediment.",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Attribution: Contains modified Copernicus Sentinel data 2026. Processed with Kuro Siwo pipeline.",
                            color = TextTertiary,
                            fontSize = 10.sp
                        )
                    }
                },
                containerColor = DarkNavy,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun TelemetryBadge(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextSecondary, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
        Text(text = value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun LayerToggleChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        color = if (isSelected) color.copy(alpha = 0.2f) else SurfaceNavy.copy(alpha = 0.85f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (isSelected) color else BorderSubtle),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(if (isSelected) color else TextTertiary, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) color else TextSecondary
            )
        }
    }
}
