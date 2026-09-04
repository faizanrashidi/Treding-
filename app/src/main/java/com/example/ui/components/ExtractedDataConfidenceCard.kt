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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExtractedScreenData
import com.example.ui.theme.SignalBuyGreen
import com.example.ui.theme.SignalBuyGreenLight
import com.example.ui.theme.SignalHoldYellow
import com.example.ui.theme.SignalSellRed
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSubtleDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardSurface

@Composable
fun ExtractedDataConfidenceCard(
    extractedData: ExtractedScreenData,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .testTag("extracted_data_confidence_card")
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
                    text = "SCREEN OCR & VISION DATA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.2.sp
                )
                ConfidenceBadge(extractedData.ocrConfidenceAverage)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2-Column Grid for Primary Extracted Data
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Left Tile: Symbol
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F1522))
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "EXTRACTED SYMBOL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = extractedData.symbol,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Confidence: ${extractedData.symbolConfidence}%",
                            fontSize = 10.sp,
                            color = SignalBuyGreenLight
                        )
                    }
                }

                // Right Tile: Current Price
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F1522))
                        .border(1.dp, TradeCardBorderDark, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "CURRENT PRICE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtleDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (extractedData.price > 0) "₹%,.2f".format(extractedData.price) else "--",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Confidence: ${extractedData.priceConfidence}%",
                            fontSize = 10.sp,
                            color = SignalBuyGreenLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Additional details
            ConfidenceItemRow("Timeframe", extractedData.timeframe, extractedData.timeframeConfidence)
            if (extractedData.candlestickPattern.isNotBlank()) {
                ConfidenceItemRow("Candle Pattern", extractedData.candlestickPattern, 92)
            }
            if (extractedData.chartStructure.isNotBlank()) {
                ConfidenceItemRow("Chart Structure", extractedData.chartStructure, 90)
            }

            // Visible Indicators
            if (extractedData.indicators.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "IDENTIFIED INDICATORS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSubtleDark,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                extractedData.indicators.forEach { (indName, indValue) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(indName, fontSize = 12.sp, color = TextMutedDark)
                        Text(indValue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }

            if (extractedData.ocrConfidenceAverage < 60) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x33450A0A))
                        .border(1.dp, Color(0x33EF4444), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "⚠ Unable to reliably read screen data. Low confidence — setup disabled to NO TRADE.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignalSellRed
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfidenceItemRow(label: String, value: String, confidence: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, color = TextSubtleDark)
            Text(value, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        ConfidenceBadge(confidence)
    }
}

@Composable
fun ConfidenceBadge(confidence: Int) {
    val (color, bgColor) = when {
        confidence >= 85 -> Pair(SignalBuyGreenLight, SignalBuyGreen.copy(alpha = 0.15f))
        confidence >= 65 -> Pair(SignalHoldYellow, SignalHoldYellow.copy(alpha = 0.15f))
        else -> Pair(SignalSellRed, SignalSellRed.copy(alpha = 0.15f))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$confidence% Conf.",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
