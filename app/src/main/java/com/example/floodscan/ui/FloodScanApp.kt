package com.example.floodscan.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class FloodScanTab(
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    TACTICAL_MAP("GIS Map", Icons.Default.Public, "nav_tactical_map"),
    DATA_FUSION("Data Fusion", Icons.Default.Hub, "nav_data_fusion"),
    ALERT_DISPATCHER("Alert Agent", Icons.Default.NotificationsActive, "nav_alert_dispatcher"),
    CUTOFF_TRIAGE("Cut-Off", Icons.AutoMirrored.Filled.AltRoute, "nav_cutoff_triage"),
    VALLEY_TRACER("Tracer", Icons.Default.WaterDrop, "nav_valley_tracer"),
    COPILOT_CHAT("Copilot", Icons.Default.SmartToy, "nav_copilot_chat"),
    SITREP_REPORT("SitRep", Icons.Default.Description, "nav_sitrep_report")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloodScanApp() {
    var selectedTab by remember { mutableStateOf(FloodScanTab.TACTICAL_MAP) }
    var forwardedCopilotPrompt by remember { mutableStateOf<String?>(null) }

    if (selectedTab != FloodScanTab.TACTICAL_MAP) {
        BackHandler {
            selectedTab = FloodScanTab.TACTICAL_MAP
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(RadarCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SatelliteAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "FloodScan AI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFFFEE2E2),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                                ) {
                                    Text(
                                        text = "EMSR927",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DangerRed,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Bhote Koshi–Trishuli Flood Corridor (Nepal 2026)",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary
                ),
                actions = {
                    Surface(
                        color = Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(DangerRed, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "1,342 DEAD",
                                color = DangerRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloodScanTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Surface(
                            color = if (isSelected) Color(0xFFE0F2FE) else Color.Transparent,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .clickable { selectedTab = tab }
                                .testTag(tab.testTag)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) RadarCyan else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = tab.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) RadarCyan else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                FloodScanTab.TACTICAL_MAP -> TacticalMapScreen(
                    onNavigateToCopilot = { prompt ->
                        forwardedCopilotPrompt = prompt
                        selectedTab = FloodScanTab.COPILOT_CHAT
                    }
                )
                FloodScanTab.DATA_FUSION -> DataFusionScreen(
                    onNavigateToCopilot = { prompt ->
                        forwardedCopilotPrompt = prompt
                        selectedTab = FloodScanTab.COPILOT_CHAT
                    }
                )
                FloodScanTab.ALERT_DISPATCHER -> AgenticDispatcherScreen()
                FloodScanTab.CUTOFF_TRIAGE -> CutoffTriageScreen(
                    onAskCopilot = { prompt ->
                        forwardedCopilotPrompt = prompt
                        selectedTab = FloodScanTab.COPILOT_CHAT
                    }
                )
                FloodScanTab.VALLEY_TRACER -> ValleyTracerScreen(
                    onAskCopilot = { prompt ->
                        forwardedCopilotPrompt = prompt
                        selectedTab = FloodScanTab.COPILOT_CHAT
                    }
                )
                FloodScanTab.COPILOT_CHAT -> CopilotChatScreen(
                    initialPrompt = forwardedCopilotPrompt
                )
                FloodScanTab.SITREP_REPORT -> SitRepReportScreen()
            }
        }
    }
}
