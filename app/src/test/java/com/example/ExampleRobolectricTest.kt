package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.floodscan.data.DamageLevel
import com.example.floodscan.data.DataFusionPipeline
import com.example.floodscan.data.StationAlertLevel
import com.example.floodscan.data.TrishuliDisasterData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FloodScan AI", appName)
    }

    @Test
    fun `verify disaster statistics and cutoff settlement counts`() {
        val stats = TrishuliDisasterData.STATS
        assertEquals(1342, stats.totalDeathsConfirmed)
        assertEquals(4895, stats.missingOrUnaccounted)
        assertEquals(6270, stats.cutOffPopulationTotal)

        val cutoffSettlements = TrishuliDisasterData.SETTLEMENTS.filter { it.isCutOff }
        assertEquals(6, cutoffSettlements.size)

        // Verify Timure is severely isolated with emergency LZ
        val timure = cutoffSettlements.find { it.name == "Timure" }
        assertTrue(timure != null)
        assertEquals(1850, timure?.population)
        assertTrue(timure?.recommendedHeliLZ != null)

        // Verify destroyed bridge counts
        val destroyedBridges = TrishuliDisasterData.BRIDGES.filter { it.damageState == DamageLevel.DESTROYED }
        assertEquals(5, destroyedBridges.size)
    }

    @Test
    fun `verify river gauge stations and danger threshold metrics`() {
        val gauges = TrishuliDisasterData.RIVER_GAUGES
        assertEquals(6, gauges.size)

        // Check Timure Gauge is in CRITICAL_DANGER exceeding 8.5m threshold
        val timureGauge = gauges.find { it.id == "gauge_timure" }
        assertTrue(timureGauge != null)
        assertEquals(StationAlertLevel.CRITICAL_DANGER, timureGauge?.status)
        assertTrue(timureGauge!!.currentStageM > timureGauge.dangerThresholdM)
    }

    @Test
    fun `verify watersheds and historical timeline progression`() {
        val watersheds = TrishuliDisasterData.WATERSHEDS
        assertEquals(4, watersheds.size)

        val timelineFrames = TrishuliDisasterData.TIMELINE_FRAMES
        assertEquals(9, timelineFrames.size)

        // First frame is 04:00 AM baseline
        assertEquals("04:00 AM", timelineFrames.first().timeLabel)
        assertEquals("NORMAL", timelineFrames.first().severityLevel)

        // Peak frame is 09:00 AM Timure inundation
        val peakFrame = timelineFrames.find { it.timeLabel == "09:00 AM" }
        assertTrue(peakFrame != null)
        assertEquals("CRITICAL_PEAK", peakFrame?.severityLevel)
        assertEquals(18.52, peakFrame!!.activeFloodAreaSqKm, 0.01)
    }

    @Test
    fun `verify multimodal data fusion pipeline fallback generation`() {
        val feeds = TrishuliDisasterData.SENSOR_FEEDS
        val fusionResult = DataFusionPipeline.generateFallbackFusionResult(feeds, "Glacial Outburst Breach")

        assertTrue(fusionResult.glofProbabilityPct >= 90)
        assertTrue(fusionResult.keyObservations.isNotEmpty())
        assertTrue(fusionResult.tacticalDirectives.isNotEmpty())
        assertTrue(fusionResult.nepaliSummary.isNotBlank())
    }
}
