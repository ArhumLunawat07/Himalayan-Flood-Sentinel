package com.example.floodscan.data

enum class AccessStatus {
    ISOLATED_CRITICAL,
    HELI_AIRDROP_ONLY,
    FOOT_TRAIL_ONLY,
    PARTIAL_SINGLE_LANE,
    OPERATIONAL
}

enum class DamageLevel {
    DESTROYED,
    MAJOR_DAMAGE,
    INUNDATED,
    INTACT
}

enum class StationAlertLevel {
    NORMAL,
    WATCH_WARNING,
    CRITICAL_DANGER
}

data class Settlement(
    val id: String,
    val name: String,
    val nepaliName: String,
    val lat: Double,
    val lon: Double,
    val elevationM: Int,
    val population: Int,
    val isCutOff: Boolean,
    val cutoffReason: String,
    val accessStatus: AccessStatus,
    val urgentNeeds: List<String>,
    val nearestHospital: String,
    val hospitalRoadConnected: Boolean,
    val recommendedHeliLZ: HeliLZ?,
    val dailyRationsNeededKg: Int,
    val waterPurificationUnitsNeeded: Int
)

data class HeliLZ(
    val code: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val elevationM: Int,
    val maxHelicopterType: String,
    val terrainRemarks: String
)

data class BridgeStatus(
    val id: String,
    val name: String,
    val river: String,
    val lat: Double,
    val lon: Double,
    val preEventOsmId: String,
    val structureType: String,
    val damageState: DamageLevel,
    val isolatedSettlementIds: List<String>,
    val estimatedRepairDays: Int,
    val remarks: String
)

data class RoadSegment(
    val id: String,
    val name: String,
    val section: String,
    val status: DamageLevel,
    val lengthCutKm: Double,
    val debrisThicknessMeters: Double,
    val coordinates: List<Pair<Double, Double>>
)

data class ValleyFlowPoint(
    val stepIndex: Int,
    val locationName: String,
    val lat: Double,
    val lon: Double,
    val elevationM: Int,
    val distanceDownstreamKm: Double,
    val estimatedArrivalTimeMin: Int,
    val peakDischargeM3s: Int,
    val slopePercent: Double,
    val riskLevel: String,
    val keyInfrastructureAtRisk: String
)

data class EMSR927Metric(
    val metric: String,
    val floodScanAi: String,
    val emsr927Reference: String,
    val agreementPercent: String,
    val remarks: String
)

data class DamageSummaryStats(
    val totalDeathsConfirmed: Int = 1342,
    val missingOrUnaccounted: Int = 4895,
    val totalInundatedAreaSqKm: Double = 18.52,
    val debrisDepositedMillionM3: Double = 3.4,
    val bridgesDestroyedCount: Int = 7,
    val bridgesDamagedCount: Int = 4,
    val severedRoadLengthKm: Double = 42.8,
    val isolatedSettlementsCount: Int = 6,
    val cutOffPopulationTotal: Int = 6270,
    val buildingsSubmergedOrSmashed: Int = 384,
    val sarBackscatterMeanDeltaDb: Double = -5.8,
    val timeToGenerateAnalysisMinutes: Double = 4.2
)

data class CopilotChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val nepaliText: String? = null,
    val timestamp: String,
    val groundedFacts: List<String> = emptyList(),
    val isThinking: Boolean = false
)

// --- Expanded GIS & Telemetry Models ---

data class RiverGaugeStation(
    val id: String,
    val name: String,
    val riverName: String,
    val lat: Double,
    val lon: Double,
    val elevationM: Int,
    val currentStageM: Double,
    val warningThresholdM: Double,
    val dangerThresholdM: Double,
    val currentDischargeM3s: Int,
    val rainfallLastHourMm: Double,
    val status: StationAlertLevel,
    val trend: String,
    val lastUpdated: String
)

data class WatershedRegion(
    val id: String,
    val name: String,
    val nepaliName: String,
    val areaSqKm: Double,
    val meanElevationM: Int,
    val dominantRisk: String,
    val riskColorHex: String,
    val polygonPoints: List<Pair<Double, Double>>
)

data class HistoricalTimelineFrame(
    val index: Int,
    val timeLabel: String,
    val dateTimeStr: String,
    val eventTitle: String,
    val description: String,
    val severityLevel: String,
    val rainfallIntensityFactor: Float,
    val cloudCoverPercent: Int,
    val activeFloodAreaSqKm: Double,
    val destroyedBridgeIds: List<String>,
    val stationStages: Map<String, Double>
)

data class SensorTelemetryFeed(
    val stationId: String,
    val stationName: String,
    val river: String,
    val waterLevelM: Double,
    val warningThresholdM: Double,
    val dangerThresholdM: Double,
    val dischargeM3s: Int,
    val velocityMs: Double,
    val rainfallRateMmH: Double,
    val soilSaturationPct: Int,
    val turbidityNtu: Int,
    val isBreached: Boolean
)

data class MultimodalFusionResult(
    val glofProbabilityPct: Int,
    val surgeSeverityIndex: String,
    val moraineCondition: String,
    val estimatedArrivalMin: Int,
    val peakDischargeEstimateM3s: Int,
    val keyObservations: List<String>,
    val tacticalDirectives: List<String>,
    val nepaliSummary: String,
    val confidenceScore: Double
)

data class DispatcherAlert(
    val id: String,
    val timestamp: String,
    val stationName: String,
    val level: String,
    val triggerCondition: String,
    val measuredValue: String,
    val authoritiesTargeted: List<String>,
    val webhookSuccess: Boolean,
    val smsCountSent: Int,
    val payloadJson: String
)
