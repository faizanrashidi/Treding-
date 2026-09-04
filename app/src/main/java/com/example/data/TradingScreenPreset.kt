package com.example.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TradingScreenPreset(
    val id: String,
    val brokerName: String,
    val symbol: String,
    val price: Double,
    val changeStr: String,
    val timeframe: String,
    val expectedSignal: TradingSignal,
    val description: String,
    val presetAnalysis: TradingAnalysisResult
)

object TradingPresets {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val presets: List<TradingScreenPreset> by lazy {
        listOf(
            TradingScreenPreset(
                id = "zerodha_reliance",
                brokerName = "Zerodha Kite",
                symbol = "RELIANCE",
                price = 1520.50,
                changeStr = "+18.50 (+1.23%)",
                timeframe = "5m",
                expectedSignal = TradingSignal.BUY_SETUP,
                description = "Bullish Breakout above Resistance with EMA & MACD Alignment",
                presetAnalysis = TradingAnalysisResult(
                    symbol = "RELIANCE",
                    price = 1520.50,
                    timeframe = "5m",
                    signal = TradingSignal.BUY_SETUP,
                    trend = MarketTrend.BULLISH,
                    modelScore = 82,
                    dataConfidence = 96,
                    entryZone = "₹1,515–₹1,522",
                    stopLoss = "₹1,495",
                    targets = listOf("₹1,545", "₹1,570"),
                    riskReward = "1 : 2.4",
                    reasons = listOf(
                        "Higher High / Higher Low price structure confirmed",
                        "Price holding comfortably above 20 EMA and VWAP",
                        "MACD bullish histogram expansion above zero-line",
                        "RSI supportive at 64 (healthy momentum, not overbought)",
                        "Volume confirmation on breakout candle (+45% vs avg)"
                    ),
                    risks = listOf(
                        "Resistance nearby at ₹1,535 round level",
                        "Broader Nifty index nearing intraday supply zone"
                    ),
                    confirmationRequired = true,
                    confirmationNote = "Wait for current 5m candle close above ₹1,522 before triggering entry.",
                    dataSource = listOf("Screen Visuals", "OCR Extractor", "Technical Indicators"),
                    analysisTimestamp = dateFormat.format(Date()),
                    sensitiveDataShieldActive = true,
                    sensitiveItemsMasked = 0,
                    scoreBreakdown = ScoreBreakdown(
                        trendScore = 88,
                        priceActionScore = 84,
                        momentumScore = 80,
                        volumeScore = 85,
                        indicatorConfirmationScore = 82,
                        supportResistanceScore = 78,
                        riskRewardScore = 82,
                        dataQualityScore = 96
                    ),
                    extractedData = ExtractedScreenData(
                        symbol = "RELIANCE",
                        symbolConfidence = 99,
                        price = 1520.50,
                        priceConfidence = 98,
                        priceChange = "+18.50 (+1.23%)",
                        timeframe = "5m",
                        timeframeConfidence = 95,
                        candlestickPattern = "Bullish Breakout / Marubozu Extension",
                        chartStructure = "Higher High & Higher Low",
                        indicators = mapOf(
                            "EMA 20" to "₹1,508.20 (Price Above)",
                            "EMA 50" to "₹1,496.50 (Bullish Slope)",
                            "VWAP" to "₹1,511.00 (Price Above)",
                            "RSI (14)" to "64.2 (Bullish Expansion)",
                            "MACD" to "+12.4 Crossover (Signal +8.1)",
                            "Volume" to "1.42M (Above 20-period SMA)"
                        ),
                        supportLevel = "₹1,495.00",
                        resistanceLevel = "₹1,535.00",
                        ocrConfidenceAverage = 97
                    ),
                    timeframeBreakdown = listOf(
                        TimeframeSignal("5m", MarketTrend.BULLISH, TradingSignal.BUY_SETUP, "Clean breakout from morning range"),
                        TimeframeSignal("15m", MarketTrend.BULLISH, TradingSignal.BUY_SETUP, "Trend continuation with rising volume"),
                        TimeframeSignal("1H", MarketTrend.NEUTRAL, TradingSignal.HOLD, "Consolidating near major daily pivot"),
                        TimeframeSignal("1D", MarketTrend.BULLISH, TradingSignal.BUY_SETUP, "Bullish flag breakout on daily chart")
                    ),
                    marketDataVerified = true
                )
            ),
            TradingScreenPreset(
                id = "upstox_nifty",
                brokerName = "Upstox Pro",
                symbol = "NIFTY 50",
                price = 24480.00,
                changeStr = "-165.20 (-0.67%)",
                timeframe = "15m",
                expectedSignal = TradingSignal.SELL_SETUP,
                description = "Bearish Breakdown below Head & Shoulders Neckline",
                presetAnalysis = TradingAnalysisResult(
                    symbol = "NIFTY 50",
                    price = 24480.00,
                    timeframe = "15m",
                    signal = TradingSignal.SELL_SETUP,
                    trend = MarketTrend.BEARISH,
                    modelScore = 79,
                    dataConfidence = 94,
                    entryZone = "₹24,480–₹24,495",
                    stopLoss = "₹24,560",
                    targets = listOf("₹24,350", "₹24,220"),
                    riskReward = "1 : 2.1",
                    reasons = listOf(
                        "Lower High & Lower Low structural sequence established",
                        "Price rejected sharply below EMA 20 and VWAP",
                        "MACD bearish histogram printing lower values",
                        "RSI at 36 sloping downward into oversold territory",
                        "Strong bearish volume expansion on neckline breakdown"
                    ),
                    risks = listOf(
                        "Major psychological support at ₹24,300 nearby",
                        "Possibility of short-covering pullback at European open"
                    ),
                    confirmationRequired = true,
                    confirmationNote = "Wait for 15m retest and rejection of broken support at ₹24,495.",
                    dataSource = listOf("Screen Visuals", "OCR Extractor", "Technical Indicators"),
                    analysisTimestamp = dateFormat.format(Date()),
                    sensitiveDataShieldActive = true,
                    sensitiveItemsMasked = 0,
                    scoreBreakdown = ScoreBreakdown(
                        trendScore = 84,
                        priceActionScore = 82,
                        momentumScore = 78,
                        volumeScore = 80,
                        indicatorConfirmationScore = 81,
                        supportResistanceScore = 75,
                        riskRewardScore = 79,
                        dataQualityScore = 94
                    ),
                    extractedData = ExtractedScreenData(
                        symbol = "NIFTY 50",
                        symbolConfidence = 99,
                        price = 24480.00,
                        priceConfidence = 97,
                        priceChange = "-165.20 (-0.67%)",
                        timeframe = "15m",
                        timeframeConfidence = 94,
                        candlestickPattern = "Bearish Engulfing Candle",
                        chartStructure = "Lower High & Lower Low",
                        indicators = mapOf(
                            "EMA 20" to "24,545.00 (Price Below)",
                            "EMA 50" to "24,590.00 (Bearish Alignment)",
                            "VWAP" to "24,530.00 (Price Below)",
                            "RSI (14)" to "36.5 (Bearish Pressure)",
                            "MACD" to "-18.2 Crossover Below Zero",
                            "Volume" to "3.8M (High Selling Volume)"
                        ),
                        supportLevel = "₹24,350.00",
                        resistanceLevel = "₹24,560.00",
                        ocrConfidenceAverage = 95
                    ),
                    timeframeBreakdown = listOf(
                        TimeframeSignal("5m", MarketTrend.BEARISH, TradingSignal.SELL_SETUP, "Aggressive selling pressure"),
                        TimeframeSignal("15m", MarketTrend.BEARISH, TradingSignal.SELL_SETUP, "Clean neckline breakdown"),
                        TimeframeSignal("1H", MarketTrend.BEARISH, TradingSignal.SELL_SETUP, "Lower high rejection from 1H 50 EMA"),
                        TimeframeSignal("1D", MarketTrend.NEUTRAL, TradingSignal.HOLD, "Daily trend still in broad range")
                    ),
                    marketDataVerified = true
                )
            ),
            TradingScreenPreset(
                id = "angel_banknifty",
                brokerName = "Angel One",
                symbol = "BANKNIFTY",
                price = 51240.00,
                changeStr = "+42.10 (+0.08%)",
                timeframe = "1H",
                expectedSignal = TradingSignal.HOLD,
                description = "Compression & Squeeze near Resistance, Awaiting Confirmation",
                presetAnalysis = TradingAnalysisResult(
                    symbol = "BANKNIFTY",
                    price = 51240.00,
                    timeframe = "1H",
                    signal = TradingSignal.HOLD,
                    trend = MarketTrend.NEUTRAL,
                    modelScore = 62,
                    dataConfidence = 92,
                    entryZone = "Awaiting Breakout (> ₹51,450 or < ₹50,950)",
                    stopLoss = "Defined post-breakout",
                    targets = listOf("₹51,900 (Bullish Target)", "₹50,400 (Bearish Target)"),
                    riskReward = "1 : 1.8 (Projected)",
                    reasons = listOf(
                        "Bollinger Bands tightly compressed (Volatility Squeeze)",
                        "Inside bar pattern formed on the 1H timeframe",
                        "RSI neutral at 50.8, showing no clear directional bias",
                        "MACD lines converged at the zero line with low histogram"
                    ),
                    risks = listOf(
                        "Whipsaw risk inside tight 500-point consolidation zone",
                        "Impending RBI policy / macro announcement volatility"
                    ),
                    confirmationRequired = true,
                    confirmationNote = "DO NOT FOMO: Wait for decisive candle close outside 50,950–51,450 range with volume confirmation.",
                    dataSource = listOf("Screen Visuals", "OCR Extractor", "Technical Indicators"),
                    analysisTimestamp = dateFormat.format(Date()),
                    sensitiveDataShieldActive = true,
                    sensitiveItemsMasked = 0,
                    scoreBreakdown = ScoreBreakdown(
                        trendScore = 55,
                        priceActionScore = 65,
                        momentumScore = 52,
                        volumeScore = 58,
                        indicatorConfirmationScore = 60,
                        supportResistanceScore = 75,
                        riskRewardScore = 65,
                        dataQualityScore = 92
                    ),
                    extractedData = ExtractedScreenData(
                        symbol = "BANKNIFTY",
                        symbolConfidence = 98,
                        price = 51240.00,
                        priceConfidence = 96,
                        priceChange = "+42.10 (+0.08%)",
                        timeframe = "1H",
                        timeframeConfidence = 93,
                        candlestickPattern = "Inside Bar / Doji Consolidation",
                        chartStructure = "Horizontal Rectangle Range",
                        indicators = mapOf(
                            "EMA 20" to "51,210.00 (Flat)",
                            "VWAP" to "51,230.00 (Coinciding)",
                            "RSI (14)" to "50.8 (Neutral)",
                            "Bollinger Bands" to "Squeeze (Bandwidth 1.2%)",
                            "Volume" to "780K (Below Average Volume)"
                        ),
                        supportLevel = "₹50,950.00",
                        resistanceLevel = "₹51,450.00",
                        ocrConfidenceAverage = 94
                    ),
                    timeframeBreakdown = listOf(
                        TimeframeSignal("5m", MarketTrend.NEUTRAL, TradingSignal.HOLD, "Choppy consolidation"),
                        TimeframeSignal("15m", MarketTrend.NEUTRAL, TradingSignal.HOLD, "No directional follow-through"),
                        TimeframeSignal("1H", MarketTrend.NEUTRAL, TradingSignal.HOLD, "Bollinger band squeeze"),
                        TimeframeSignal("1D", MarketTrend.BULLISH, TradingSignal.HOLD, "Bullish long-term trend, but pausing")
                    ),
                    marketDataVerified = true
                )
            ),
            TradingScreenPreset(
                id = "groww_tatamotors",
                brokerName = "Groww",
                symbol = "TATA MOTORS",
                price = 985.20,
                changeStr = "-2.10 (-0.21%)",
                timeframe = "5m",
                expectedSignal = TradingSignal.NO_TRADE,
                description = "Low Confidence / Conflicting Indicators / High Noise",
                presetAnalysis = TradingAnalysisResult(
                    symbol = "TATA MOTORS",
                    price = 985.20,
                    timeframe = "5m",
                    signal = TradingSignal.NO_TRADE,
                    trend = MarketTrend.NEUTRAL,
                    modelScore = 48,
                    dataConfidence = 74,
                    entryZone = null,
                    stopLoss = null,
                    targets = emptyList(),
                    riskReward = null,
                    reasons = listOf(
                        "Conflicting indicators: Price above EMA but MACD printing divergence",
                        "Extremely low volume (thin liquidity during midday lull)",
                        "Tight resistance nearby at ₹990 with no clean clearance",
                        "Risk/Reward is weak (< 1:1.2), not meeting edge criteria"
                    ),
                    risks = listOf(
                        "Choppy price action with repeated false breakout wicks",
                        "High slippage risk due to sparse order depth"
                    ),
                    confirmationRequired = true,
                    confirmationNote = "DISCIPLINE RULE: Setup does not satisfy risk parameters. Preserving capital is priority.",
                    dataSource = listOf("Screen Visuals", "OCR Extractor"),
                    analysisTimestamp = dateFormat.format(Date()),
                    sensitiveDataShieldActive = true,
                    sensitiveItemsMasked = 1,
                    scoreBreakdown = ScoreBreakdown(
                        trendScore = 42,
                        priceActionScore = 46,
                        momentumScore = 45,
                        volumeScore = 38,
                        indicatorConfirmationScore = 44,
                        supportResistanceScore = 50,
                        riskRewardScore = 35,
                        dataQualityScore = 74
                    ),
                    extractedData = ExtractedScreenData(
                        symbol = "TATA MOTORS",
                        symbolConfidence = 91,
                        price = 985.20,
                        priceConfidence = 88,
                        priceChange = "-2.10 (-0.21%)",
                        timeframe = "5m",
                        timeframeConfidence = 82,
                        candlestickPattern = "Indecision Wick Candle",
                        chartStructure = "Choppy Sideways Drift",
                        indicators = mapOf(
                            "EMA 20" to "₹984.10 (Marginally Above)",
                            "MACD" to "Diverging (Bearish Tick)",
                            "RSI (14)" to "47.2 (Lack of Momentum)",
                            "Volume" to "120K (Drying Up)"
                        ),
                        supportLevel = "₹980.00",
                        resistanceLevel = "₹990.00",
                        ocrConfidenceAverage = 85
                    ),
                    timeframeBreakdown = listOf(
                        TimeframeSignal("5m", MarketTrend.NEUTRAL, TradingSignal.NO_TRADE, "Choppy noise"),
                        TimeframeSignal("15m", MarketTrend.NEUTRAL, TradingSignal.NO_TRADE, "Volume dried up"),
                        TimeframeSignal("1H", MarketTrend.NEUTRAL, TradingSignal.HOLD, "Inside range"),
                        TimeframeSignal("1D", MarketTrend.BULLISH, TradingSignal.HOLD, "Higher timeframe neutral-bullish")
                    ),
                    marketDataVerified = false,
                    isConflict = true
                )
            )
        )
    }

