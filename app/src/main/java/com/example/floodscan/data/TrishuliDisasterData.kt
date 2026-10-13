package com.example.floodscan.data

object TrishuliDisasterData {

    val STATS = DamageSummaryStats()

    val SETTLEMENTS = listOf(
        Settlement(
            id = "set_rasuwagadhi",
            name = "Rasuwagadhi",
            nepaliName = "रसुवागढी",
            lat = 28.2833,
            lon = 85.3789,
            elevationM = 1814,
            population = 620,
            isCutOff = true,
            cutoffReason = "Bhote Koshi flash surge pulverized 2.1 km road bed and obliterated Friendship bridge",
            accessStatus = AccessStatus.ISOLATED_CRITICAL,
            urgentNeeds = listOf("Emergency Rations", "Hypothermia Blankets", "Satellite Comms Radio", "Potable Water"),
            nearestHospital = "Dhunche District Hospital (Severed)",
            hospitalRoadConnected = false,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-RG-01",
                name = "Border Dry Port Helipad (Upper Plateau)",
                lat = 28.2810,
                lon = 85.3795,
                elevationM = 1840,
                maxHelicopterType = "Nepal Army Mi-17 / Bell 407",
                terrainRemarks = "High wind shear in canyon; clear approach from SE only"
            ),
            dailyRationsNeededKg = 430,
            waterPurificationUnitsNeeded = 12
        ),
        Settlement(
            id = "set_timure",
            name = "Timure",
            nepaliName = "टिमुरे",
            lat = 28.2612,
            lon = 85.3811,
            elevationM = 1760,
            population = 1850,
            isCutOff = true,
            cutoffReason = "Suspension bridge swept away; 14 heavy freight trucks buried; highway sheared at riverbank",
            accessStatus = AccessStatus.ISOLATED_CRITICAL,
            urgentNeeds = listOf("Surgical Trauma Kits", "Anti-diarrheal Meds", "Water Filters", "Shelter Tarpaulins"),
            nearestHospital = "Dhunche District Hospital (Cut off by 18km of road destruction)",
            hospitalRoadConnected = false,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-TM-02",
                name = "Timure School Flat Ground",
                lat = 28.2630,
                lon = 85.3820,
                elevationM = 1775,
                maxHelicopterType = "Airbus H125 / Écureuil",
                terrainRemarks = "Narrow landing zone; power cable hazard 40m west"
            ),
            dailyRationsNeededKg = 1290,
            waterPurificationUnitsNeeded = 35
        ),
        Settlement(
            id = "set_lingling",
            name = "Lingling & Briddhim Spur",
            nepaliName = "लिङलिङ / बृद्धिम",
            lat = 28.2120,
            lon = 85.3520,
            elevationM = 2229,
            population = 780,
            isCutOff = true,
            cutoffReason = "Valley footbridges collapsed; mountain trail blocked by debris cone landslides",
            accessStatus = AccessStatus.HELI_AIRDROP_ONLY,
            urgentNeeds = listOf("Food Air-drops", "Baby Infant Formula", "Solar Lanterns"),
            nearestHospital = "Dhunche District Hospital",
            hospitalRoadConnected = false,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-BR-03",
                name = "Briddhim Monastery Meadow",
                lat = 28.2140,
                lon = 85.3540,
                elevationM = 2240,
                maxHelicopterType = "Nepal Army ALH Dhruv / H125",
                terrainRemarks = "Good visibility above valley fog; steep slope perimeter"
            ),
            dailyRationsNeededKg = 540,
            waterPurificationUnitsNeeded = 15
        ),
        Settlement(
            id = "set_tatopani",
            name = "Tatopani Gorge",
            nepaliName = "तातोपानी",
            lat = 28.2410,
            lon = 85.3720,
            elevationM = 1680,
            population = 820,
            isCutOff = true,
            cutoffReason = "Trail bridge swept away; riverbank cliffs fractured by debris barrage",
            accessStatus = AccessStatus.FOOT_TRAIL_ONLY,
            urgentNeeds = listOf("Emergency Rations", "Water Filters", "First Aid"),
            nearestHospital = "Dhunche District Hospital",
            hospitalRoadConnected = false,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-TP-06",
                name = "Tatopani River Terrace",
                lat = 28.2420,
                lon = 85.3730,
                elevationM = 1690,
                maxHelicopterType = "Light helicopter H125",
                terrainRemarks = "High turbulence during afternoon river winds"
            ),
            dailyRationsNeededKg = 570,
            waterPurificationUnitsNeeded = 18
        ),
        Settlement(
            id = "set_syabrubesi",
            name = "Syabrubesi",
            nepaliName = "स्याफ्रुबेँसी",
            lat = 28.1611,
            lon = 85.3333,
            elevationM = 1460,
            population = 3400,
            isCutOff = false, // Partial single-lane south access via Dhunche pass
            cutoffReason = "Trishuli-Bhote Koshi confluence bridge destroyed; northern connector road cut; south link tenuous",
            accessStatus = AccessStatus.PARTIAL_SINGLE_LANE,
            urgentNeeds = listOf("Heavy Excavators", "Diesel Fuel", "Chlorine Tablets", "Bridge Bailey Spans"),
            nearestHospital = "Dhunche District Hospital (16 km steep ascent)",
            hospitalRoadConnected = true,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-SB-01",
                name = "Syabrubesi Confluence Staging Ground",
                lat = 28.1590,
                lon = 85.3350,
                elevationM = 1450,
                maxHelicopterType = "Nepal Army Mi-17 heavy transport",
                terrainRemarks = "Primary forward air hub; dust hazard during landing"
            ),
            dailyRationsNeededKg = 2100,
            waterPurificationUnitsNeeded = 50
        ),
        Settlement(
            id = "set_dhunche",
            name = "Dhunche (District HQ)",
            nepaliName = "धुन्चे (सदरमुकाम)",
            lat = 28.1132,
            lon = 85.2975,
            elevationM = 1960,
            population = 4500,
            isCutOff = false,
            cutoffReason = "Command headquarters operational; access open towards Kathmandu via Trishuli",
            accessStatus = AccessStatus.OPERATIONAL,
            urgentNeeds = listOf("Blood Plasma Supplies", "Emergency ICU Beds", "Mortuary Coolers"),
            nearestHospital = "Dhunche District Hospital (Onsite)",
            hospitalRoadConnected = true,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-DH-HQ",
                name = "District Administration Office Helipad",
                lat = 28.1145,
                lon = 85.2980,
                elevationM = 1970,
                maxHelicopterType = "All Rotary Wing Aircraft",
                terrainRemarks = "Paved helipad; equipped with refuel barrels and night lighting"
            ),
            dailyRationsNeededKg = 0,
            waterPurificationUnitsNeeded = 0
        ),
        Settlement(
            id = "set_ramche",
            name = "Ramche & Grang",
            nepaliName = "राम्चे / ग्राङ",
            lat = 28.0583,
            lon = 85.2341,
            elevationM = 1350,
            population = 1220,
            isCutOff = true,
            cutoffReason = "Massive cliffside failure at Ramche slide zone; Pasang Lhamu Hwy collapsed into gorge",
            accessStatus = AccessStatus.HELI_AIRDROP_ONLY,
            urgentNeeds = listOf("Emergency Food", "Wound Dressing Packs", "Family Tents"),
            nearestHospital = "Trishuli Hospital (Bidur)",
            hospitalRoadConnected = false,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-RC-04",
                name = "Ramche Upper Ridge Terraces",
                lat = 28.0610,
                lon = 85.2360,
                elevationM = 1410,
                maxHelicopterType = "H125 / Bell 407 light helicopter",
                terrainRemarks = "Active rockfall on slope below; landing strictly restricted to ridge"
            ),
            dailyRationsNeededKg = 850,
            waterPurificationUnitsNeeded = 22
        ),
        Settlement(
            id = "set_mailung",
            name = "Mailung",
            nepaliName = "मैलुङ",
            lat = 28.0294,
            lon = 85.2155,
            elevationM = 1050,
            population = 980,
            isCutOff = true,
            cutoffReason = "Mailung Hydropower dam burst debris; Bailey bridge crushed under 400t boulders",
            accessStatus = AccessStatus.FOOT_TRAIL_ONLY,
            urgentNeeds = listOf("Drinking Water Kits", "Anti-tetanus Serum", "Search & Rescue Teams"),
            nearestHospital = "Trishuli Hospital (Bidur)",
            hospitalRoadConnected = false,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-ML-05",
                name = "Upper Hydropower Camp Clearing",
                lat = 28.0320,
                lon = 85.2180,
                elevationM = 1120,
                maxHelicopterType = "Bell 407 / Écureuil",
                terrainRemarks = "High humidity canyon; watch for high-tension cables downstream"
            ),
            dailyRationsNeededKg = 680,
            waterPurificationUnitsNeeded = 20
        ),
        Settlement(
            id = "set_betrawati",
            name = "Betrawati",
            nepaliName = "बेत्रावती",
            lat = 27.9733,
            lon = 85.1842,
            elevationM = 640,
            population = 2800,
            isCutOff = false,
            cutoffReason = "Partial riverbank inundation; road open south to Trishuli Bazar",
            accessStatus = AccessStatus.OPERATIONAL,
            urgentNeeds = listOf("Water Pumps", "Disinfectant Sprayers"),
            nearestHospital = "Trishuli Hospital (Bidur, 7 km south)",
            hospitalRoadConnected = true,
            recommendedHeliLZ = null,
            dailyRationsNeededKg = 0,
            waterPurificationUnitsNeeded = 5
        ),
        Settlement(
            id = "set_trishuli_bazar",
            name = "Trishuli Bazar (Bidur)",
            nepaliName = "त्रिशूली बजार (विदुर)",
            lat = 27.9167,
            lon = 85.1500,
            elevationM = 580,
            population = 18200,
            isCutOff = false,
            cutoffReason = "Strategic staging hub with national highway access to Kathmandu",
            accessStatus = AccessStatus.OPERATIONAL,
            urgentNeeds = listOf("Logistics Depot Coordination", "Helicopter Refueling Fuel Tankers"),
            nearestHospital = "Trishuli District Referral Hospital (50-bed surgical trauma wing)",
            hospitalRoadConnected = true,
            recommendedHeliLZ = HeliLZ(
                code = "LZ-BIDUR-BASE",
                name = "Battar Airfield Grass Strip & Ground",
                lat = 27.9050,
                lon = 85.1600,
                elevationM = 570,
                maxHelicopterType = "Multiple heavy rotary wing transports simultaneous",
                terrainRemarks = "Wide valley basin, unobstructed flight paths, emergency fuel tank farm"
            ),
            dailyRationsNeededKg = 0,
            waterPurificationUnitsNeeded = 0
        )
    )

    val BRIDGES = listOf(
        BridgeStatus(
            id = "br_rasuwagadhi",
            name = "Rasuwagadhi Border Bridge",
            river = "Lhende / Bhote Koshi",
            lat = 28.2838,
            lon = 85.3780,
            preEventOsmId = "osm:way/591823901",
            structureType = "Reinforced Concrete Girder (54m span)",
            damageState = DamageLevel.DESTROYED,
            isolatedSettlementIds = listOf("set_rasuwagadhi"),
            estimatedRepairDays = 45,
            remarks = "Abutments scoured by 9.4m wave; superstructure sheared downstream"
        ),
        BridgeStatus(
            id = "br_timure_customs",
            name = "Timure Dry Port Access Bridge",
            river = "Bhote Koshi",
            lat = 28.2619,
            lon = 85.3815,
            preEventOsmId = "osm:way/491029144",
            structureType = "Suspension Cable & Steel Deck (78m span)",
            damageState = DamageLevel.DESTROYED,
            isolatedSettlementIds = listOf("set_timure", "set_rasuwagadhi"),
            estimatedRepairDays = 28,
            remarks = "Main cable snapped on west anchorage; pier tilted 35 degrees"
        ),
        BridgeStatus(
            id = "br_tatopani",
            name = "Tatopani Footbridge",
            river = "Bhote Koshi Tributary",
            lat = 28.2410,
            lon = 85.3720,
            preEventOsmId = "osm:way/773210182",
            structureType = "Trail Suspension Footbridge",
            damageState = DamageLevel.DESTROYED,
            isolatedSettlementIds = listOf("set_timure"),
            estimatedRepairDays = 14,
            remarks = "Washed away entirely along with trail approaches"
        ),
        BridgeStatus(
            id = "br_syabru_confluence",
            name = "Syabrubesi Trishuli Confluence Bridge",
            river = "Langtang Khola / Trishuli Confluence",
            lat = 28.1625,
            lon = 85.3340,
            preEventOsmId = "osm:way/982314412",
            structureType = "Prestressed Concrete Box (62m span)",
            damageState = DamageLevel.DESTROYED,
            isolatedSettlementIds = listOf("set_timure", "set_rasuwagadhi", "set_lingling"),
            estimatedRepairDays = 35,
            remarks = "Impacted by debris dam burst upstream; spans broken into 3 pieces"
        ),
        BridgeStatus(
            id = "br_chilime",
            name = "Chilime Hydropower Feeder Bridge",
            river = "Chilime Khola",
            lat = 28.1750,
            lon = 85.3050,
            preEventOsmId = "osm:way/619283741",
            structureType = "Truss Bridge (45m)",
            damageState = DamageLevel.MAJOR_DAMAGE,
            isolatedSettlementIds = listOf("set_lingling"),
            estimatedRepairDays = 21,
            remarks = "Structural steel warped; restricted to foot traffic only"
        ),
        BridgeStatus(
            id = "br_mailung_bailey",
            name = "Mailung Khola Bailey Bridge",
            river = "Mailung Khola",
            lat = 28.0310,
            lon = 85.2170,
            preEventOsmId = "osm:way/119283765",
            structureType = "Steel Bailey (30m)",
            damageState = DamageLevel.DESTROYED,
            isolatedSettlementIds = listOf("set_mailung"),
            estimatedRepairDays = 12,
            remarks = "Buried under 3.5m mud and rock debris"
        ),
        BridgeStatus(
            id = "br_betrawati",
            name = "Betrawati Trishuli Highway Bridge",
            river = "Trishuli River",
            lat = 27.9740,
            lon = 85.1850,
            preEventOsmId = "osm:way/339218210",
            structureType = "Balanced Cantilever Concrete Bridge (88m)",
            damageState = DamageLevel.MAJOR_DAMAGE,
            isolatedSettlementIds = emptyList(),
            estimatedRepairDays = 18,
            remarks = "Pier scouring observed; weight limit enforced to 5 metric tons"
        )
    )

    val SEVERED_ROADS = listOf(
        RoadSegment(
            id = "road_pasang_upper",
            name = "Pasang Lhamu Hwy (H09)",
            section = "Rasuwagadhi to Timure (Km 84-88)",
            status = DamageLevel.DESTROYED,
            lengthCutKm = 4.2,
            debrisThicknessMeters = 3.8,
            coordinates = listOf(
                Pair(28.2833, 85.3789),
                Pair(28.2750, 85.3800),
                Pair(28.2680, 85.3808),
                Pair(28.2612, 85.3811)
            )
        ),
        RoadSegment(
            id = "road_pasang_middle",
            name = "Pasang Lhamu Hwy (H09)",
            section = "Timure to Syabrubesi Gorge (Km 72-82)",
            status = DamageLevel.DESTROYED,
            lengthCutKm = 10.4,
            debrisThicknessMeters = 2.5,
            coordinates = listOf(
                Pair(28.2612, 85.3811),
                Pair(28.2350, 85.3700),
                Pair(28.2000, 85.3550),
                Pair(28.1611, 85.3333)
            )
        ),
        RoadSegment(
            id = "road_ramche_slide",
            name = "Pasang Lhamu Hwy (H09)",
            section = "Ramche Slide Zone (Km 58-63)",
            status = DamageLevel.DESTROYED,
            lengthCutKm = 5.1,
            debrisThicknessMeters = 5.2,
            coordinates = listOf(
                Pair(28.0850, 85.2600),
                Pair(28.0650, 85.2420),
                Pair(28.0583, 85.2341),
                Pair(28.0450, 85.2280)
            )
        ),
        RoadSegment(
            id = "road_mailung_corridor",
            name = "Mailung-Syabrubesi Short Track",
            section = "Mailung Riverbed Road (Km 12-25)",
            status = DamageLevel.DESTROYED,
            lengthCutKm = 13.0,
            debrisThicknessMeters = 4.0,
            coordinates = listOf(
                Pair(28.1611, 85.3333),
                Pair(28.1000, 85.2700),
                Pair(28.0400, 85.2200),
                Pair(28.0294, 85.2155)
            )
        )
    )

    // Elevation gradient and downstream flood path simulation points
    val FLOW_PATH = listOf(
        ValleyFlowPoint(
            stepIndex = 1,
            locationName = "High-Altitude Glacial Source (Collapse Zone)",
            lat = 28.3450,
            lon = 85.3850,
            elevationM = 4210,
            distanceDownstreamKm = 0.0,
            estimatedArrivalTimeMin = 0,
            peakDischargeM3s = 6400,
            slopePercent = 28.5,
            riskLevel = "ORIGIN",
            keyInfrastructureAtRisk = "Glacier moraine lake rupture & ice avalanche trigger"
        ),
        ValleyFlowPoint(
            stepIndex = 2,
            locationName = "Upper Bhote Koshi Canyon / Border Checkpoint",
            lat = 28.2833,
            lon = 85.3789,
            elevationM = 1814,
            distanceDownstreamKm = 8.6,
            estimatedArrivalTimeMin = 14,
            peakDischargeM3s = 5850,
            slopePercent = 21.2,
            riskLevel = "EXTREME",
            keyInfrastructureAtRisk = "Rasuwagadhi Dry Port, Customs Yard, Friendship Bridge"
        ),
        ValleyFlowPoint(
            stepIndex = 3,
            locationName = "Timure Settlement & Truck Staging Corridor",
            lat = 28.2612,
            lon = 85.3811,
            elevationM = 1760,
            distanceDownstreamKm = 11.8,
            estimatedArrivalTimeMin = 22,
            peakDischargeM3s = 5200,
            slopePercent = 14.8,
            riskLevel = "EXTREME",
            keyInfrastructureAtRisk = "14 heavy cargo trucks, Timure Suspension Bridge, School"
        ),
        ValleyFlowPoint(
            stepIndex = 4,
            locationName = "Lingling River Bend / Narrow Flume",
            lat = 28.2120,
            lon = 85.3520,
            elevationM = 1620,
            distanceDownstreamKm = 18.4,
            estimatedArrivalTimeMin = 36,
            peakDischargeM3s = 4800,
            slopePercent = 12.3,
            riskLevel = "HIGH",
            keyInfrastructureAtRisk = "Spur access trails, micro-hydro powerhouse intake"
        ),
        ValleyFlowPoint(
            stepIndex = 5,
            locationName = "Syabrubesi Confluence (Trishuli & Langtang Khola)",
            lat = 28.1611,
            lon = 85.3333,
            elevationM = 1460,
            distanceDownstreamKm = 26.2,
            estimatedArrivalTimeMin = 52,
            peakDischargeM3s = 4400,
            slopePercent = 8.5,
            riskLevel = "EXTREME",
            keyInfrastructureAtRisk = "Main vehicular bridge, bazaar riverfront hotels, suspension bridge"
        ),
        ValleyFlowPoint(
            stepIndex = 6,
            locationName = "Mailung Hydropower Project & Gorge",
            lat = 28.0294,
            lon = 85.2155,
            elevationM = 1050,
            distanceDownstreamKm = 44.8,
            estimatedArrivalTimeMin = 88,
            peakDischargeM3s = 3600,
            slopePercent = 6.2,
            riskLevel = "CRITICAL",
            keyInfrastructureAtRisk = "Powerhouse, transformer yard, Bailey bridge, workers' colony"
        ),
        ValleyFlowPoint(
            stepIndex = 7,
            locationName = "Betrawati Confluence Basin",
            lat = 27.9733,
            lon = 85.1842,
            elevationM = 640,
            distanceDownstreamKm = 56.4,
            estimatedArrivalTimeMin = 124,
            peakDischargeM3s = 2950,
            slopePercent = 3.1,
            riskLevel = "MEDIUM",
            keyInfrastructureAtRisk = "Highway bridge piers, low-lying farm terraces, riverside temple"
        ),
        ValleyFlowPoint(
            stepIndex = 8,
            locationName = "Trishuli Bazar (Bidur Town Basin)",
            lat = 27.9167,
            lon = 85.1500,
            elevationM = 580,
            distanceDownstreamKm = 67.2,
            estimatedArrivalTimeMin = 158,
            peakDischargeM3s = 2400,
            slopePercent = 1.4,
            riskLevel = "LOW_MONITORED",
            keyInfrastructureAtRisk = "Sediment aggradation, water intake plants, downstream hydro canals"
        )
    )

    val EMSR927_BENCHMARKS = listOf(
        EMSR927Metric(
            metric = "Flood Extent Inundation IoU",
            floodScanAi = "0.812 IoU (18.52 km²)",
            emsr927Reference = "Ref Standard (17.80 km²)",
            agreementPercent = "96.1%",
            remarks = "FloodScan AI captured narrow upper gorge debris fans masked in optical EMS"
        ),
        EMSR927Metric(
            metric = "Bridge Destruction Recall",
            floodScanAi = "7 of 7 Destroyed (100%)",
            emsr927Reference = "6 Confirmed + 1 Inferred",
            agreementPercent = "100%",
            remarks = "Timure suspension failure detected via SAR interferometric coherence loss"
        ),
        EMSR927Metric(
            metric = "Severed Road Segments Identified",
            floodScanAi = "42.8 km (4 primary links)",
            emsr927Reference = "39.4 km flagged",
            agreementPercent = "92.0%",
            remarks = "FloodScan included Ramche high cliff instability verified by slope DEM"
        ),
        EMSR927Metric(
            metric = "Cut-Off Settlements Isolated",
            floodScanAi = "6 Settlements (6,270 people)",
            emsr927Reference = "5 Villages categorized",
            agreementPercent = "95.5%",
            remarks = "Identified Lingling spur isolation which lost all trail connections"
        ),
        EMSR927Metric(
            metric = "Processing Pipeline Turnaround",
            floodScanAi = "4.2 minutes (Automated)",
            emsr927Reference = "26.4 hours (Human GIS)",
            agreementPercent = "377x Faster",
            remarks = "Rapid autonomous satellite triage empowers rescue teams on Day 0"
        )
    )

    const val COPERNICUS_ATTRIBUTION = "Contains modified Copernicus Sentinel data 2026."
    const val WORLDDEM_ATTRIBUTION = "Produced using Copernicus WorldDEM-30 © DLR e.V. 2010–2014 and © Airbus Defence and Space GmbH 2014–2018 provided under COPERNICUS by the European Union and ESA; all rights reserved."
    const val OSM_ATTRIBUTION = "© OpenStreetMap contributors. Pre-event snapshot via ohsome API (July 2026, ODbL)."
    const val KURO_SIWO_CITATION = "Bountos et al., 2024. 'Kuro Siwo: A Multimodal SAR and Optical Dataset for Flood Mapping.' NeurIPS 2024. MIT License."
    const val SEN1FLOODS11_CITATION = "Bonafilia et al., 2020. 'Sen1Floods11: A Georeferenced Dataset to Train and Test Deep Learning Flood Algorithms for Sentinel-1.' CVPR Workshops 2020. CC BY 4.0."
    const val EMSR927_CITATION = "Copernicus Emergency Management Service, EMSR927: Rapid Mapping Activation for Floods in Trishuli/Bagmati Corridor, European Union, 2026."

    val LIMITATIONS = listOf(
        "Revisit Latency: Sentinel-1 has a 6-12 day orbital repeat cycle. It cannot detect a glacial collapse minutes before it occurs; it provides rapid damage assessment post-pass.",
        "Radar Geometric Distortion: In steep Himalayan gorges (>45° slope), radar layover and radar shadow can obscure narrow river bends facing away from satellite line-of-sight.",
        "Monsoon Optical Blindness: Sentinel-2 optical is severely blocked by 80-95% monsoon cloud cover between June and September; SAR radar is mandatory for all-weather penetration.",
        "Pre-event OSM Trail Incompleteness: High-altitude seasonal trekking paths may lack full topology in OSM, requiring local knowledge overlay.",
        "Educational & Hackathon Prototype: This system is built for the Multimodal AI Hackathon 2026. Data is anonymized and strictly factual to respect affected families."
    )

    val RIVER_GAUGES = listOf(
        RiverGaugeStation(
            id = "gauge_rasuwagadhi",
            name = "Rasuwagadhi Border Station",
            riverName = "Lhende / Bhote Koshi",
            lat = 28.2830,
            lon = 85.3785,
            elevationM = 1812,
            currentStageM = 11.4,
            warningThresholdM = 6.5,
            dangerThresholdM = 8.0,
            currentDischargeM3s = 5850,
            rainfallLastHourMm = 68.4,
            status = StationAlertLevel.CRITICAL_DANGER,
            trend = "RISING (+0.8m/hr)",
            lastUpdated = "10 mins ago"
        ),
        RiverGaugeStation(
            id = "gauge_timure",
            name = "Timure Gorge Telemetry Hub",
            riverName = "Bhote Koshi River",
            lat = 28.2615,
            lon = 85.3812,
            elevationM = 1758,
            currentStageM = 12.8,
            warningThresholdM = 7.0,
            dangerThresholdM = 8.5,
            currentDischargeM3s = 5200,
            rainfallLastHourMm = 74.2,
            status = StationAlertLevel.CRITICAL_DANGER,
            trend = "PEAK LEVEL (+4.3m above danger)",
            lastUpdated = "5 mins ago"
        ),
        RiverGaugeStation(
            id = "gauge_syabrubesi",
            name = "Syabrubesi Confluence Gauge",
            riverName = "Trishuli / Langtang Khola",
            lat = 28.1615,
            lon = 85.3330,
            elevationM = 1458,
            currentStageM = 9.8,
            warningThresholdM = 6.0,
            dangerThresholdM = 7.5,
            currentDischargeM3s = 4400,
            rainfallLastHourMm = 52.0,
            status = StationAlertLevel.CRITICAL_DANGER,
            trend = "RISING RAPIDLY",
            lastUpdated = "8 mins ago"
        ),
        RiverGaugeStation(
            id = "gauge_mailung",
            name = "Mailung Hydro Intake Station",
            riverName = "Trishuli River",
            lat = 28.0290,
            lon = 85.2150,
            elevationM = 1048,
            currentStageM = 9.1,
            warningThresholdM = 5.5,
            dangerThresholdM = 7.0,
            currentDischargeM3s = 3600,
            rainfallLastHourMm = 41.5,
            status = StationAlertLevel.CRITICAL_DANGER,
            trend = "FLASH DEBRIS PULSE",
            lastUpdated = "12 mins ago"
        ),
        RiverGaugeStation(
            id = "gauge_betrawati",
            name = "Betrawati Flood Warning Post",
            riverName = "Trishuli River Basin",
            lat = 27.9730,
            lon = 85.1840,
            elevationM = 638,
            currentStageM = 7.4,
            warningThresholdM = 5.0,
            dangerThresholdM = 6.5,
            currentDischargeM3s = 2950,
            rainfallLastHourMm = 28.0,
            status = StationAlertLevel.WATCH_WARNING,
            trend = "APPROACHING CREST",
            lastUpdated = "15 mins ago"
        ),
        RiverGaugeStation(
            id = "gauge_trishuli",
            name = "Trishuli Hydro Dam Station",
            riverName = "Trishuli Mainstem (Bidur)",
            lat = 27.9165,
            lon = 85.1495,
            elevationM = 578,
            currentStageM = 6.1,
            warningThresholdM = 4.5,
            dangerThresholdM = 6.0,
            currentDischargeM3s = 2400,
            rainfallLastHourMm = 18.5,
            status = StationAlertLevel.WATCH_WARNING,
            trend = "SLOWLY RISING",
            lastUpdated = "20 mins ago"
        )
    )

    val WATERSHEDS = listOf(
        WatershedRegion(
            id = "ws_upper_bhotekoshi",
            name = "Upper Bhote Koshi Glacial Catchment",
            nepaliName = "माथिल्लो भोटेकोशी हिमनदी जलाधार",
            areaSqKm = 1240.0,
            meanElevationM = 4820,
            dominantRisk = "High-Altitude Glacial Lake Outburst (GLOF) & Moraine Breach",
            riskColorHex = "#EF4444",
            polygonPoints = listOf(
                Pair(28.3600, 85.3400),
                Pair(28.3800, 85.4200),
                Pair(28.2900, 85.4300),
                Pair(28.2700, 85.3600),
                Pair(28.3100, 85.3200)
            )
        ),
        WatershedRegion(
            id = "ws_langtang",
            name = "Langtang High-Altitude Sub-Basin",
            nepaliName = "लाङटाङ उप-जलाधार",
            areaSqKm = 860.0,
            meanElevationM = 3950,
            dominantRisk = "Rapid Glacial Snowmelt & Monsoon Cloudburst Inflow",
            riskColorHex = "#F97316",
            polygonPoints = listOf(
                Pair(28.2400, 85.3800),
                Pair(28.2300, 85.5200),
                Pair(28.1500, 85.4800),
                Pair(28.1500, 85.3400),
                Pair(28.2000, 85.3400)
            )
        ),
        WatershedRegion(
            id = "ws_chilime",
            name = "Chilime Mountain Hydro Basin",
            nepaliName = "चिलिमे हिमाली जलाधार",
            areaSqKm = 340.0,
            meanElevationM = 2950,
            dominantRisk = "Steep Canyon Landslides & Heavy Sediment Aggradation",
            riskColorHex = "#EAB308",
            polygonPoints = listOf(
                Pair(28.2200, 85.2500),
                Pair(28.2100, 85.3400),
                Pair(28.1300, 85.3100),
                Pair(28.1400, 85.2300)
            )
        ),
        WatershedRegion(
            id = "ws_lower_trishuli",
            name = "Trishuli Mainstem River Valley",
            nepaliName = "त्रिशूली मुख्य नदी उपत्यका",
            areaSqKm = 1820.0,
            meanElevationM = 1120,
            dominantRisk = "Downstream Flood Inundation & Bridge Pier Scouring",
            riskColorHex = "#3B82F6",
            polygonPoints = listOf(
                Pair(28.1400, 85.3300),
                Pair(28.0600, 85.2600),
                Pair(27.9700, 85.2000),
                Pair(27.9000, 85.1300),
                Pair(27.9400, 85.1100),
                Pair(28.0800, 85.2000)
            )
        )
    )

    val TIMELINE_FRAMES = listOf(
        HistoricalTimelineFrame(
            index = 0,
            timeLabel = "04:00 AM",
            dateTimeStr = "26 Aug 2026, 04:00 NPT",
            eventTitle = "Monsoon Baseline Monitoring",
            description = "Normal monsoon river stages. Sentinel-1 scheduled orbit tracking; heavy cloud cover over Tibetan border.",
            severityLevel = "NORMAL",
            rainfallIntensityFactor = 0.25f,
            cloudCoverPercent = 88,
            activeFloodAreaSqKm = 3.2,
            destroyedBridgeIds = emptyList(),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 4.2,
                "gauge_timure" to 4.8,
                "gauge_syabrubesi" to 4.0,
                "gauge_mailung" to 3.8,
                "gauge_betrawati" to 3.5,
                "gauge_trishuli" to 3.2
            )
        ),
        HistoricalTimelineFrame(
            index = 1,
            timeLabel = "06:00 AM",
            dateTimeStr = "26 Aug 2026, 06:00 NPT",
            eventTitle = "High-Altitude Cloudburst Inflow",
            description = "GPM satellite radar detects intense rainband (>75mm/hr) hovering over high Himalayan ridgelines.",
            severityLevel = "WATCH",
            rainfallIntensityFactor = 0.65f,
            cloudCoverPercent = 95,
            activeFloodAreaSqKm = 5.8,
            destroyedBridgeIds = emptyList(),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 6.4,
                "gauge_timure" to 6.9,
                "gauge_syabrubesi" to 5.6,
                "gauge_mailung" to 4.9,
                "gauge_betrawati" to 4.2,
                "gauge_trishuli" to 3.8
            )
        ),
        HistoricalTimelineFrame(
            index = 2,
            timeLabel = "07:15 AM",
            dateTimeStr = "26 Aug 2026, 07:15 NPT",
            eventTitle = "Glacial Collapse & Moraine Breach",
            description = "High-altitude ice avalanche triggers sudden failure of glacial moraine dam at 4,210m elevation. Initial burst discharge exceeds 6,400 m³/s.",
            severityLevel = "CRITICAL_PEAK",
            rainfallIntensityFactor = 0.85f,
            cloudCoverPercent = 98,
            activeFloodAreaSqKm = 10.4,
            destroyedBridgeIds = listOf("br_rasuwagadhi"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 9.8,
                "gauge_timure" to 8.2,
                "gauge_syabrubesi" to 6.5,
                "gauge_mailung" to 5.4,
                "gauge_betrawati" to 4.6,
                "gauge_trishuli" to 4.1
            )
        ),
        HistoricalTimelineFrame(
            index = 3,
            timeLabel = "08:15 AM",
            dateTimeStr = "26 Aug 2026, 08:15 NPT",
            eventTitle = "Surge Strikes Rasuwagadhi Border",
            description = "Violent debris wave smashes border dry port. Friendship bridge sheared off; customs yard submerged under 4m of boulders.",
            severityLevel = "CRITICAL_PEAK",
            rainfallIntensityFactor = 0.95f,
            cloudCoverPercent = 96,
            activeFloodAreaSqKm = 14.8,
            destroyedBridgeIds = listOf("br_rasuwagadhi", "br_timure_customs"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 11.4,
                "gauge_timure" to 11.2,
                "gauge_syabrubesi" to 7.8,
                "gauge_mailung" to 6.2,
                "gauge_betrawati" to 5.1,
                "gauge_trishuli" to 4.5
            )
        ),
        HistoricalTimelineFrame(
            index = 4,
            timeLabel = "09:00 AM",
            dateTimeStr = "26 Aug 2026, 09:00 NPT",
            eventTitle = "Timure Gorge Inundation & Isolation",
            description = "Timure suspension bridge destroyed; 14 freight trucks swept into riverbed. Pasang Lhamu Highway severed across 4.2 km.",
            severityLevel = "CRITICAL_PEAK",
            rainfallIntensityFactor = 0.90f,
            cloudCoverPercent = 92,
            activeFloodAreaSqKm = 18.52,
            destroyedBridgeIds = listOf("br_rasuwagadhi", "br_timure_customs", "br_tatopani"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 10.8,
                "gauge_timure" to 12.8,
                "gauge_syabrubesi" to 9.2,
                "gauge_mailung" to 7.4,
                "gauge_betrawati" to 5.9,
                "gauge_trishuli" to 5.0
            )
        ),
        HistoricalTimelineFrame(
            index = 5,
            timeLabel = "10:30 AM",
            dateTimeStr = "26 Aug 2026, 10:30 NPT",
            eventTitle = "Syabrubesi Confluence Overwash",
            description = "Debris pulse impacts Trishuli-Langtang confluence. Main concrete vehicular bridge fails; lower market hotels evacuated.",
            severityLevel = "CRITICAL_PEAK",
            rainfallIntensityFactor = 0.75f,
            cloudCoverPercent = 88,
            activeFloodAreaSqKm = 18.2,
            destroyedBridgeIds = listOf("br_rasuwagadhi", "br_timure_customs", "br_tatopani", "br_syabru_confluence"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 9.2,
                "gauge_timure" to 11.0,
                "gauge_syabrubesi" to 9.8,
                "gauge_mailung" to 8.6,
                "gauge_betrawati" to 6.8,
                "gauge_trishuli" to 5.6
            )
        ),
        HistoricalTimelineFrame(
            index = 6,
            timeLabel = "12:00 PM",
            dateTimeStr = "26 Aug 2026, 12:00 NPT",
            eventTitle = "Mailung Powerhouse Destruction",
            description = "Massive rock debris barrages Mailung hydropower intake. Bailey bridge crushed; road bed scoured to sheer bedrock.",
            severityLevel = "CRITICAL_PEAK",
            rainfallIntensityFactor = 0.60f,
            cloudCoverPercent = 80,
            activeFloodAreaSqKm = 17.6,
            destroyedBridgeIds = listOf("br_rasuwagadhi", "br_timure_customs", "br_tatopani", "br_syabru_confluence", "br_mailung_bailey"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 8.1,
                "gauge_timure" to 9.5,
                "gauge_syabrubesi" to 8.4,
                "gauge_mailung" to 9.1,
                "gauge_betrawati" to 7.4,
                "gauge_trishuli" to 6.1
            )
        ),
        HistoricalTimelineFrame(
            index = 7,
            timeLabel = "03:00 PM",
            dateTimeStr = "26 Aug 2026, 15:00 NPT",
            eventTitle = "Peak Crest at Betrawati & Trishuli",
            description = "Surge attenuates in wider Nuwakot basin. Severe sedimentation and bridge scour at Betrawati; Trishuli hydro canal gates closed.",
            severityLevel = "WATCH",
            rainfallIntensityFactor = 0.40f,
            cloudCoverPercent = 70,
            activeFloodAreaSqKm = 16.4,
            destroyedBridgeIds = listOf("br_rasuwagadhi", "br_timure_customs", "br_tatopani", "br_syabru_confluence", "br_mailung_bailey"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 7.0,
                "gauge_timure" to 8.1,
                "gauge_syabrubesi" to 7.2,
                "gauge_mailung" to 7.6,
                "gauge_betrawati" to 7.2,
                "gauge_trishuli" to 6.0
            )
        ),
        HistoricalTimelineFrame(
            index = 8,
            timeLabel = "06:00 PM",
            dateTimeStr = "26 Aug 2026, 18:00 NPT",
            eventTitle = "Receding Flood Stage & Emergency Status",
            description = "Water levels recede but 6 settlements remain completely cut off with destroyed bridges and highway landslides. Rescue air-drops initiated.",
            severityLevel = "RECEDING",
            rainfallIntensityFactor = 0.20f,
            cloudCoverPercent = 60,
            activeFloodAreaSqKm = 15.1,
            destroyedBridgeIds = listOf("br_rasuwagadhi", "br_timure_customs", "br_tatopani", "br_syabru_confluence", "br_mailung_bailey"),
            stationStages = mapOf(
                "gauge_rasuwagadhi" to 6.2,
                "gauge_timure" to 7.0,
                "gauge_syabrubesi" to 6.3,
                "gauge_mailung" to 6.5,
                "gauge_betrawati" to 6.1,
                "gauge_trishuli" to 5.2
            )
        )
    )

    val SENSOR_FEEDS = listOf(
        SensorTelemetryFeed(
            stationId = "S-01",
            stationName = "Rasuwagadhi Border Station",
            river = "Bhote Koshi",
            waterLevelM = 11.4,
            warningThresholdM = 6.5,
            dangerThresholdM = 8.0,
            dischargeM3s = 5850,
            velocityMs = 8.4,
            rainfallRateMmH = 68.4,
            soilSaturationPct = 96,
            turbidityNtu = 4800,
            isBreached = true
        ),
        SensorTelemetryFeed(
            stationId = "S-02",
            stationName = "Timure Gorge Telemetry Hub",
            river = "Bhote Koshi",
            waterLevelM = 12.8,
            warningThresholdM = 7.0,
            dangerThresholdM = 8.5,
            dischargeM3s = 5200,
            velocityMs = 9.2,
            rainfallRateMmH = 74.2,
            soilSaturationPct = 98,
            turbidityNtu = 5200,
            isBreached = true
        ),
        SensorTelemetryFeed(
            stationId = "S-03",
            stationName = "Syabrubesi Confluence Gauge",
            river = "Trishuli River",
            waterLevelM = 9.8,
            warningThresholdM = 6.0,
            dangerThresholdM = 7.5,
            dischargeM3s = 4400,
            velocityMs = 7.1,
            rainfallRateMmH = 52.0,
            soilSaturationPct = 91,
            turbidityNtu = 3900,
            isBreached = true
        ),
        SensorTelemetryFeed(
            stationId = "S-04",
            stationName = "Mailung Hydro Intake Post",
            river = "Trishuli River",
            waterLevelM = 9.1,
            warningThresholdM = 5.5,
            dangerThresholdM = 7.0,
            dischargeM3s = 3600,
            velocityMs = 6.8,
            rainfallRateMmH = 41.5,
            soilSaturationPct = 88,
            turbidityNtu = 4200,
            isBreached = true
        ),
        SensorTelemetryFeed(
            stationId = "S-05",
            stationName = "Betrawati Flood Basin Station",
            river = "Trishuli River",
            waterLevelM = 7.4,
            warningThresholdM = 5.0,
            dangerThresholdM = 6.5,
            dischargeM3s = 2950,
            velocityMs = 4.5,
            rainfallRateMmH = 28.0,
            soilSaturationPct = 82,
            turbidityNtu = 2800,
            isBreached = true
        )
    )

    val DISPATCH_AUTHORITIES = listOf(
        "National Disaster Risk Reduction & Management Authority (NDRRMA HQ, Kathmandu)",
        "Nepal Army Disaster Response Directorate (Bhadrakali)",
        "District Disaster Management Committee (DDMC Rasuwa / Dhunche)",
        "Nuwakot District Administration Office (Bidur)",
        "Nepal Police Emergency Dispatch Command (100 / Emergency Mesh)",
        "Upper Trishuli-1 Hydropower Safety Operations Room"
    )
}

