package com.example.floodscan.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.example.floodscan.data.*
import org.json.JSONArray
import org.json.JSONObject

class LeafletBridge(
    private val onGaugeClick: (String) -> Unit,
    private val onWatershedClick: (String) -> Unit
) {
    @JavascriptInterface
    fun onGaugeSelected(gaugeId: String) {
        onGaugeClick(gaugeId)
    }

    @JavascriptInterface
    fun onWatershedSelected(wsId: String) {
        onWatershedClick(wsId)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeafletGisMapView(
    timelineFrameIndex: Int,
    showWatersheds: Boolean,
    showRiverGauges: Boolean,
    showRainfallHeatmap: Boolean,
    showCloudCover: Boolean,
    selectedGaugeId: String?,
    onGaugeSelected: (RiverGaugeStation) -> Unit,
    onWatershedSelected: (WatershedRegion) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isMapLoaded by remember { mutableStateOf(false) }

    val bridge = remember {
        LeafletBridge(
            onGaugeClick = { id ->
                val gauge = TrishuliDisasterData.RIVER_GAUGES.find { it.id == id }
                gauge?.let { onGaugeSelected(it) }
            },
            onWatershedClick = { id ->
                val ws = TrishuliDisasterData.WATERSHEDS.find { it.id == id }
                ws?.let { onWatershedSelected(it) }
            }
        )
    }

    // Prepare JSON payload for the initial Leaflet map injection
    val gaugesJson = remember {
        JSONArray().apply {
            TrishuliDisasterData.RIVER_GAUGES.forEach { g ->
                put(JSONObject().apply {
                    put("id", g.id)
                    put("name", g.name)
                    put("river", g.riverName)
                    put("lat", g.lat)
                    put("lon", g.lon)
                    put("stage", g.currentStageM)
                    put("warning", g.warningThresholdM)
                    put("danger", g.dangerThresholdM)
                    put("discharge", g.currentDischargeM3s)
                    put("rainMm", g.rainfallLastHourMm)
                    put("status", g.status.name)
                })
            }
        }.toString()
    }

    val watershedsJson = remember {
        JSONArray().apply {
            TrishuliDisasterData.WATERSHEDS.forEach { ws ->
                put(JSONObject().apply {
                    put("id", ws.id)
                    put("name", ws.name)
                    put("nepali", ws.nepaliName)
                    put("area", ws.areaSqKm)
                    put("elev", ws.meanElevationM)
                    put("risk", ws.dominantRisk)
                    put("color", ws.riskColorHex)
                    val pts = JSONArray()
                    ws.polygonPoints.forEach { pt ->
                        val coord = JSONArray().apply {
                            put(pt.first)
                            put(pt.second)
                        }
                        pts.put(coord)
                    }
                    put("points", pts)
                })
            }
        }.toString()
    }

    val htmlContent = remember {
        generateLeafletHtml(gaugesJson, watershedsJson)
    }

    // Synchronize timeline frame changes to the Leaflet map instance
    LaunchedEffect(timelineFrameIndex, isMapLoaded) {
        if (isMapLoaded) {
            val frame = TrishuliDisasterData.TIMELINE_FRAMES.getOrNull(timelineFrameIndex) ?: return@LaunchedEffect
            val stagesObj = JSONObject()
            frame.stationStages.forEach { (k, v) -> stagesObj.put(k, v) }
            val js = "if (window.updateTimeline) { window.updateTimeline(${frame.index}, '${frame.timeLabel}', ${frame.rainfallIntensityFactor}, ${frame.cloudCoverPercent}, $stagesObj); }"
            webViewRef?.evaluateJavascript(js, null)
        }
    }

    // Synchronize Layer Toggles
    LaunchedEffect(showWatersheds, showRiverGauges, showRainfallHeatmap, showCloudCover, isMapLoaded) {
        if (isMapLoaded) {
            val js = "if (window.toggleLayers) { window.toggleLayers($showWatersheds, $showRiverGauges, $showRainfallHeatmap, $showCloudCover); }"
            webViewRef?.evaluateJavascript(js, null)
        }
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    setSupportZoom(true)
                    builtInZoomControls = false
                    displayZoomControls = false
                }
                addJavascriptInterface(bridge, "AndroidBridge")
                webChromeClient = WebChromeClient()
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isMapLoaded = true
                    }
                }
                loadDataWithBaseURL("https://leafletjs.com/", htmlContent, "text/html", "UTF-8", null)
                webViewRef = this
            }
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("leaflet_gis_webview")
    )
}

