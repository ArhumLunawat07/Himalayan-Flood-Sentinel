package com.example.floodscan.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.floodscan.data.DataFusionPipeline
import com.example.floodscan.data.MultimodalFusionResult
import com.example.floodscan.data.SensorTelemetryFeed
import com.example.floodscan.data.TrishuliDisasterData
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DataFusionScreen(
    onNavigateToCopilot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedScenarioIndex by remember { mutableIntStateOf(0) }
    var isRunningFusion by remember { mutableStateOf(false) }
    var fusionResult by remember { mutableStateOf<MultimodalFusionResult?>(null) }
    var enableThinking by remember { mutableStateOf(true) }

    val scenarios = listOf(
        "Glacial Moraine Collapse & Cloudburst Surge (Timure / Rasuwa)",
        "Langtang Snowmelt Surcharge & Saturated Soil Inflow",
        "Mailung Debris Pulse & Hydropower Pier Scour"
    )

    // Initial fallback fusion result
    LaunchedEffect(Unit) {
        if (fusionResult == null) {
            fusionResult = DataFusionPipeline.generateFallbackFusionResult(
                TrishuliDisasterData.SENSOR_FEEDS,
                scenarios[0]
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Pipeline Header Card
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
                                text = "MULTIMODAL AI PIPELINE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadarCyan,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Satellite Imagery + Sensor Matrix Fusion",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Hub,
                            contentDescription = null,
                            tint = RadarCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Simultaneously feeds unstructured Sentinel-1 SAR & optical satellite imagery patches (checking for glacial lake outbursts and moraine failures) alongside structured tabular river sensor metrics (water level, discharge, velocity, soil saturation) into Gemini.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Scenario Selector Chips
        item {
            Column {
                Text(
                    text = "SELECT OPERATIONAL FUSION SCENARIO:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                scenarios.forEachIndexed { idx, name ->
                    Surface(
                        color = if (selectedScenarioIndex == idx) SurfaceCardElevated else SurfaceCard,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, if (selectedScenarioIndex == idx) RadarCyan else BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedScenarioIndex = idx
                                fusionResult = null
                            }
                            .testTag("scenario_chip_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(
                                        if (selectedScenarioIndex == idx) RadarCyan else BorderSubtle,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedScenarioIndex == idx) {
                                    Box(modifier = Modifier.size(8.dp).background(SpaceBlack, CircleShape))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = name,
                                fontSize = 12.sp,
                                color = if (selectedScenarioIndex == idx) RadarCyan else TextPrimary,
                                fontWeight = if (selectedScenarioIndex == idx) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Multimodal Input Split View: [Unstructured Satellite Image] + [Structured Telemetry Matrix]
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "INPUT 1: UNSTRUCTURED SATELLITE IMAGE PATCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningAmber,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.satellite_damage_pair_1791472811441),
                        contentDescription = "Glacial lake and river debris satellite patch",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Sentinel-1 SAR C-band + Optical True-Color patch of Langtang-Bhote Koshi canyon",
                        fontSize = 10.sp,
                        color = TextTertiary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "INPUT 2: STRUCTURED SENSOR TELEMETRY FEEDS (TABULAR)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RadarCyan,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Sensor Table Preview
                    TrishuliDisasterData.SENSOR_FEEDS.take(3).forEach { feed ->
                        SensorTableRow(feed = feed)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        // Action Button to run Multimodal Data Fusion
        item {
            Button(
                onClick = {
                    isRunningFusion = true
                    coroutineScope.launch {
                        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.satellite_damage_pair_1791472811441)
                        fusionResult = DataFusionPipeline.executeFusionAnalysis(
                            satelliteImage = bmp,
                            sensorFeeds = TrishuliDisasterData.SENSOR_FEEDS,
                            scenarioName = scenarios[selectedScenarioIndex],
                            enableThinking = enableThinking
                        )
                        isRunningFusion = false
                    }
                },
                enabled = !isRunningFusion,
                colors = ButtonDefaults.buttonColors(containerColor = RadarCyan, contentColor = SpaceBlack),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_run_data_fusion")
            ) {
                if (isRunningFusion) {
                    CircularProgressIndicator(color = SpaceBlack, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Synthesizing Cross-Modal Telemetry...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                } else {
                    Icon(Icons.Default.Insights, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Execute Multimodal Data Fusion", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Fusion AI Results Section
        fusionResult?.let { result ->
            item {
                Surface(
                    color = SurfaceCardElevated,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, RadarCyan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fusion_result_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CROSS-MODAL FUSION OUTPUT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadarCyan,
                                fontFamily = FontFamily.Monospace
                            )
                            Surface(
                                color = DangerRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, DangerRed)
                            ) {
                                Text(
                                    text = result.surgeSeverityIndex.replace('_', ' '),
                                    color = DangerRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Key Metric Badges Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FusionPill("GLOF PROBABILITY", "${result.glofProbabilityPct}%", DangerRed, Modifier.weight(1f))
                            FusionPill("CREST ARRIVAL", "T+ ${result.estimatedArrivalMin}m", WarningAmber, Modifier.weight(1f))
                            FusionPill("PEAK SURGE", "${result.peakDischargeEstimateM3s} m³/s", RadarCyan, Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Cross-Modal Observations:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        result.keyObservations.forEach { obs ->
                            Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                                Text(text = "✓", color = SafeGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = obs, color = TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Tactical SAR Directives:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningAmber
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        result.tacticalDirectives.forEach { directive ->
                            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                                Text(text = "•", color = WarningAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = directive, color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nepali summary box
                        Surface(
                            color = DarkNavy,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "🇳🇵 नेपाली निष्कर्ष (Bilingual Directive):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RadarCyan
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = result.nepaliSummary,
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SensorTableRow(feed: SensorTelemetryFeed) {
    Surface(
        color = DarkNavy,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (feed.isBreached) DangerRed.copy(alpha = 0.5f) else BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = feed.stationName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "${feed.river} | Precip: ${feed.rainfallRateMmH} mm/h", fontSize = 10.sp, color = TextSecondary)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Stage: ${feed.waterLevelM}m", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (feed.isBreached) DangerRed else SafeGreen)
                    Text(text = "Danger: ${feed.dangerThresholdM}m", fontSize = 9.sp, color = TextTertiary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "${feed.dischargeM3s} m³/s", fontSize = 11.sp, color = WarningAmber)
                    Text(text = "${feed.velocityMs} m/s", fontSize = 9.sp, color = TextTertiary)
                }
            }
        }
    }
}

@Composable
fun FusionPill(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = DarkNavy,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, color = TextSecondary, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}
