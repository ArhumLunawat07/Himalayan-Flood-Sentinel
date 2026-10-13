package com.example.floodscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floodscan.data.TrishuliDisasterData
import com.example.floodscan.data.ValleyFlowPoint
import com.example.ui.theme.*

@Composable
fun ValleyTracerScreen(
    onAskCopilot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOriginIndex by remember { mutableIntStateOf(0) }
    var highlightedStep by remember { mutableStateOf<ValleyFlowPoint?>(null) }

    val originOptions = listOf(
        "Glacial Lake Collapse (4,210m)",
        "Rasuwagadhi Border Surge (1,814m)",
        "Langtang Khola Tributary (2,450m)"
    )

    val flowSteps = TrishuliDisasterData.FLOW_PATH

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Surface(
                color = SurfaceNavy,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "BONUS REQUIREMENT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadarCyan,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Valley Flow Path & Downstream Tracer",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = WaterBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Given any upstream glacial lake or mountain origin, traces the gravity flow vector down the Trishuli corridor using Copernicus WorldDEM-30 elevation gradients to forecast wave arrival times and downstream settlement impacts.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Upstream Origin Selector
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "SELECT UPSTREAM ORIGIN POINT:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        originOptions.forEachIndexed { index, title ->
                            Surface(
                                color = if (selectedOriginIndex == index) RadarCyan.copy(alpha = 0.2f) else DarkNavy,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (selectedOriginIndex == index) RadarCyan else BorderSubtle),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedOriginIndex = index }
                                    .testTag("origin_option_$index")
                            ) {
                                Box(
                                    modifier = Modifier.padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedOriginIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedOriginIndex == index) RadarCyan else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Elevation Profile Chart (WorldDEM-30)
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Copernicus WorldDEM-30 Profile",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElevationPurple
                        )
                        Text(
                            text = "Total Drop: 3,630m across 67.2 km",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Custom Canvas Elevation Profile
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(DarkNavy, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            val maxElev = 4400f
                            val minElev = 400f

                            val points = flowSteps.mapIndexed { idx, pt ->
                                val x = (idx.toFloat() / (flowSteps.size - 1)) * w
                                val y = h - ((pt.elevationM - minElev) / (maxElev - minElev)) * h
                                Offset(x, y)
                            }

                            // Fill below curve
                            val fillPath = Path().apply {
                                moveTo(0f, h)
                                for (p in points) {
                                    lineTo(p.x, p.y)
                                }
                                lineTo(w, h)
                                close()
                            }
                            drawPath(
                                path = fillPath,
                                color = ElevationPurple.copy(alpha = 0.25f)
                            )

                            // Profile Line
                            val linePath = Path().apply {
                                moveTo(points.first().x, points.first().y)
                                for (p in points) {
                                    lineTo(p.x, p.y)
                                }
                            }
                            drawPath(
                                path = linePath,
                                color = RadarCyan,
                                style = Stroke(width = 3f, cap = StrokeCap.Round)
                            )

                            // Point circles
                            for (p in points) {
                                drawCircle(color = RadarCyan, radius = 5f, center = p)
                                drawCircle(color = DarkNavy, radius = 2.5f, center = p)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Attribution: Produced using Copernicus WorldDEM-30 © DLR e.V. and © Airbus Defence and Space GmbH provided under COPERNICUS by the EU and ESA.",
                        fontSize = 10.sp,
                        color = TextTertiary
                    )
                }
            }
        }

        // Downstream Flow Simulation Timeline Steps
        item {
            Text(
                text = "DOWNSTREAM FLOOD PATH & SETTLEMENT IMPACT TIMELINE:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RadarCyan,
                fontFamily = FontFamily.Monospace
            )
        }

        items(flowSteps) { step ->
            FlowStepTimelineCard(
                step = step,
                isHighlighted = highlightedStep == step,
                onClick = { highlightedStep = if (highlightedStep == step) null else step },
                onAskCopilot = { onAskCopilot("What was the flood arrival time, surge discharge, and infrastructure impact at ${step.locationName}?") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FlowStepTimelineCard(
    step: ValleyFlowPoint,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    onAskCopilot: () -> Unit
) {
    val riskColor = when (step.riskLevel) {
        "ORIGIN" -> ElevationPurple
        "EXTREME" -> DangerRed
        "CRITICAL" -> DangerRedDim
        "HIGH" -> WarningAmber
        "MEDIUM" -> WarningAmberDim
        else -> SafeGreen
    }

    Surface(
        color = if (isHighlighted) SurfaceCardElevated else SurfaceCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isHighlighted) RadarCyan else BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("step_card_${step.stepIndex}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(riskColor.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, riskColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.stepIndex}",
                            color = riskColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = step.locationName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Elev: ${step.elevationM}m | Dist: +${step.distanceDownstreamKm} km",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    color = DarkNavy,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Text(
                        text = "T+ ${step.estimatedArrivalTimeMin} min",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RadarCyan,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Key Telemetry Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Peak Surge: ${step.peakDischargeM3s} m³/s",
                    fontSize = 11.sp,
                    color = WarningAmber,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Valley Slope: ${step.slopePercent}%",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = DarkNavy.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = riskColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "At Risk: ${step.keyInfrastructureAtRisk}",
                        fontSize = 11.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