private fun generateLeafletHtml(gaugesJson: String, watershedsJson: String): String {
    return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
        body, html { margin: 0; padding: 0; height: 100%; width: 100%; background: #FFFFFF; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; overflow: hidden; }
        #map { width: 100%; height: 100%; background: #F8FAFC; }
        .custom-popup .leaflet-popup-content-wrapper { background: #FFFFFF; color: #0F172A; border: 1px solid #CBD5E1; border-radius: 10px; font-size: 12px; box-shadow: 0 4px 16px rgba(0,0,0,0.12); }
        .custom-popup .leaflet-popup-tip { background: #FFFFFF; }
        .gauge-badge { width: 22px; height: 22px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 11px; color: #FFFFFF; border: 2px solid #FFFFFF; box-shadow: 0 2px 6px rgba(0,0,0,0.25); }
        .gauge-danger { background: #DC2626; box-shadow: 0 0 10px rgba(220,38,38,0.7); animation: pulse 1.5s infinite; }
        .gauge-warning { background: #D97706; box-shadow: 0 0 8px rgba(217,119,6,0.6); }
        .gauge-normal { background: #16A34A; }
        @keyframes pulse {
            0% { transform: scale(0.95); opacity: 0.9; }
            50% { transform: scale(1.15); opacity: 1; }
            100% { transform: scale(0.95); opacity: 0.9; }
        }
        .hud-badge { position: absolute; bottom: 12px; left: 12px; z-index: 1000; background: rgba(255, 255, 255, 0.94); border: 1px solid #CBD5E1; border-radius: 8px; padding: 6px 12px; color: #0F172A; font-size: 11px; font-family: monospace; font-weight: bold; box-shadow: 0 2px 8px rgba(0,0,0,0.08); pointer-events: none; }
    </style>
</head>
<body>
    <div id="map"></div>
    <div class="hud-badge" id="hud">Bhote Koshi–Trishuli GIS Layer</div>

    <script>
        var gaugesData = $gaugesJson;
        var watershedsData = $watershedsJson;

        // Initialize Map centered on Bhote Koshi / Trishuli river corridor (Nepal)
        var map = L.map('map', {
            zoomControl: false,
            attributionControl: false
        }).setView([28.15, 85.28], 11);

        // Clean, bright sober basemap tiles (CartoDB Positron style)
        L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
            maxZoom: 18,
            subdomains: 'abcd'
        }).addTo(map);

        var watershedsLayerGroup = L.layerGroup().addTo(map);
        var gaugesLayerGroup = L.layerGroup().addTo(map);
        var heatmapLayerGroup = L.layerGroup().addTo(map);
        var cloudLayerGroup = L.layerGroup().addTo(map);

        // 1. Render Watershed Polygons
        watershedsData.forEach(function(ws) {
            var polygon = L.polygon(ws.points, {
                color: ws.color,
                fillColor: ws.color,
                fillOpacity: 0.22,
                weight: 2,
                dashArray: '4, 4'
            }).addTo(watershedsLayerGroup);

            polygon.bindPopup(
                "<div style='min-width:160px;'>" +
                "<b style='color:" + ws.color + ";'>" + ws.name + "</b><br/>" +
                "<span style='color:#94A3B8;'>" + ws.nepali + "</span><br/>" +
                "Area: " + ws.area + " km² | Elev: " + ws.elev + "m<br/>" +
                "<b>Primary Hazard:</b> " + ws.risk +
                "</div>",
                { className: 'custom-popup' }
            );

            polygon.on('click', function() {
                if (window.AndroidBridge) {
                    window.AndroidBridge.onWatershedSelected(ws.id);
                }
            });
        });

        // 2. Render River Gauge Stations
        var gaugeMarkers = {};
        gaugesData.forEach(function(g) {
            var badgeClass = g.status === 'CRITICAL_DANGER' ? 'gauge-danger' : (g.status === 'WATCH_WARNING' ? 'gauge-warning' : 'gauge-normal');
            var iconHtml = "<div class='gauge-badge " + badgeClass + "'>" + g.stage.toFixed(1) + "</div>";
            
            var customIcon = L.divIcon({
                html: iconHtml,
                className: '',
                iconSize: [22, 22],
                iconAnchor: [11, 11]
            });

            var marker = L.marker([g.lat, g.lon], { icon: customIcon }).addTo(gaugesLayerGroup);
            gaugeMarkers[g.id] = { marker: marker, data: g };

            marker.bindPopup(
                "<div style='min-width:180px;'>" +
                "<b style='color:#00E5FF;'>" + g.name + "</b><br/>" +
                "<span style='color:#94A3B8;'>" + g.river + "</span><br/><br/>" +
                "<b>Current Stage:</b> <span style='color:#FF453A; font-weight:bold;'>" + g.stage.toFixed(1) + " m</span><br/>" +
                "<b>Warning Level:</b> " + g.warning.toFixed(1) + " m | <b>Danger:</b> " + g.danger.toFixed(1) + " m<br/>" +
                "<b>Discharge:</b> " + g.discharge + " m³/s<br/>" +
                "<b>Precip (1h):</b> " + g.rainMm + " mm/h" +
                "</div>",
                { className: 'custom-popup' }
            );

            marker.on('click', function() {
                if (window.AndroidBridge) {
                    window.AndroidBridge.onGaugeSelected(g.id);
                }
            });
        });

        // 3. Render Rainfall Heatmap Zones (GPM Monsoon metrics)
        var rainCenters = [
            { lat: 28.32, lon: 85.38, intensity: 85, radius: 14000 },
            { lat: 28.26, lon: 85.36, intensity: 95, radius: 12000 },
            { lat: 28.16, lon: 85.33, intensity: 65, radius: 10000 },
            { lat: 28.05, lon: 85.24, intensity: 50, radius: 9000 }
        ];

        var rainCircles = [];
        rainCenters.forEach(function(rc) {
            var circle = L.circle([rc.lat, rc.lon], {
                radius: rc.radius,
                color: '#FF9F0A',
                fillColor: '#FF453A',
                fillOpacity: 0.35,
                weight: 1
            }).addTo(heatmapLayerGroup);
            rainCircles.push(circle);
        });

        // 4. Render Cloud Cover Infrared Overlay
        var cloudCoverPolygon = L.polygon([
            [28.40, 85.10], [28.40, 85.55],
            [28.05, 85.55], [28.05, 85.10]
        ], {
            color: '#FFFFFF',
            fillColor: '#E2E8F0',
            fillOpacity: 0.15,
            weight: 0
        }).addTo(cloudLayerGroup);

        // Timeline Update Handler called from Compose
        window.updateTimeline = function(index, label, rainFactor, cloudPct, stageMap) {
            document.getElementById('hud').innerText = "T-LAPSE: " + label + " | Cloud: " + cloudPct + "%";
            
            // Update rain heatmap intensity
            rainCircles.forEach(function(c) {
                c.setStyle({ fillOpacity: 0.15 + (rainFactor * 0.45) });
            });

            // Update cloud opacity
            cloudCoverPolygon.setStyle({ fillOpacity: (cloudPct / 100.0) * 0.25 });

            // Update gauge marker text and pulsing color
            for (var id in gaugeMarkers) {
                if (stageMap && stageMap[id] !== undefined) {
                    var newStage = stageMap[id];
                    var item = gaugeMarkers[id];
                    var isDanger = newStage >= item.data.danger;
                    var isWarning = newStage >= item.data.warning;
                    var badgeClass = isDanger ? 'gauge-danger' : (isWarning ? 'gauge-warning' : 'gauge-normal');
                    
                    var newHtml = "<div class='gauge-badge " + badgeClass + "'>" + newStage.toFixed(1) + "</div>";
                    item.marker.setIcon(L.divIcon({
                        html: newHtml,
                        className: '',
                        iconSize: [22, 22],
                        iconAnchor: [11, 11]
                    }));
                }
            }
        };

        // Layer Toggles
        window.toggleLayers = function(wsVisible, gaugeVisible, rainVisible, cloudVisible) {
            if (wsVisible) map.addLayer(watershedsLayerGroup); else map.removeLayer(watershedsLayerGroup);
            if (gaugeVisible) map.addLayer(gaugesLayerGroup); else map.removeLayer(gaugesLayerGroup);
            if (rainVisible) map.addLayer(heatmapLayerGroup); else map.removeLayer(heatmapLayerGroup);
            if (cloudVisible) map.addLayer(cloudLayerGroup); else map.removeLayer(cloudLayerGroup);
        };
    </script>
</body>
</html>
    """.trimIndent()
}
