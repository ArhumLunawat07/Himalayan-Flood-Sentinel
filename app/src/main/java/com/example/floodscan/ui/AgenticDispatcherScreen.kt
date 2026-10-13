package com.example.floodscan.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floodscan.data.AlertDispatcherEngine
import com.example.floodscan.data.DispatcherAlert
import com.example.floodscan.data.TrishuliDisasterData
import com.example.ui.theme.*

@Composable
fun AgenticDispatcherScreen(
    modifier: Modifier = Modifier
) {
    val isLoopActive by AlertDispatcherEngine.isLoopActive.collectAsState()
    val cycleCount by AlertDispatcherEngine.cycleCount.collectAsState()
    val logs by AlertDispatcherEngine.dispatcherLogs.collectAsState()
    val alerts by AlertDispatcherEngine.alertsHistory.collectAsState()

    var expandedPayloadAlertId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Agent Status & Control Header
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(if (isLoopActive) SafeGreen else DangerRed, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isLoopActive) "AUTONOMOUS MONITOR: ACTIVE" else "AUTONOMOUS MONITOR: PAUSED",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isLoopActive) SafeGreen else DangerRed,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Agentic Sensor Evaluation Loop",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Switch(
                            checked = isLoopActive,
                            onCheckedChange = { AlertDispatcherEngine.toggleLoop(it) },
                            modifier = Modifier.testTag("switch_agent_loop")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Autonomous background agent evaluates river gauge stages and precipitation rates every cycle. Upon danger threshold breach, it automatically compiles emergency incident payloads and dispatches webhooks and mass SMS alerts to authorities.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { AlertDispatcherEngine.triggerManualScan() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = RadarCyan),
                            border = BorderStroke(1.dp, RadarCyanDim),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_manual_scan")
                        ) {
                            Icon(Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Scan Telemetry Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            color = DarkNavy,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Cycles: $cycleCount",
                                    fontSize = 12.sp,
                                    color = RadarCyan,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Webhook Gateway Configuration
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EMERGENCY WEBHOOK GATEWAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningAmber,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "POST HTTP/1.1",
                            fontSize = 10.sp,
                            color = TextTertiary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = DarkNavy,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "https://api.ndrrma.gov.np/v1/webhook/emergency-alert",
                            fontSize = 11.sp,
                            color = RadarCyan,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Target Authorities: Nepal Army Directorate, DDMC Rasuwa, APF Command, Nuwakot DAO",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Live Agent Terminal Logs
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AGENTIC EXECUTION AUDIT LOG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RadarCyan,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(RadarCyan, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = DarkNavy,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .heightIn(max = 160.dp)
                        ) {
                            logs.takeLast(6).forEach { logLine ->
                                Text(
                                    text = logLine,
                                    fontSize = 10.sp,
                                    color = if (logLine.contains("CRITICAL") || logLine.contains("BREACH")) DangerRed else (if (logLine.contains("ACTION")) WarningAmber else TextSecondary),
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 14.sp,
                                    modifier = Modifier.padding(vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dispatched Alerts History
        item {
            Text(
                text = "AUTOMATED ALERT DISPATCHES (${alerts.size}):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontFamily = FontFamily.Monospace
            )
        }

        items(alerts) { alert ->
            DispatcherAlertCard(
                alert = alert,
                isExpanded = expandedPayloadAlertId == alert.id,
                onToggleExpand = {
                    expandedPayloadAlertId = if (expandedPayloadAlertId == alert.id) null else alert.id
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DispatcherAlertCard(
    alert: DispatcherAlert,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, DangerRed.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("alert_card_${alert.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = DangerRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = alert.id,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Surface(
                    color = DangerRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = alert.timestamp,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Target: ${alert.stationName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Condition: ${alert.triggerCondition}",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Webhook: 200 OK", fontSize = 11.sp, color = SafeGreen, fontFamily = FontFamily.Monospace)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sms, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "SMS Sent: ${alert.smsCountSent}", fontSize = 11.sp, color = WarningAmber, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expand Payload JSON toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Hide JSON Payload" else "View Webhook Payload JSON",
                    fontSize = 11.sp,
                    color = RadarCyan,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = RadarCyan,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = DarkNavy,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = alert.payloadJson,
                        fontSize = 10.sp,
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