    fun generatePresetBitmap(preset: TradingScreenPreset): Bitmap {
        val width = 720
        val height = 1280
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background: Dark FinTech UI
        canvas.drawColor(Color.parseColor("#0F172A"))

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Broker Header Bar
        paint.color = Color.parseColor("#1E293B")
        canvas.drawRect(0f, 0f, width.toFloat(), 130f, paint)

        // Broker Name & Time
        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText("${preset.brokerName} • Chart v4.2", 36f, 55f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 22f
        paint.isFakeBoldText = false
        canvas.drawText("NSE EQ • INTRADAY", 36f, 95f, paint)

        // Stock Symbol & Price
        paint.color = Color.WHITE
        paint.textSize = 48f
        paint.isFakeBoldText = true
        canvas.drawText(preset.symbol, 36f, 195f, paint)

        val isBullish = preset.changeStr.startsWith("+")
        paint.color = if (isBullish) Color.parseColor("#00E676") else Color.parseColor("#FF5252")
        paint.textSize = 42f
        canvas.drawText("₹%,.2f".format(preset.price), 36f, 255f, paint)

        paint.textSize = 26f
        canvas.drawText(preset.changeStr, 340f, 255f, paint)

        // Timeframe & Indicator Tags
        paint.color = Color.parseColor("#334155")
        val tfRect = RectF(36f, 280f, 130f, 325f)
        canvas.drawRoundRect(tfRect, 8f, 8f, paint)
        paint.color = Color.parseColor("#38BDF8")
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText(preset.timeframe, 62f, 312f, paint)

        paint.color = Color.parseColor("#334155")
        val indRect = RectF(145f, 280f, 350f, 325f)
        canvas.drawRoundRect(indRect, 8f, 8f, paint)
        paint.color = Color.parseColor("#E2E8F0")
        paint.textSize = 22f
        paint.isFakeBoldText = false
        canvas.drawText("EMA(20,50) • RSI", 160f, 312f, paint)

        // Grid lines for chart
        paint.color = Color.parseColor("#1E293B")
        paint.strokeWidth = 1.5f
        for (y in 380..820 step 80) {
            canvas.drawLine(36f, y.toFloat(), (width - 36).toFloat(), y.toFloat(), paint)
        }
        for (x in 80..680 step 100) {
            canvas.drawLine(x.toFloat(), 360f, x.toFloat(), 840f, paint)
        }

        // Draw Candlesticks
        val candlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val greenColor = Color.parseColor("#00E676")
        val redColor = Color.parseColor("#FF5252")

        val candleCount = 12
        val startX = 60f
        val stepX = 52f

        for (i in 0 until candleCount) {
            val cx = startX + i * stepX
            val candleGreen = when (preset.expectedSignal) {
                TradingSignal.BUY_SETUP -> i >= 4
                TradingSignal.SELL_SETUP -> i < 4
                TradingSignal.HOLD -> i % 2 == 0
                TradingSignal.NO_TRADE -> i % 3 != 0
            }

            candlePaint.color = if (candleGreen) greenColor else redColor
            val basePriceY = when (preset.expectedSignal) {
                TradingSignal.BUY_SETUP -> 720f - (i * 24f)
                TradingSignal.SELL_SETUP -> 460f + (i * 22f)
                TradingSignal.HOLD -> 580f + (Math.sin(i.toDouble()) * 30).toFloat()
                TradingSignal.NO_TRADE -> 590f + (if (i % 2 == 0) 25f else -25f)
            }

            val bodyHeight = 25f + (i % 4) * 8f
            // Wick
            candlePaint.strokeWidth = 3f
            canvas.drawLine(cx, basePriceY - 20f, cx, basePriceY + bodyHeight + 20f, candlePaint)
            // Body
            canvas.drawRect(cx - 16f, basePriceY, cx + 16f, basePriceY + bodyHeight, candlePaint)
        }

        // Draw EMA 20 line (cyan)
        paint.color = Color.parseColor("#00F0FF")
        paint.strokeWidth = 3.5f
        var prevX = startX
        var prevY = 650f
        for (i in 1 until candleCount) {
            val curX = startX + i * stepX
            val curY = when (preset.expectedSignal) {
                TradingSignal.BUY_SETUP -> 730f - (i * 22f)
                TradingSignal.SELL_SETUP -> 440f + (i * 21f)
                TradingSignal.HOLD -> 590f
                TradingSignal.NO_TRADE -> 580f + (i % 2) * 10f
            }
            canvas.drawLine(prevX, prevY, curX, curY, paint)
            prevX = curX
            prevY = curY
        }

        // Draw Volume Bars
        paint.color = Color.parseColor("#334155")
        canvas.drawText("VOL", 36f, 880f, paint)
        for (i in 0 until candleCount) {
            val cx = startX + i * stepX
            val vHeight = 20f + (i * 8f)
            paint.color = if (i > 8) Color.parseColor("#00E676") else Color.parseColor("#475569")
            canvas.drawRect(cx - 12f, 960f - vHeight, cx + 12f, 960f, paint)
        }

        // Draw RSI & MACD Sub-panel
        paint.color = Color.parseColor("#1E293B")
        canvas.drawRect(36f, 980f, (width - 36).toFloat(), 1140f, paint)
        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 24f
        canvas.drawText("RSI(14): 64.20   MACD(12,26,9): +12.40   VWAP: ₹1,511", 54f, 1025f, paint)

        // Simulated broker footer
        paint.color = Color.parseColor("#0F172A")
        canvas.drawRect(0f, 1170f, width.toFloat(), 1280f, paint)
        paint.color = Color.parseColor("#475569")
        paint.textSize = 22f
        canvas.drawText("Order Book • Market Depth • Bid / Ask: 1520.40 / 1520.50", 36f, 1220f, paint)

        return bitmap
    }
}
