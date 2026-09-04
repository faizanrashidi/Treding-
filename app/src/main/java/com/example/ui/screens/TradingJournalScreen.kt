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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.AnalysisHistoryEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingJournalScreen(
    journalEntries: List<AnalysisHistoryEntity>,
    onBack: () -> Unit,
    onDeleteEntry: (Long) -> Unit,
    onUpdateStatus: (AnalysisHistoryEntity, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = journalEntries.size
    val winCount = journalEntries.count { it.outcomeStatus == "HIT_TARGET" }
    val lossCount = journalEntries.count { it.outcomeStatus == "HIT_SL" }
    val evaluatedCount = winCount + lossCount
    val winRate = if (evaluatedCount > 0) (winCount * 100) / evaluatedCount else 0

    Scaffold(
        modifier = modifier.fillMaxSize().background(TradeBackgroundDark),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "VERIFIED PERFORMANCE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Trading Journal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryDark
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("journal_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
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
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            // Stats Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(TradeCardSurface)
                    .border(1.dp, TradeCardBorderDark, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(label = "TOTAL SETUPS", value = "$totalCount", color = Color.White)
                    StatItem(label = "WIN RATE", value = "$winRate%", color = SignalBuyGreenLight)
                    StatItem(label = "TARGETS HIT", value = "$winCount", color = SignalBuyGreenLight)
                    StatItem(label = "STOP LOSSES", value = "$lossCount", color = SignalSellRed)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (journalEntries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF334155),
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No saved trading setups yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Analyze a trading screen and tap 'Save Setup to Journal'",
                            fontSize = 12.sp,
                            color = TextSubtleDark
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(journalEntries, key = { it.id }) { entry ->
                        JournalEntryCard(
                            entry = entry,
                            onDelete = { onDeleteEntry(entry.id) },
                            onUpdateStatus = { status -> onUpdateStatus(entry, status) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = TextSubtleDark,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

@Composable
private fun JournalEntryCard(
    entry: AnalysisHistoryEntity,
    onDelete: () -> Unit,
    onUpdateStatus: (String) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    var showStatusDialog by remember { mutableStateOf(false) }

    val (signalBg, signalBorder, signalColor) = when (entry.signal) {
        "BUY_SETUP" -> Triple(SignalBuyContainer, SignalBuyBorder, SignalBuyGreenLight)
        "SELL_SETUP" -> Triple(SignalSellContainer, SignalSellBorder, SignalSellRed)
        "HOLD" -> Triple(SignalHoldContainer, SignalHoldBorder, SignalHoldYellow)
        else -> Triple(Color(0xFF1E293B), SignalNoTradeBorder, SignalNoTradeGrey)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.symbol,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, TradeCardBorderDark, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = entry.timeframe,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TradeCyan
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateFormat.format(Date(entry.timestamp)),
                        fontSize = 10.5.sp,
                        color = TextSubtleDark
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(26.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(signalBg)
                            .border(1.dp, signalBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = entry.signal.replace("_", " "),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = signalColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Score: ${entry.modelScore}/100",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubtleDark
                    )
                }

                // Outcome status badge (clickable to change)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (entry.outcomeStatus) {
                                "HIT_TARGET" -> SignalBuyContainer
                                "HIT_SL" -> SignalSellContainer
                                "CANCELLED" -> Color(0xFF1E293B)
                                else -> Color(0xFF0F172A)
                            }
                        )
                        .border(
                            1.dp,
                            when (entry.outcomeStatus) {
                                "HIT_TARGET" -> SignalBuyBorder
                                "HIT_SL" -> SignalSellBorder
                                else -> TradeCardBorderDark
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { showStatusDialog = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (entry.outcomeStatus) {
                            "HIT_TARGET" -> "✓ TARGET HIT"
                            "HIT_SL" -> "✕ STOP LOSS"
                            "CANCELLED" -> "CANCELLED"
                            else -> "⏳ PENDING"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = when (entry.outcomeStatus) {
                            "HIT_TARGET" -> SignalBuyGreenLight
                            "HIT_SL" -> SignalSellRed
                            "CANCELLED" -> TextSubtleDark
                            else -> TradeCyan
                        }
                    )
                }
            }

            if (entry.entryZone != null || entry.stopLoss != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0B132B).copy(alpha = 0.5f))
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Entry: ${entry.entryZone ?: "--"}",
                        fontSize = 11.sp,
                        color = TextSecondaryDark
                    )
                    Text(
                        text = "SL: ${entry.stopLoss ?: "--"}",
                        fontSize = 11.sp,
                        color = SignalSellRed
                    )
                    if (entry.riskReward != null) {
                        Text(
                            text = "R:R ${entry.riskReward}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TradeCyan
                        )
                    }
                }
            }
        }
    }

    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = {
                Text(
                    text = "Update Setup Outcome",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column {
                    Text(
                        text = "Record the actual market outcome for this trade setup to refine historical win-rate stats:",
                        color = TextSecondaryDark,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { onUpdateStatus("HIT_TARGET"); showStatusDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SignalBuyGreen,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text("Target Hit (Win)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onUpdateStatus("HIT_SL"); showStatusDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SignalSellRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text("Stop Loss Hit (Loss)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onUpdateStatus("PENDING"); showStatusDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E293B),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text("Mark as Pending", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Cancel", color = TextSubtleDark)
                }
            },
            containerColor = TradeCardSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
