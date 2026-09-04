package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BacktestEngine
import com.example.data.TradeAiMarketData
import com.example.data.db.AnalysisHistoryEntity
import com.example.data.models.BacktestResult
import com.example.data.models.TradeMistakeInsight
import com.example.ui.theme.NoticeCardBg
import com.example.ui.theme.NoticeCardBorder
import com.example.ui.theme.PolishBluePrimary
import com.example.ui.theme.SignalBuyBorder
import com.example.ui.theme.SignalBuyContainer
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalHoldYellow
import com.example.ui.theme.SignalSellBorder
import com.example.ui.theme.SignalSellContainer
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TextSubtleDark
import com.example.ui.theme.TradeBackgroundDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardSurface
import com.example.ui.theme.TradeCyan
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BacktestScreen(
    journalEntries: List<AnalysisHistoryEntity>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Strategy Backtest, 1: AI Mistake Analyzer

    Scaffold(
        modifier = modifier.fillMaxSize().background(TradeBackgroundDark),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "QUANT & BEHAVIORAL LAB",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Backtest & Mistake Analysis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TradeBackgroundDark)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TradeBackgroundDark)
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = TradeBackgroundDark,
                contentColor = PolishBluePrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PolishBluePrimary
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("STRATEGY BACKTEST", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("MISTAKE DETECTOR", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (selectedTab) {
                0 -> StrategyBacktestView()
                1 -> AiMistakeAnalyzerView(journalEntries = journalEntries)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StrategyBacktestView() {
    var selectedStrategy by remember { mutableStateOf(BacktestEngine.availableStrategies[0]) }
    var selectedSymbol by remember { mutableStateOf(BacktestEngine.availableSymbols[0]) }
    var selectedTimeframe by remember { mutableStateOf("15m") }
    var strategyExpanded by remember { mutableStateOf(false) }
    var symbolExpanded by remember { mutableStateOf(false) }

    var isRunning by remember { mutableStateOf(false) }
    var backtestResult by remember {
        mutableStateOf<BacktestResult?>(
            BacktestEngine.runBacktest(selectedStrategy, selectedSymbol, selectedTimeframe)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Configuration Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "BACKTEST CONFIGURATION (ZERO LOOK-AHEAD BIAS)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = TextSubtleDark,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Strategy Selector Dropdown
                    ExposedDropdownMenuBox(
                        expanded = strategyExpanded,
                        onExpandedChange = { strategyExpanded = !strategyExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedStrategy,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Strategy Rule", fontSize = 11.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = strategyExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PolishBluePrimary,
                                unfocusedBorderColor = TradeCardBorderDark,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = strategyExpanded,
                            onDismissRequest = { strategyExpanded = false },
                            modifier = Modifier.background(Color(0xFF0F172A))
                        ) {
                            BacktestEngine.availableStrategies.forEach { strat ->
                                DropdownMenuItem(
                                    text = { Text(strat, color = Color.White, fontSize = 12.sp) },
                                    onClick = {
                                        selectedStrategy = strat
                                        strategyExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ExposedDropdownMenuBox(
                            expanded = symbolExpanded,
                            onExpandedChange = { symbolExpanded = !symbolExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedSymbol,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Asset", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = symbolExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PolishBluePrimary,
                                    unfocusedBorderColor = TradeCardBorderDark,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = symbolExpanded,
                                onDismissRequest = { symbolExpanded = false },
                                modifier = Modifier.background(Color(0xFF0F172A))
                            ) {
                                BacktestEngine.availableSymbols.forEach { sym ->
                                    DropdownMenuItem(
                                        text = { Text(sym, color = Color.White, fontSize = 12.sp) },
                                        onClick = {
                                            selectedSymbol = sym
                                            symbolExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Timeframe chips
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("5m", "15m", "1H").forEach { tf ->
                                val selected = selectedTimeframe == tf
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) PolishBluePrimary else Color(0xFF1E293B))
                                        .border(1.dp, if (selected) PolishBluePrimary else TradeCardBorderDark, RoundedCornerShape(8.dp))
                                        .clickable { selectedTimeframe = tf },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(tf, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            backtestResult = BacktestEngine.runBacktest(selectedStrategy, selectedSymbol, selectedTimeframe)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("run_backtest_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PolishBluePrimary)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("RUN HISTORICAL BACKTEST (60 DAYS)", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        backtestResult?.let { result ->
            item {
                // Key Performance Metrics Strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, PolishBluePrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("WIN RATE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSubtleDark)
                                Text("${result.winRatePct}%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = SignalBuyGreenLight)
                                Text("${result.winningTrades}W / ${result.losingTrades}L", fontSize = 10.sp, color = TextSecondaryDark)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("PROFIT FACTOR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSubtleDark)
                                Text("${result.profitFactor}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("Net: +${result.totalReturnPct}%", fontSize = 10.sp, color = SignalBuyGreenLight)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("MAX DRAWDOWN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSubtleDark)
                                Text("-${result.maxDrawdownPct}%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = SignalSellRed)
                                Text("${result.totalTrades} Trades", fontSize = 10.sp, color = TextSecondaryDark)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "SIMULATED EXECUTIONS LEDGER (${result.trades.size} TRADES)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.sp
                )
            }

            items(result.trades) { trade ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TradeCardSurface)
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (trade.outcome == "WIN") SignalBuyContainer else SignalSellContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        trade.outcome,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (trade.outcome == "WIN") SignalBuyGreenLight else SignalSellRed
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (trade.isBuy) "BUY" else "SELL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(trade.dateStr, fontSize = 10.5.sp, color = TextSubtleDark)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text("Entry: ₹${trade.entryPrice} → Exit: ₹${trade.exitPrice}", fontSize = 11.sp, color = TextSecondaryDark)
                        }

                        Text(
                            text = (if (trade.pnlAmount >= 0) "+₹%,.2f" else "-₹%,.2f").format(abs(trade.pnlAmount)),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = if (trade.pnlAmount >= 0) SignalBuyGreenLight else SignalSellRed
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun AiMistakeAnalyzerView(journalEntries: List<AnalysisHistoryEntity>) {
    val insights = remember(journalEntries) {
        TradeAiMarketData.evaluateJournalMistakes(journalEntries)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = PolishBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Behavioral & Mistake Auditing",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Scans journal entries for revenge trading clusters, counter-trend execution, sub-optimal risk-to-reward ratios, and emotional premature exits.",
                            fontSize = 11.sp,
                            color = TextSubtleDark,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "ACTIONABLE MISTAKE INSIGHTS (${insights.size})",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = TextSubtleDark,
                letterSpacing = 1.sp
            )
        }

        items(insights, key = { it.id }) { insight ->
            val severityColor = when (insight.severity) {
                "CRITICAL" -> SignalSellRed
                "MODERATE" -> SignalHoldYellow
                else -> PolishBluePrimary
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(severityColor.copy(alpha = 0.15f))
                                .border(1.dp, severityColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${insight.severity} • ${insight.category}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = severityColor
                            )
                        }

                        Text(
                            text = "Detected: ${insight.detectedCount}x",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = insight.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = insight.explanation,
                        fontSize = 11.5.sp,
                        color = TextSecondaryDark,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SignalBuyGreenLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = insight.actionableGuideline,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFA7F3D0),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}
