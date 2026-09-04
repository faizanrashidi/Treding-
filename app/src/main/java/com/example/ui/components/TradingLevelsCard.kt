package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingSignal
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSubtleDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardSurface

@Composable
fun TradingLevelsCard(
    signal: TradingSignal,
    entryZone: String?,
    stopLoss: String?,
    targets: List<String>,
    riskReward: String?,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .testTag("trading_levels_card")
            .clip(cardShape)
            .background(TradeCardSurface)
            .border(1.dp, TradeCardBorderDark, cardShape)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MODEL-DERIVED LEVELS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.2.sp
                )
                if (riskReward != null) {
                    Text(
                        text = "R:R $riskReward",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignalBuyGreenLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2-Column Grid matching Professional Polish theme
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column: Entry Zone & Target 1
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ENTRY ZONE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSubtleDark,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = entryZone ?: "Wait for setup",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (targets.isNotEmpty()) "TARGET 1" else "TARGET",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSubtleDark,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = targets.firstOrNull() ?: "Undefined",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignalBuyGreenLight
                    )
                }

                // Right Column: Stop Loss & Target 2 / Risk-Reward
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "STOP LOSS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSubtleDark,
                        textAlign = TextAlign.End,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stopLoss ?: "Undefined",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignalSellRed,
                        textAlign = TextAlign.End
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (targets.size > 1) "TARGET 2" else "RISK/REWARD",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSubtleDark,
                        textAlign = TextAlign.End,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (targets.size > 1) targets[1] else (riskReward ?: "1 : 2.0"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (targets.size > 1) SignalBuyGreenLight else Color.White,
                        textAlign = TextAlign.End
                    )
                }
            }

            if (targets.size > 2) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "TARGET 3",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark
                        )
                        Text(
                            text = targets[2],
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SignalBuyGreenLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Notice: Derived mathematically from visible pivot structure & ATR. Not execution guarantee.",
                fontSize = 10.sp,
                color = TextSubtleDark,
                lineHeight = 14.sp
            )
        }
    }
}
