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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ClosedPaperTrade
import com.example.data.models.PaperPosition
import com.example.ui.theme.NoticeCardBg
import com.example.ui.theme.NoticeCardBorder
import com.example.ui.theme.PolishBluePrimary
import com.example.ui.theme.SignalBuyBorder
import com.example.ui.theme.SignalBuyContainer
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
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
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaperTradingScreen(
    cashBalance: Double,
    positions: List<PaperPosition>,
    closedTrades: List<ClosedPaperTrade>,
    onOpenPosition: (symbol: String, isBuy: Boolean, entryPrice: Double, qty: Int, sl: Double?, target: Double?) -> Unit,
    onClosePosition: (id: String) -> Unit,
    onResetPortfolio: () -> Unit,
    initialCalculatorEntry: Double? = null,
    initialCalculatorSl: Double? = null,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Sizing Calc, 1: Virtual Portfolio, 2: Closed Trades

    Scaffold(
        modifier = modifier.fillMaxSize().background(TradeBackgroundDark),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "RISK ENGINE & SIMULATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Paper Trading & Sizing",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryDark
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onResetPortfolio,
                        modifier = Modifier.testTag("reset_portfolio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Virtual Balance",
                            tint = TextSecondaryDark,
                            modifier = Modifier.size(20.dp)
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
            // Portfolio Quick Metric Strip
            val totalPositionValue = positions.sumOf { it.currentValue }
            val totalUnrealizedPnl = positions.sumOf { it.pnl }
            val totalEquity = cashBalance + totalPositionValue

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "VIRTUAL EQUITY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = TextSubtleDark,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "₹%,.2f".format(totalEquity),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CASH BALANCE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = TextSubtleDark,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "₹%,.2f".format(cashBalance),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryDark
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "UNREALIZED P&L",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = TextSubtleDark,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = (if (totalUnrealizedPnl >= 0) "+₹%,.2f" else "-₹%,.2f").format(abs(totalUnrealizedPnl)),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = if (totalUnrealizedPnl >= 0) SignalBuyGreenLight else SignalSellRed
                        )
                    }
                }
            }

            // Tabs: Position Sizing Calculator vs Virtual Portfolio
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
                    text = { Text("POSITION SIZER", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("OPEN POSITIONS (${positions.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("HISTORY (${closedTrades.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (selectedTab) {
                0 -> PositionSizeCalculatorView(
                    initialEntry = initialCalculatorEntry,
                    initialSl = initialCalculatorSl,
                    onExecutePaperTrade = { symbol, isBuy, entry, qty, sl, target ->
                        onOpenPosition(symbol, isBuy, entry, qty, sl, target)
                        selectedTab = 1 // Switch to active positions
                    }
                )
                1 -> OpenPositionsView(
                    positions = positions,
                    onClosePosition = onClosePosition,
                    onSwitchToCalculator = { selectedTab = 0 }
                )
                2 -> ClosedTradesHistoryView(closedTrades = closedTrades)
            }
        }
    }
}

@Composable
private fun PositionSizeCalculatorView(
    initialEntry: Double?,
    initialSl: Double?,
    onExecutePaperTrade: (symbol: String, isBuy: Boolean, entry: Double, qty: Int, sl: Double?, target: Double?) -> Unit
) {
    var symbolInput by remember { mutableStateOf("RELIANCE") }
    var capitalInput by remember { mutableStateOf("500000") }
    var riskPercentInput by remember { mutableStateOf("1.5") }
    var entryPriceInput by remember { mutableStateOf(initialEntry?.toString() ?: "2940.0") }
    var stopLossInput by remember { mutableStateOf(initialSl?.toString() ?: "2910.0") }
    var targetInput by remember { mutableStateOf("3000.0") }
    var isBuyOrder by remember { mutableStateOf(true) }

    val capital = capitalInput.toDoubleOrNull() ?: 500_000.0
    val riskPct = riskPercentInput.toDoubleOrNull() ?: 1.5
    val entryPrice = entryPriceInput.toDoubleOrNull() ?: 2940.0
    val stopLoss = stopLossInput.toDoubleOrNull() ?: 2910.0
    val target = targetInput.toDoubleOrNull() ?: 3000.0

    // Calculations
    val maxMonetaryRisk = capital * (riskPct / 100.0)
    val riskPerShare = abs(entryPrice - stopLoss)
    val suggestedQuantity = if (riskPerShare > 0) (maxMonetaryRisk / riskPerShare).toInt() else 0
    val totalInvestment = suggestedQuantity * entryPrice
    val rewardPerShare = abs(target - entryPrice)
    val riskRewardRatio = if (riskPerShare > 0) rewardPerShare / riskPerShare else 0.0
    val exceedsCapital = totalInvestment > capital

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Inputs Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RISK PARAMETERS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = TextSubtleDark,
                            letterSpacing = 1.sp
                        )

                        // Direction Selector
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, TradeCardBorderDark, RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isBuyOrder) SignalBuyContainer else Color.Transparent)
                                    .clickable { isBuyOrder = true }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "BUY / LONG",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBuyOrder) SignalBuyGreenLight else TextSubtleDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (!isBuyOrder) SignalSellContainer else Color.Transparent)
                                    .clickable { isBuyOrder = false }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "SELL / SHORT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isBuyOrder) SignalSellRed else TextSubtleDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = symbolInput,
                            onValueChange = { symbolInput = it.uppercase() },
                            label = { Text("Symbol", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = outlinedColors()
                        )
                        OutlinedTextField(
                            value = capitalInput,
                            onValueChange = { capitalInput = it },
                            label = { Text("Capital (₹)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1.2f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = outlinedColors()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = riskPercentInput,
                            onValueChange = { riskPercentInput = it },
                            label = { Text("Max Risk %", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = outlinedColors()
                        )
                        OutlinedTextField(
                            value = entryPriceInput,
                            onValueChange = { entryPriceInput = it },
                            label = { Text("Entry Price (₹)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = outlinedColors()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = stopLossInput,
                            onValueChange = { stopLossInput = it },
                            label = { Text("Stop Loss (₹)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = outlinedColors()
                        )
                        OutlinedTextField(
                            value = targetInput,
                            onValueChange = { targetInput = it },
                            label = { Text("Target (₹)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = outlinedColors()
                        )
                    }
                }
            }
        }

        item {
            // Sizing Results Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, PolishBluePrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "SUGGESTED QUANTITY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = TextSubtleDark,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "$suggestedQuantity Units",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = PolishBluePrimary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "RISK / REWARD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = TextSubtleDark,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "1 : %.2f".format(riskRewardRatio),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = if (riskRewardRatio >= 2.0) SignalBuyGreenLight else SignalSellRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E293B))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Max Loss", fontSize = 10.sp, color = TextSubtleDark)
                            Text("₹%,.2f".format(maxMonetaryRisk), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SignalSellRed)
                        }
                        Column {
                            Text("Risk / Share", fontSize = 10.sp, color = TextSubtleDark)
                            Text("₹%,.2f".format(riskPerShare), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Capital Req.", fontSize = 10.sp, color = TextSubtleDark)
                            Text("₹%,.2f".format(totalInvestment), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (exceedsCapital) SignalSellRed else Color.White)
                        }
                    }

                    if (exceedsCapital) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ Total position capital exceeds account equity. Consider leverage or lower position size.",
                            fontSize = 11.sp,
                            color = SignalSellRed
                        )
                    }
                }
            }
        }

        item {
            // Virtual Paper Execution Button
            Button(
                onClick = {
                    onExecutePaperTrade(
                        symbolInput,
                        isBuyOrder,
                        entryPrice,
                        max(1, suggestedQuantity),
                        stopLoss,
                        target
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("execute_paper_trade_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBuyOrder) SignalBuyGreen else SignalSellRed,
                    contentColor = if (isBuyOrder) Color.Black else Color.White
                )
            ) {
                Icon(
                    imageVector = if (isBuyOrder) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EXECUTE PAPER ${if (isBuyOrder) "BUY" else "SELL"} ($suggestedQuantity QTY)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun OpenPositionsView(
    positions: List<PaperPosition>,
    onClosePosition: (id: String) -> Unit,
    onSwitchToCalculator: () -> Unit
) {
    if (positions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color(0xFF334155),
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No Active Paper Positions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondaryDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Use the Position Sizer to simulate taking a trade risk-free",
                    fontSize = 12.sp,
                    color = TextSubtleDark
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onSwitchToCalculator,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PolishBluePrimary)
                ) {
                    Text("Open Position Sizer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(positions, key = { it.id }) { pos ->
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = pos.symbol,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (pos.isBuy) SignalBuyContainer else SignalSellContainer)
                                        .border(
                                            0.8.dp,
                                            if (pos.isBuy) SignalBuyBorder else SignalSellBorder,
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (pos.isBuy) "BUY" else "SELL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (pos.isBuy) SignalBuyGreenLight else SignalSellRed
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${pos.quantity} Qty",
                                    fontSize = 11.sp,
                                    color = TextSecondaryDark
                                )
                            }

                            Button(
                                onClick = { onClosePosition(pos.id) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("Close", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Entry: ₹%,.2f".format(pos.entryPrice), fontSize = 11.sp, color = TextSecondaryDark)
                                Text("LTP: ₹%,.2f".format(pos.currentPrice), fontSize = 11.sp, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = (if (pos.pnl >= 0) "+₹%,.2f" else "-₹%,.2f").format(abs(pos.pnl)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (pos.pnl >= 0) SignalBuyGreenLight else SignalSellRed
                                )
                                Text(
                                    text = "(%.2f%%)".format(pos.pnlPercent),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (pos.pnl >= 0) SignalBuyGreenLight else SignalSellRed
                                )
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun ClosedTradesHistoryView(closedTrades: List<ClosedPaperTrade>) {
    if (closedTrades.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF334155), modifier = Modifier.size(56.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("No Closed Paper Trades Yet", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextSecondaryDark)
            }
        }
    } else {
        val totalPnl = closedTrades.sumOf { it.pnl }
        val winCount = closedTrades.count { it.pnl > 0 }
        val winRate = if (closedTrades.isNotEmpty()) (winCount.toDouble() / closedTrades.size * 100).toInt() else 0

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total P&L: ₹%,.2f".format(totalPnl), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (totalPnl >= 0) SignalBuyGreenLight else SignalSellRed)
                    Text("Win Rate: $winRate%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TradeCyan)
                }
            }

            items(closedTrades, key = { it.id }) { trade ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(TradeCardSurface)
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(trade.symbol, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (trade.isBuy) "BUY" else "SELL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (trade.isBuy) SignalBuyGreenLight else SignalSellRed)
                            }
                            Text("Exit: ${trade.exitReason} @ ₹${trade.exitPrice}", fontSize = 11.sp, color = TextSubtleDark)
                        }
                        Text(
                            text = (if (trade.pnl >= 0) "+₹%,.2f" else "-₹%,.2f").format(abs(trade.pnl)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = if (trade.pnl >= 0) SignalBuyGreenLight else SignalSellRed
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun outlinedColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PolishBluePrimary,
    unfocusedBorderColor = TradeCardBorderDark,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = PolishBluePrimary,
    unfocusedLabelColor = TextSubtleDark
)
