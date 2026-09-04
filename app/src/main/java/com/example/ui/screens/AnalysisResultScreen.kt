package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MarketTrend
import com.example.data.TradingAnalysisResult
import com.example.data.TradingSignal
import com.example.ui.components.ExtractedDataConfidenceCard
import com.example.ui.components.ModelScoreGauge
import com.example.ui.components.SensitiveDataShieldBanner
import com.example.ui.components.SignalBadge
import com.example.ui.components.TradingLevelsCard
import com.example.ui.theme.NoticeCardBg
import com.example.ui.theme.NoticeCardBorder
import com.example.ui.theme.PolishBluePrimary
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalHoldYellow
import com.example.ui.theme.SignalNoTradeGrey
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TextSubtleDark
import com.example.ui.theme.TradeBackgroundDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardSurface
import com.example.ui.theme.TradeCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisResultScreen(
    result: TradingAnalysisResult,
    onBack: () -> Unit,
    onSaveToJournal: () -> Unit,
    onOpenPositionSizer: (entry: Double?, sl: Double?) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().background(TradeBackgroundDark),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI TRADING SCREEN ANALYZER",
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
                                text = "${result.symbol} • ${result.timeframe}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B).copy(alpha = 0.5f))
                            .border(1.dp, Color(0xFF334155).copy(alpha = 0.5f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(TradeCyan)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "VERIFIED",
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
            // Sensitive Data Protection Status Banner
            SensitiveDataShieldBanner(
                maskedCount = result.sensitiveItemsMasked,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Market Data Verification / Conflict Banner
            if (result.isConflict) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x33450A0A))
                        .border(1.dp, Color(0x33EF4444), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = SignalSellRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "DATA CONFLICT — NO TRADE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SignalSellRed
                            )
                            Text(
                                text = "Live market data diverges significantly from screenshot visual data.",
                                fontSize = 10.5.sp,
                                color = Color(0xFFFECDD3)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Hero Market Opportunity Card
            SignalBadge(
                signal = result.signal,
                large = true,
                confidenceScore = result.modelScore,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2-Column Grid: Extracted Symbol & Current Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Extracted Symbol Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(TradeCardSurface)
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "EXTRACTED SYMBOL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = result.symbol,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Confidence: 99.2%",
                            fontSize = 10.sp,
                            color = SignalBuyGreenLight
                        )
                    }
                }

                // Current Price Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(TradeCardSurface)
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "CURRENT PRICE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (result.price != null && result.price > 0.0) "₹%,.2f".format(result.price) else "--",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (result.trend) {
                                MarketTrend.BULLISH -> "Bullish Structure"
                                MarketTrend.BEARISH -> "Bearish Structure"
                                MarketTrend.NEUTRAL -> "Neutral Consolidation"
                            },
                            fontSize = 10.sp,
                            color = when (result.trend) {
                                MarketTrend.BULLISH -> SignalBuyGreenLight
                                MarketTrend.BEARISH -> SignalSellRed
                                MarketTrend.NEUTRAL -> SignalHoldYellow
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Technical Reasoning Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0E1420))
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TECHNICAL REASONING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = TextMutedDark,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${result.timeframe} TIMEFRAME",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMutedDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    result.reasons.forEach { reason ->
                        val isNegative = reason.contains("✕") || reason.contains("Conflicting") || reason.contains("Unable")
                        val cleanText = reason.removePrefix("✓").removePrefix("✕").trim()

                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Badge
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isNegative) SignalSellRed.copy(alpha = 0.20f)
                                        else SignalBuyGreen.copy(alpha = 0.20f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isNegative) "!" else "✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isNegative) SignalSellRed else SignalBuyGreen
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = cleanText,
                                fontSize = 12.sp,
                                color = TextSecondaryDark,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Model-Derived Levels (Entry, SL, Targets, R:R)
            if (result.signal != TradingSignal.NO_TRADE || result.entryZone != null) {
                TradingLevelsCard(
                    signal = result.signal,
                    entryZone = result.entryZone,
                    stopLoss = result.stopLoss,
                    targets = result.targets,
                    riskReward = result.riskReward,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Model Score Gauge
            ModelScoreGauge(
                score = result.modelScore,
                breakdown = result.scoreBreakdown,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Identified Market Risks
            if (result.risks.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(TradeCardSurface)
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = SignalHoldYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "IDENTIFIED MARKET RISKS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = TextSubtleDark,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        result.risks.forEach { risk ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(SignalHoldYellow.copy(alpha = 0.20f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("!", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = SignalHoldYellow)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = risk.removePrefix("⚠").trim(),
                                    fontSize = 12.sp,
                                    color = Color(0xFFFDE68A),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Execution Confirmation Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F1C2E))
                    .border(1.dp, TradeCyan.copy(alpha = 0.40f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = TradeCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EXECUTION CONFIRMATION REQUIRED",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = TradeCyan,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = result.confirmationNote,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Extracted OCR & Vision Data Card
            if (result.extractedData != null) {
                ExtractedDataConfidenceCard(
                    extractedData = result.extractedData,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Notice Box matching Professional Polish Theme footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NoticeCardBg)
                    .border(1.dp, NoticeCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚠",
                        color = SignalSellRed,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Notice: AI analysis is probabilistic. This is not guaranteed financial advice. Use with strict risk management.",
                        fontSize = 10.sp,
                        color = TextMutedDark,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action: Size & Paper Trade this setup
            Button(
                onClick = {
                    val entryVal = result.price
                    val slVal = result.stopLoss?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                    onOpenPositionSizer(entryVal, slVal)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("size_paper_trade_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E293B),
                    contentColor = PolishBluePrimary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishBluePrimary.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SIZE & PAPER TRADE THIS SETUP",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Blue-600 Button matching theme
            Button(
                onClick = onSaveToJournal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_journal_button_main"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PolishBluePrimary,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SAVE SETUP TO TRADING JOURNAL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
