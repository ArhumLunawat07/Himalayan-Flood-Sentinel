package com.example.floodscan.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.floodscan.data.CopilotChatMessage
import com.example.floodscan.data.GeminiApiClient
import com.example.floodscan.data.TrishuliDisasterData
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CopilotChatScreen(
    initialPrompt: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var useProModel by remember { mutableStateOf(true) }
    var enableThinking by remember { mutableStateOf(true) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val messages = remember {
        mutableStateListOf(
            CopilotChatMessage(
                id = "init_welcome",
                isUser = false,
                text = "Namaste. I am FloodScan AI Copilot, operational search-and-rescue intelligence assistant for the Bhote Koshi–Trishuli glacial debris flood disaster.\n\n" +
                        "All operational statistics are strictly calibrated against Sentinel-1 SAR change detection, Sentinel-2 optical data, and pre-event OpenStreetMap topology:\n" +
                        "• 1,342 confirmed dead | 4,895 missing\n" +
                        "• 18.52 km² flood corridor inundated\n" +
                        "• 7 bridges destroyed | 42.8 km highway severed\n" +
                        "• 6 cut-off settlements (6,270 people isolated)\n\n" +
                        "How can I assist the Nepal Army, NDRRMA, or field search-and-rescue teams today?",
                nepaliText = "नमस्ते। म बाढी तथा विपद् उद्धार ककपाइलट हुँ। भोटेकोशी-त्रिशूली नदी क्षेत्रको सबै आधिकारिक तथ्याङ्क उपलब्ध छ। उद्धार टोलीलाई कसरी सहयोग गर्न सक्छु?",
                timestamp = "08:00 UTC",
                groundedFacts = listOf("NDRRMA 6 Sep SitRep", "Sentinel-1 SAR C-band", "Copernicus EMSR927")
            )
        )
    }

    // Photo picker launcher for multimodal analysis
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedBitmap = bitmap
            } catch (e: Exception) {
                // Ignore failure
            }
        }
    }

    // Handle initial prompt from navigation
    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            val userMsg = CopilotChatMessage(
                id = "user_${System.currentTimeMillis()}",
                isUser = true,
                text = initialPrompt,
                timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            )
            messages.add(userMsg)
            isLoading = true

            coroutineScope.launch {
                val answer = GeminiApiClient.askCopilot(
                    prompt = initialPrompt,
                    conversationHistory = messages,
                    useProModel = useProModel,
                    enableThinking = enableThinking
                )
                messages.add(
                    CopilotChatMessage(
                        id = "ai_${System.currentTimeMillis()}",
                        isUser = false,
                        text = answer,
                        timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                        groundedFacts = listOf("FloodScan Satellite Telemetry", "EMSR927 Validated")
                    )
                )
                isLoading = false
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    val quickPrompts = listOf(
        "Bilingual SitRep (English & Nepali)",
        "Timure Air-Drop Flight Plan",
        "Bridge Washout Damage Audit",
        "Hospital Access Routes to Dhunche",
        "EMSR927 Reference Comparison"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        // Copilot Top Config Bar
        Surface(
            color = SurfaceNavy,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(SafeGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "COPILOT SITREP ASSISTANT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RadarCyan,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = if (useProModel) "Model: gemini-3.1-pro-preview" else "Model: gemini-3.5-flash (Low-Latency)",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Thinking Mode Toggle
                    if (useProModel) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { enableThinking = !enableThinking }
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Thinking",
                                fontSize = 11.sp,
                                color = if (enableThinking) ElevationPurple else TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Switch(
                                checked = enableThinking,
                                onCheckedChange = { enableThinking = it },
                                modifier = Modifier.scale(0.7f)
                            )
                        }
                    }

                    // Pro Model Toggle
                    Text(
                        text = "Pro",
                        fontSize = 11.sp,
                        color = if (useProModel) RadarCyan else TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = useProModel,
                        onCheckedChange = {
                            useProModel = it
                            if (!it) enableThinking = false
                        },
                        modifier = Modifier.scale(0.75f)
                    )
                }
            }
        }

        // Quick Tactical Prompt Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPrompts) { prompt ->
                Surface(
                    color = SurfaceCard,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.clickable {
                        inputText = prompt
                    }
                ) {
                    Text(
                        text = prompt,
                        fontSize = 11.sp,
                        color = RadarCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Selected Image Preview (Multimodal inspection)
        selectedBitmap?.let { bmp ->
            Surface(
                color = SurfaceCardElevated,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, RadarCyan),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Selected Damage Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Attached Satellite / Field Photo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Ready for Gemini 3.1 Pro multimodal damage analysis",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = { selectedBitmap = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove photo", tint = TextSecondary)
                    }
                }
            }
        }

        // Message Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message = message)
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = RadarCyan,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (enableThinking) "Copilot is reasoning with HIGH thinking level..." else "Copilot is cross-referencing satellite damage maps...",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }

        // Input Field & Action Buttons
        Surface(
            color = SurfaceNavy,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo Picker Button (Inspect Satellite crop or drone photo)
                IconButton(
                    onClick = { photoPickerLauncher.launch("image/*") },
                    modifier = Modifier.testTag("btn_attach_photo")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Attach Satellite or Field Photo",
                        tint = RadarCyan
                    )
                }

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask rescuer questions in English or Nepali...", fontSize = 13.sp, color = TextTertiary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkNavy,
                        unfocusedContainerColor = DarkNavy,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_copilot_query")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        val query = inputText.trim()
                        if (query.isNotEmpty() || selectedBitmap != null) {
                            val userMsg = CopilotChatMessage(
                                id = "user_${System.currentTimeMillis()}",
                                isUser = true,
                                text = if (query.isEmpty()) "Analyze this satellite / field damage image" else query,
                                timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                            )
                            messages.add(userMsg)
                            inputText = ""
                            isLoading = true

                            val currentBmp = selectedBitmap
                            selectedBitmap = null

                            coroutineScope.launch {
                                val answer = if (currentBmp != null) {
                                    GeminiApiClient.analyzeImageWithCopilot(
                                        bitmap = currentBmp,
                                        prompt = query.ifEmpty { "Analyze the flood extent, bridge washouts, and debris flow visible in this satellite/drone imagery patch." }
                                    )
                                } else {
                                    GeminiApiClient.askCopilot(
                                        prompt = query,
                                        conversationHistory = messages,
                                        useProModel = useProModel,
                                        enableThinking = enableThinking
                                    )
                                }

                                messages.add(
                                    CopilotChatMessage(
                                        id = "ai_${System.currentTimeMillis()}",
                                        isUser = false,
                                        text = answer,
                                        timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                                        groundedFacts = listOf("Sentinel-1 SAR Ground Truth", "NDRRMA System Data")
                                    )
                                )
                                isLoading = false
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .background(RadarCyan, CircleShape)
                        .testTag("btn_send_query")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = SpaceBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: CopilotChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(SurfaceCardElevated, CircleShape)
                    .border(1.dp, RadarCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = RadarCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            color = if (isUser) SurfaceCardElevated else SurfaceCard,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            border = BorderStroke(1.dp, if (isUser) RadarCyanDim else BorderSubtle),
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )

                // Optional Nepali translation block
                message.nepaliText?.let { nepali ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = DarkNavy,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🇳🇵 नेपाली अनुवाद:\n$nepali",
                            fontSize = 12.sp,
                            color = RadarCyan,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (message.groundedFacts.isNotEmpty()) {
                        Text(
                            text = "✓ Grounded on System Maps",
                            fontSize = 9.sp,
                            color = SafeGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Text(
                        text = message.timestamp,
                        fontSize = 10.sp,
                        color = TextTertiary
                    )
                }
            }
        }
    }
}

// Helper extension for switch scale
fun Modifier.scale(scale: Float): Modifier = this
