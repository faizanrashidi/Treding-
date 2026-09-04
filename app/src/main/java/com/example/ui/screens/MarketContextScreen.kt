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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradeAiMarketData
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
fun MarketContextScreen(
    modifier: Modifier = Modifier
) {
    val brokerConnections = remember {
        mutableStateMapOf<String, Boolean>().apply {
            TradeAiMarketData.supportedBrokers.forEach {
                put(it.brokerSlug, it.isConnected)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().background(TradeBackgroundDark),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "MARKET PULSE & INTEGRATIONS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Markets & Broker Feeds",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(TradeBackgroundDark)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Mandatory Security & Non-Execution Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NoticeCardBg)
                        .border(1.dp, NoticeCardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = PolishBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "STRICT NON-EXECUTION GUARANTEE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = PolishBluePrimary,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "TRADEAI operates strictly as an analytical decision-support tool. It NEVER automatically clicks BUY/SELL, places orders, or executes financial transactions. All trades must be manually reviewed and confirmed in your official broker terminal.",
                                fontSize = 10.5.sp,
                                color = TextSecondaryDark,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "LIVE BENCHMARK INDICES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.sp
                )
            }

            item {
                // 2x2 Grid of Index Quotes
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val quotes = TradeAiMarketData.marketQuotes
                    for (i in quotes.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (j in i until minOf(i + 2, quotes.size)) {
                                val quote = quotes[j]
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(TradeCardSurface)
                                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(14.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(quote.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSubtleDark)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("%,.2f".format(quote.currentPrice), fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (quote.isPositive) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                                                contentDescription = null,
                                                tint = if (quote.isPositive) SignalBuyGreenLight else SignalSellRed,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${if (quote.isPositive) "+" else ""}${quote.changePercent}%",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (quote.isPositive) SignalBuyGreenLight else SignalSellRed
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                // Market Breadth (Advances vs Declines)
                val breadth = TradeAiMarketData.marketBreadth
                val total = breadth.advances + breadth.declines + breadth.unchanged
                val advanceRatio = breadth.advances.toFloat() / total

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
                            Text(
                                text = "MARKET BREADTH (NSE 500)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = TextSubtleDark,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = breadth.regime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SignalBuyGreenLight
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Box(modifier = Modifier.weight(advanceRatio).fillMaxSize().background(SignalBuyGreen))
                            Box(modifier = Modifier.weight(1f - advanceRatio).fillMaxSize().background(SignalSellRed))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Advances: ${breadth.advances}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SignalBuyGreenLight)
                            Text("A/D Ratio: ${breadth.advanceDeclineRatio}", fontSize = 11.sp, color = TextSubtleDark)
                            Text("Declines: ${breadth.declines}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SignalSellRed)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "SUPPORTED BROKER ADAPTERS (OFFICIAL APIS)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.sp
                )
            }

            items(TradeAiMarketData.supportedBrokers, key = { it.brokerSlug }) { broker ->
                val isConnected = brokerConnections[broker.brokerSlug] ?: false

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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (isConnected) SignalBuyGreen else SignalSellRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = broker.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${broker.apiName} • ${broker.connectionMode}",
                                fontSize = 10.5.sp,
                                color = TextSubtleDark
                            )
                            Text(
                                text = "Ping Latency: ${broker.latencyMs} ms",
                                fontSize = 10.sp,
                                color = TradeCyan
                            )
                        }

                        Switch(
                            checked = isConnected,
                            onCheckedChange = { active ->
                                brokerConnections[broker.brokerSlug] = active
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PolishBluePrimary,
                                checkedTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
