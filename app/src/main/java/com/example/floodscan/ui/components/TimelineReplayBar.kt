package com.example.floodscan.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.floodscan.data.HistoricalTimelineFrame
import com.example.floodscan.data.TrishuliDisasterData
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun TimelineReplayBar(
    selectedFrameIndex: Int,
    onFrameChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    val frames = TrishuliDisasterData.TIMELINE_FRAMES
    val currentFrame = frames.getOrElse(selectedFrameIndex) { frames.first() }

    // Auto-play timer
    LaunchedEffect(isPlaying, selectedFrameIndex) {
        if (isPlaying) {
            delay(2400)
            val next = (selectedFrameIndex + 1) % frames.size
            onFrameChanged(next)
        }
    }

    val severityColor = when (currentFrame.severityLevel) {
        "CRITICAL_PEAK" -> DangerRed
        "WATCH" -> WarningAmber
        "RECEDING" -> RadarCyan
        else -> SafeGreen
    }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("timeline_replay_bar")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: Time label, event title, severity badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HistoryToggleOff,
                        contentDescription = null,
                        tint = RadarCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TIME-LAPSE REPLAY: ${currentFrame.timeLabel}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RadarCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Surface(
                    color = severityColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, severityColor)
                ) {
                    Text(
                        text = currentFrame.severityLevel.replace('_', ' '),
                        color = severityColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${currentFrame.eventTitle}: ${currentFrame.description}",
                fontSize = 11.sp,
                color = TextPrimary,
                lineHeight = 15.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrub Slider
            Slider(
                value = selectedFrameIndex.toFloat(),
                onValueChange = {
                    isPlaying = false
                    onFrameChanged(it.toInt().coerceIn(0, frames.size - 1))
                },
                valueRange = 0f..(frames.size - 1).toFloat(),
                steps = frames.size - 2,
                colors = SliderDefaults.colors(
                    thumbColor = RadarCyan,
                    activeTrackColor = severityColor,
                    inactiveTrackColor = BorderSubtle
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("timeline_slider")
            )

            // Playback controls & Telemetry badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Step backward
                    IconButton(
                        onClick = {
                            isPlaying = false
                            if (selectedFrameIndex > 0) onFrameChanged(selectedFrameIndex - 1)
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Step Back", tint = TextPrimary)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Play / Pause
                    Surface(
                        color = if (isPlaying) DangerRed else RadarCyan,
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.testTag("btn_toggle_play")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Step forward
                    IconButton(
                        onClick = {
                            isPlaying = false
                            if (selectedFrameIndex < frames.size - 1) onFrameChanged(selectedFrameIndex + 1)
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Step Forward", tint = TextPrimary)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Flood: ${currentFrame.activeFloodAreaSqKm} km²",
                        fontSize = 10.sp,
                        color = RadarCyan,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Cloud: ${currentFrame.cloudCoverPercent}%",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
