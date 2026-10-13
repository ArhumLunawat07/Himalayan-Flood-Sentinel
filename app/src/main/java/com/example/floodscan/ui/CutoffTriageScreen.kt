package com.example.floodscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
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
import com.example.floodscan.data.*
import com.example.ui.theme.*

@Composable
fun CutoffTriageScreen(
    onAskCopilot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterCutoffOnly by remember { mutableStateOf(true) }
    var showRestorationPlanner by remember { mutableStateOf(false) }

    val displayedSettlements = remember(filterCutoffOnly) {
        if (filterCutoffOnly) {
            TrishuliDisasterData.SETTLEMENTS.filter { it.isCutOff }
        } else {
            TrishuliDisasterData.SETTLEMENTS
        }
    }

    val totalCutoffPop = TrishuliDisasterData.STATS.cutOffPopulationTotal
    val totalRations = TrishuliDisasterData.SETTLEMENTS.filter { it.isCutOff }.sumOf { it.dailyRationsNeededKg }
    val totalWaterUnits = TrishuliDisasterData.SETTLEMENTS.filter { it.isCutOff }.sumOf { it.waterPurificationUnitsNeeded }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Triage Header Card
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
                                text = "WHO IS CUT OFF?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadarCyan,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Settlement Isolation & Road Network Triage",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.AltRoute,
                            contentDescription = null,
                            tint = WarningAmber,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Topological analysis across OpenStreetMap road graph identifies 6 settlements severed from vehicular contact to Dhunche & Trishuli Hospitals due to 7 destroyed bridges and 42.8 km of debris washouts.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Key Logistics Metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TriageStatBox("CUT-OFF CITIZENS", "$totalCutoffPop", DangerRed, Modifier.weight(1f))
                        TriageStatBox("DAILY RATIONS", "${totalRations}kg", WarningAmber, Modifier.weight(1f))
                        TriageStatBox("WATER PURIFIERS", "$totalWaterUnits units", RadarCyan, Modifier.weight(1f))
                    }
                }
            }
        }

        // Filter Bar & Action Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = filterCutoffOnly,
                        onClick = { filterCutoffOnly = true },
                        label = { Text("Cut-Off Only (6)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DangerRed.copy(alpha = 0.2f),
                            selectedLabelColor = DangerRed
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filterCutoffOnly,
                            selectedBorderColor = DangerRed,
                            borderColor = BorderSubtle
                        ),
                        modifier = Modifier.testTag("filter_cutoff_only")
                    )
                    FilterChip(
                        selected = !filterCutoffOnly,
                        onClick = { filterCutoffOnly = false },
                        label = { Text("All (10)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SurfaceCardElevated,
                            selectedLabelColor = RadarCyan
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = !filterCutoffOnly,
                            selectedBorderColor = RadarCyan,
                            borderColor = BorderSubtle
                        ),
                        modifier = Modifier.testTag("filter_all_settlements")
                    )
                }

                TextButton(
                    onClick = { showRestorationPlanner = !showRestorationPlanner },
                    colors = ButtonDefaults.textButtonColors(contentColor = RadarCyan)
                ) {
                    Icon(
                        imageVector = if (showRestorationPlanner) Icons.Default.ExpandLess else Icons.Default.Engineering,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showRestorationPlanner) "Hide Planner" else "Restoration Plan", fontSize = 12.sp)
                }
            }
        }

        // Bridge Restoration Priority Planner (if toggled)
        if (showRestorationPlanner) {
            item {
                Surface(
                    color = SurfaceCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, RadarCyanDim),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ROUTE RESTORATION PRIORITY RANKING",
                                color = RadarCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Calculated by: (Reconnected Population ÷ Est. Repair Days) to optimize relief life-support access:",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        PriorityRankRow(
                            rank = "1",
                            name = "Syabrubesi Confluence Bridge",
                            impact = "Reopens north access corridor for 6,030 citizens",
                            estDays = "35 days",
                            tagColor = DangerRed
                        )
                        PriorityRankRow(
                            rank = "2",
                            name = "Timure Dry Port Access Bridge",
                            impact = "Restores direct road link for 1,850 citizens & customs depot",
                            estDays = "28 days",
                            tagColor = WarningAmber
                        )
                        PriorityRankRow(
                            rank = "3",
                            name = "Mailung Khola Bailey Bridge",
                            impact = "Reconnects 980 isolated hydropower residents",
                            estDays = "12 days",
                            tagColor = RadarCyan
                        )
                        PriorityRankRow(
                            rank = "4",
                            name = "Betrawati Cantilever Bridge Reinforcement",
                            impact = "Secures southern lifeline for 2,800 residents & trucks",
                            estDays = "18 days",
                            tagColor = SafeGreen
                        )
                    }
                }
            }
        }

        // List of Settlements with full isolation diagnostics
        items(displayedSettlements) { settlement ->
            SettlementTriageCard(
                settlement = settlement,
                onAskCopilot = { prompt -> onAskCopilot(prompt) }
            )
        }

        // Bottom space for scrolling comfort
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TriageStatBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = DarkNavy,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, color = TextSecondary, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PriorityRankRow(rank: String, name: String, impact: String, estDays: String, tagColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = tagColor.copy(alpha = 0.2f),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, tagColor),
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = rank, color = tagColor, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = impact, color = TextSecondary, fontSize = 11.sp)
        }
        Text(text = estDays, color = tagColor, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun SettlementTriageCard(
    settlement: Settlement,
    onAskCopilot: (String) -> Unit
) {
    val statusColor = when (settlement.accessStatus) {
        AccessStatus.ISOLATED_CRITICAL -> DangerRed
        AccessStatus.HELI_AIRDROP_ONLY -> WarningAmber
        AccessStatus.FOOT_TRAIL_ONLY -> WarningAmberDim
        AccessStatus.PARTIAL_SINGLE_LANE -> Color(0xFFFFD54F)
        AccessStatus.OPERATIONAL -> SafeGreen
    }

    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, if (settlement.isCutOff) statusColor.copy(alpha = 0.6f) else BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_settlement_${settlement.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Name & Population
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = settlement.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = settlement.nepaliName,
                        fontSize = 13.sp,
                        color = RadarCyan
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, statusColor)
                ) {
                    Text(
                        text = settlement.accessStatus.name.replace('_', ' '),
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Altitude: ${settlement.elevationM}m | Population: ${settlement.population} people",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Cut-off Reason Description
            Surface(
                color = DarkNavy.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = if (settlement.isCutOff) Icons.Default.Dangerous else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = settlement.cutoffReason,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hospital Connection Status
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = if (settlement.hospitalRoadConnected) SafeGreen else DangerRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = settlement.nearestHospital,
                    fontSize = 12.sp,
                    color = if (settlement.hospitalRoadConnected) SafeGreen else DangerRed,
                    fontWeight = FontWeight.Medium
                )
            }

            // Helicopter LZ Details
            settlement.recommendedHeliLZ?.let { lz ->
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.FlightTakeoff,
                        contentDescription = null,
                        tint = RadarCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Helicopter LZ: ${lz.name} (${lz.code})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RadarCyan
                        )
                        Text(
                            text = "${lz.lat}° N, ${lz.lon}° E | Aircraft: ${lz.maxHelicopterType}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Urgent Supplies Needed Badges
            if (settlement.urgentNeeds.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "URGENT LIFE-SAVING NEEDS:", fontSize = 10.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    settlement.urgentNeeds.take(3).forEach { need ->
                        Surface(
                            color = SurfaceNavy,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Text(
                                text = "• $need",
                                fontSize = 11.sp,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button to request mission plan from Copilot
            OutlinedButton(
                onClick = {
                    onAskCopilot(
                        "Generate an emergency rescue evacuation and helicopter air-drop plan for ${settlement.name} (${settlement.nepaliName}). " +
                                "Population: ${settlement.population}. Recommended LZ: ${settlement.recommendedHeliLZ?.code ?: "None"}."
                    )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RadarCyan),
                border = BorderStroke(1.dp, RadarCyan.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_plan_settlement_${settlement.id}")
            ) {
                Icon(Icons.Default.AirplanemodeActive, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Air-Drop Flight Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
