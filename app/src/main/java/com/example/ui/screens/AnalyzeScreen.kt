package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingPresets
import com.example.data.TradingScreenPreset
import com.example.data.TradingSignal
import com.example.ui.UiState
import com.example.ui.components.SensitiveDataShieldBanner
import com.example.ui.theme.NoticeCardBg
import com.example.ui.theme.NoticeCardBorder
import com.example.ui.theme.PolishBluePrimary
import com.example.ui.theme.SignalBuyBorder
import com.example.ui.theme.SignalBuyContainer
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalHoldBorder
import com.example.ui.theme.SignalHoldContainer
import com.example.ui.theme.SignalHoldYellow
import com.example.ui.theme.SignalNoTradeBorder
import com.example.ui.theme.SignalNoTradeGrey
import com.example.ui.theme.SignalSellBorder
import com.example.ui.theme.SignalSellContainer
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TextSubtleDark
import com.example.ui.theme.TradeBackgroundDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardDark
import com.example.ui.theme.TradeCardSurface
import com.example.ui.theme.TradeCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeScreen(
    currentBitmap: Bitmap?,
    selectedPreset: TradingScreenPreset?,
    uiState: UiState,
    marketCrossCheckEnabled: Boolean,
    selectedTimeframe: String,
    onSelectPreset: (TradingScreenPreset) -> Unit,
    onSelectImageUri: (android.net.Uri) -> Unit,
    onToggleMarketCrossCheck: (Boolean) -> Unit,
    onSelectTimeframe: (String) -> Unit,
    onStartAnalysis: () -> Unit,
    onOpenLiveCapture: () -> Unit,
    onOpenJournal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onSelectImageUri(uri)
            }
        }
    )

    Scaffold(
        modifier = modifier.fillMaxSize().background(TradeBackgroundDark),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "TRADEAI • UNIVERSAL SCANNER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SignalBuyGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Screen Analyzer",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimaryDark
                            )
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(TradeCardSurface)
                            .border(1.dp, TradeCardBorderDark, CircleShape)
                            .clickable { onOpenJournal() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("open_journal_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Trading Journal",
                                tint = TradeCyan,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "JOURNAL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondaryDark,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TradeBackgroundDark
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TradeBackgroundDark)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            // Privacy Shield Notification
            SensitiveDataShieldBanner(
                maskedCount = 0,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Screen Preview Canvas Card
            val canvasShape = RoundedCornerShape(20.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(canvasShape)
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, canvasShape)
            ) {
                Column {
                    // Preview Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedPreset != null) {
                                "${selectedPreset.brokerName} • ${selectedPreset.symbol}"
                            } else if (currentBitmap != null) {
                                "Custom Trading Screen"
                            } else {
                                "No Trading Screen Selected"
                            },
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = selectedTimeframe,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TradeCyan
                                )
                            }
                        }
                    }

                    // Chart Image Canvas
                    if (currentBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                                .background(Color(0xFF0F172A))
                        ) {
                            Image(
                                bitmap = currentBitmap.asImageBitmap(),
                                contentDescription = "Trading Screen Chart Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )

                            // Quick overlay tag
                            if (selectedPreset != null) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xCC0B0E14))
                                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = selectedPreset.description,
                                        fontSize = 11.sp,
                                        color = TextSecondaryDark
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Select a broker test preset or upload a screenshot below",
                                fontSize = 12.sp,
                                color = TextSubtleDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row: Upload Screenshot & Live Screen Capture
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("upload_screenshot_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TradeCardSurface,
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TradeCardBorderDark)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = TradeCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "UPLOAD SHOT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = onOpenLiveCapture,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("live_capture_sheet_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TradeCardSurface,
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TradeCardBorderDark)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircleOutline,
                        contentDescription = null,
                        tint = SignalBuyGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE CAPTURE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Test Broker Presets
            Text(
                text = "BROKER SCREEN PRESETS (TEST SCENARIOS)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = TextSubtleDark,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TradingPresets.presets.forEach { preset ->
                    val isSelected = selectedPreset?.id == preset.id
                    val (badgeBg, badgeBorder, badgeText) = when (preset.expectedSignal) {
                        TradingSignal.BUY_SETUP -> Triple(SignalBuyContainer, SignalBuyBorder, SignalBuyGreenLight)
                        TradingSignal.SELL_SETUP -> Triple(SignalSellContainer, SignalSellBorder, SignalSellRed)
                        TradingSignal.HOLD -> Triple(SignalHoldContainer, SignalHoldBorder, SignalHoldYellow)
                        TradingSignal.NO_TRADE -> Triple(Color(0xFF1E293B), SignalNoTradeBorder, SignalNoTradeGrey)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFF162238) else TradeCardSurface)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) PolishBluePrimary else TradeCardBorderDark,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectPreset(preset) }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = preset.brokerName,
                                    fontSize = 10.5.sp,
                                    color = TextSubtleDark
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(badgeBg)
                                        .border(0.8.dp, badgeBorder, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = preset.expectedSignal.displayName,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = badgeText
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = preset.symbol,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "₹%,.2f".format(preset.price),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (preset.changeStr.startsWith("+")) SignalBuyGreenLight else SignalSellRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Multi-Timeframe Selector
            Text(
                text = "PRIMARY TIMEFRAME",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = TextSubtleDark,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("1m", "5m", "15m", "1H", "1D").forEach { tf ->
                    val isSelected = selectedTimeframe == tf
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) PolishBluePrimary else TradeCardSurface)
                            .border(
                                1.dp,
                                if (isSelected) PolishBluePrimary else TradeCardBorderDark,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectTimeframe(tf) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tf,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) Color.White else TextSecondaryDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options: Authorized Market Cross-Check
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Market Context Cross-Check",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Verifies visual price against live data feed to prevent stale or manipulated screenshots.",
                            fontSize = 10.5.sp,
                            color = TextSubtleDark,
                            lineHeight = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = marketCrossCheckEnabled,
                        onCheckedChange = onToggleMarketCrossCheck,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PolishBluePrimary,
                            checkedTrackColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("market_check_toggle")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Analyze CTA Button (Professional Polish Blue-600)
            val isAnalyzing = uiState is UiState.Analyzing
            Button(
                onClick = onStartAnalysis,
                enabled = !isAnalyzing && currentBitmap != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("analyze_screen_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PolishBluePrimary,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF1E293B).copy(alpha = 0.5f),
                    disabledContentColor = TextSubtleDark
                )
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = (uiState as UiState.Analyzing).stepMessage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ANALYZE TRADING SCREEN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            if (uiState is UiState.Error) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(NoticeCardBg)
                        .border(1.dp, NoticeCardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = uiState.message,
                        fontSize = 11.5.sp,
                        color = SignalSellRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
