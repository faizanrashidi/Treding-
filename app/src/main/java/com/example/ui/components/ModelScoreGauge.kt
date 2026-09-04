package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScoreBreakdown
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalHoldYellow
import com.example.ui.theme.SignalNoTradeGrey
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSubtleDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardSurface

@Composable
fun ModelScoreGauge(
    score: Int,
    breakdown: ScoreBreakdown = ScoreBreakdown(),
    modifier: Modifier = Modifier,
    showBreakdown: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "score_gauge"
    )

    val scoreColor = when {
        score >= 75 -> SignalBuyGreenLight
        score in 55..74 -> SignalHoldYellow
        score in 40..54 -> SignalSellRed
        else -> SignalNoTradeGrey
    }

    val scoreLabel = when {
        score >= 80 -> "Strong Bullish Setup"
        score in 70..79 -> "Moderate Bullish Setup"
        score in 55..69 -> "Neutral / Indecisive Setup"
        score in 40..54 -> "Weak / Incomplete Setup"
        else -> "High Risk / Low Confidence"
    }

    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .testTag("model_score_gauge")
            .clip(cardShape)
            .background(TradeCardSurface)
            .border(1.dp, TradeCardBorderDark, cardShape)
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MODEL SCORE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Heuristic Probability Index",
                    fontSize = 10.sp,
                    color = TextSubtleDark
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Circular Score Dial
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                Canvas(modifier = Modifier.size(120.dp)) {
                    val strokeWidth = 10.dp.toPx()
                    // Background track
                    drawArc(
                        color = Color(0xFF1E293B),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    // Score arc
                    drawArc(
                        color = scoreColor,
                        startAngle = 135f,
                        sweepAngle = 270f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "/ 100",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMutedDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = scoreLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )

            Text(
                text = "Notice: Heuristic scoring, not guaranteed future market outcome.",
                fontSize = 10.sp,
                color = TextSubtleDark,
                modifier = Modifier.padding(top = 2.dp)
            )

            if (showBreakdown) {
                Spacer(modifier = Modifier.height(14.dp))
                ScoreFactorBar("Trend Alignment", breakdown.trendScore)
                ScoreFactorBar("Price Action & Candlesticks", breakdown.priceActionScore)
                ScoreFactorBar("Momentum (RSI / MACD)", breakdown.momentumScore)
                ScoreFactorBar("Volume Confirmation", breakdown.volumeScore)
                ScoreFactorBar("Support / Resistance Alignment", breakdown.supportResistanceScore)
                ScoreFactorBar("Risk / Reward Profile", breakdown.riskRewardScore)
                ScoreFactorBar("Screen Data Quality", breakdown.dataQualityScore)
            }
        }
    }
}

@Composable
private fun ScoreFactorBar(label: String, value: Int) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 10.5.sp, color = TextMutedDark)
            Text("$value%", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(2.5.dp))
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.5.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = if (value >= 75) SignalBuyGreen else if (value >= 50) SignalHoldYellow else SignalSellRed,
            trackColor = Color(0xFF1E293B),
        )
    }
}
