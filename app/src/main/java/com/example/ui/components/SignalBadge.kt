package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingSignal
import com.example.ui.theme.SignalBuyBorder
import com.example.ui.theme.SignalBuyContainer
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalHoldBorder
import com.example.ui.theme.SignalHoldContainer
import com.example.ui.theme.SignalHoldYellow
import com.example.ui.theme.SignalNoTradeBorder
import com.example.ui.theme.SignalNoTradeContainer
import com.example.ui.theme.SignalNoTradeGrey
import com.example.ui.theme.SignalSellBorder
import com.example.ui.theme.SignalSellContainer
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun SignalBadge(
    signal: TradingSignal,
    modifier: Modifier = Modifier,
    large: Boolean = false,
    confidenceScore: Int? = null
) {
    val (primaryColor, accentColor, gradientBrush, borderColor) = when (signal) {
        TradingSignal.BUY_SETUP -> Quadruple(
            SignalBuyGreen,
            SignalBuyGreenLight,
            Brush.linearGradient(
                listOf(
                    Color(0xFF16A34A).copy(alpha = 0.20f),
                    Color(0xFF064E3B).copy(alpha = 0.10f)
                )
            ),
            SignalBuyBorder
        )
        TradingSignal.SELL_SETUP -> Quadruple(
            SignalSellRed,
            Color(0xFFFCA5A5),
            Brush.linearGradient(
                listOf(
                    Color(0xFFDC2626).copy(alpha = 0.20f),
                    Color(0xFF7F1D1D).copy(alpha = 0.10f)
                )
            ),
            SignalSellBorder
        )
        TradingSignal.HOLD -> Quadruple(
            SignalHoldYellow,
            Color(0xFFFDE047),
            Brush.linearGradient(
                listOf(
                    Color(0xFFCA8A04).copy(alpha = 0.20f),
                    Color(0xFF713F12).copy(alpha = 0.10f)
                )
            ),
            SignalHoldBorder
        )
        TradingSignal.NO_TRADE -> Quadruple(
            SignalNoTradeGrey,
            Color(0xFFCBD5E1),
            Brush.linearGradient(
                listOf(
                    Color(0xFF334155).copy(alpha = 0.30f),
                    Color(0xFF1E293B).copy(alpha = 0.20f)
                )
            ),
            SignalNoTradeBorder
        )
    }

    if (large) {
        // Professional Polish Hero Card: Gradient background, rounded-3xl, uppercase tracking tag
        val cardShape = RoundedCornerShape(24.dp)
        Box(
            modifier = modifier
                .testTag("signal_badge_${signal.name}")
                .clip(cardShape)
                .background(gradientBrush)
                .border(1.dp, borderColor, cardShape)
                .padding(vertical = 20.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MARKET OPPORTUNITY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = signal.emoji,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = signal.displayName,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                }

                if (confidenceScore != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.45f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Model Confidence Score ",
                                fontSize = 12.sp,
                                color = TextSecondaryDark
                            )
                            Text(
                                text = "$confidenceScore/100",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Compact Pill
        val shape = RoundedCornerShape(8.dp)
        Box(
            modifier = modifier
                .testTag("signal_badge_${signal.name}")
                .clip(shape)
                .background(primaryColor.copy(alpha = 0.15f))
                .border(1.dp, borderColor, shape)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = signal.emoji,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = signal.displayName,
                    color = primaryColor,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
