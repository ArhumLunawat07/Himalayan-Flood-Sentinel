package com.example.floodscan.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object AlertDispatcherEngine {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var loopJob: Job? = null

    private val _isLoopActive = MutableStateFlow(true)
    val isLoopActive: StateFlow<Boolean> = _isLoopActive.asStateFlow()

    private val _cycleCount = MutableStateFlow(4)
    val cycleCount: StateFlow<Int> = _cycleCount.asStateFlow()

    private val _dispatcherLogs = MutableStateFlow<List<String>>(
        listOf(
            "[SYSTEM INIT] Agentic Automated Dispatcher initialized with NDRRMA Webhook gateway.",
            "[CYCLE #01] Evaluated 6 gauge stations. 0 breaches detected (Monsoon baseline).",
            "[CYCLE #02] Evaluated 6 gauge stations. Station G-01 Lhende warning stage approaching (6.4m).",
            "[CYCLE #03] CRITICAL THRESHOLD BREACH: Station G-02 (Timure) exceeded 8.5m danger limit (Measured: 12.8m).",
            "[ACTION TAKEN] Agent generated JSON Webhook payload and dispatched emergency push alert to Nepal Army Command.",
            "[ACTION TAKEN] Emergency SMS broadcast queued for 1,850 citizens in Timure sector."
        )
    )
    val dispatcherLogs: StateFlow<List<String>> = _dispatcherLogs.asStateFlow()

    private val _alertsHistory = MutableStateFlow<List<DispatcherAlert>>(
        listOf(
            DispatcherAlert(
                id = "ALT-TIMURE-01",
                timestamp = "08:48 AM NPT",
                stationName = "Timure Gorge Telemetry Hub",
                level = "RED_EVACUATION",
                triggerCondition = "River Stage Exceeded Danger Limit (12.8m vs 8.5m)",
                measuredValue = "12.8 m (+4.3m above danger)",
                authoritiesTargeted = listOf(
                    "Nepal Army Disaster Response Directorate",
                    "DDMC Rasuwa (Dhunche)",
                    "Timure Ward Disaster Committee"
                ),
                webhookSuccess = true,
                smsCountSent = 1850,
                payloadJson = createPayloadJson("gauge_timure", "Timure Gorge", 12.8, 8.5, 1850)
            ),
            DispatcherAlert(
                id = "ALT-RASUWA-02",
                timestamp = "08:16 AM NPT",
                stationName = "Rasuwagadhi Border Station",
                level = "RED_EVACUATION",
                triggerCondition = "Stage Exceeded Danger + Extreme Rainfall (68.4 mm/h)",
                measuredValue = "11.4 m (+3.4m above danger)",
                authoritiesTargeted = listOf(
                    "Nepal Police Border Customs Post",
                    "Armed Police Force (APF) Border Security Unit"
                ),
                webhookSuccess = true,
                smsCountSent = 620,
                payloadJson = createPayloadJson("gauge_rasuwagadhi", "Rasuwagadhi Border", 11.4, 8.0, 620)
            )
        )
    )
    val alertsHistory: StateFlow<List<DispatcherAlert>> = _alertsHistory.asStateFlow()

    fun toggleLoop(enable: Boolean) {
        _isLoopActive.value = enable
        if (enable) {
            startAutonomousLoop()
        } else {
            loopJob?.cancel()
            appendLog("[AGENT PAUSED] Automated hourly threshold evaluation suspended by operator.")
        }
    }

    fun startAutonomousLoop() {
        loopJob?.cancel()
        loopJob = scope.launch {
            appendLog("[AGENT ACTIVE] Autonomous monitoring loop running. Polling river telemetry & precipitation...")
            while (_isLoopActive.value) {
                delay(6000) // Simulated hourly evaluation cycle in 6 seconds
                evaluateSensorThresholds()
            }
        }
    }

    fun triggerManualScan() {
        scope.launch {
            appendLog("[MANUAL SCAN] Operator triggered instant sensor threshold evaluation scan...")
            evaluateSensorThresholds()
        }
    }

    private fun evaluateSensorThresholds() {
        _cycleCount.value += 1
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val breachedGauges = TrishuliDisasterData.RIVER_GAUGES.filter { it.currentStageM >= it.dangerThresholdM }

        appendLog("[$timeStr CYCLE #${_cycleCount.value}] Scanned 6 river gauge telemetry stations across Bhote Koshi - Trishuli.")

        if (breachedGauges.isNotEmpty()) {
            val primaryBreach = breachedGauges.first()
            val alertId = "ALT-${primaryBreach.id.takeLast(4).uppercase()}-${System.currentTimeMillis() % 1000}"
            val payload = createPayloadJson(primaryBreach.id, primaryBreach.name, primaryBreach.currentStageM, primaryBreach.dangerThresholdM, 1850)

            val alert = DispatcherAlert(
                id = alertId,
                timestamp = "$timeStr NPT",
                stationName = primaryBreach.name,
                level = "RED_EVACUATION",
                triggerCondition = "River Stage ${primaryBreach.currentStageM}m > Danger ${primaryBreach.dangerThresholdM}m",
                measuredValue = "${primaryBreach.currentStageM} m (Discharge: ${primaryBreach.currentDischargeM3s} m³/s)",
                authoritiesTargeted = TrishuliDisasterData.DISPATCH_AUTHORITIES.take(3),
                webhookSuccess = true,
                smsCountSent = 1200 + (Math.random() * 800).toInt(),
                payloadJson = payload
            )

            val updatedAlerts = ArrayList(_alertsHistory.value)
            updatedAlerts.add(0, alert)
            _alertsHistory.value = updatedAlerts.take(15)

            appendLog("[$timeStr CRITICAL ALERT] ${primaryBreach.name} breached danger threshold (${primaryBreach.currentStageM}m >= ${primaryBreach.dangerThresholdM}m).")
            appendLog("[$timeStr WEBHOOK DISPATCH] POST https://api.ndrrma.gov.np/v1/webhook/emergency-alert -> 200 OK (Latency: 142ms).")
            appendLog("[$timeStr CELL BROADCAST] Dispatched emergency evacuation alert to ${alert.smsCountSent} subscribers in zone.")
        } else {
            appendLog("[$timeStr STABLE] All river gauge metrics within normal thresholds.")
        }
    }

    private fun appendLog(log: String) {
        val list = ArrayList(_dispatcherLogs.value)
        list.add(log)
        _dispatcherLogs.value = list.takeLast(25)
    }

    private fun createPayloadJson(
        stationId: String,
        name: String,
        stage: Double,
        danger: Double,
        pop: Int
    ): String {
        return JSONObject().apply {
            put("event_type", "RIVER_SURGE_THRESHOLD_BREACH")
            put("alert_level", "RED_EVACUATION_MANDATORY")
            put("station_id", stationId)
            put("station_name", name)
            put("measured_stage_meters", stage)
            put("danger_threshold_meters", danger)
            put("delta_above_danger_meters", (stage - danger).coerceAtLeast(0.0))
            put("affected_population_estimate", pop)
            put("recommended_action", "IMMEDIATE EVACUATION TO HIGHER RIDGE HELIPAD")
            put("timestamp_utc", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date()))
            put("dispatched_via", "FloodScan AI Autonomous Agent")
        }.toString(2)
    }
}
