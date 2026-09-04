package com.example.data

enum class TradingSignal(val displayName: String, val emoji: String) {
    BUY_SETUP("BUY SETUP", "🟢"),
    SELL_SETUP("SELL SETUP", "🔴"),
    HOLD("HOLD / WAIT", "🟡"),
    NO_TRADE("NO TRADE", "⚪")
}

enum class MarketTrend(val displayName: String) {
    BULLISH("BULLISH"),
    BEARISH("BEARISH"),
    NEUTRAL("NEUTRAL")
}

data class ExtractedScreenData(
    val symbol: String = "",
    val symbolConfidence: Int = 95,
    val price: Double = 0.0,
    val priceConfidence: Int = 95,
    val priceChange: String = "",
    val timeframe: String = "5m",
    val timeframeConfidence: Int = 90,
    val candlestickPattern: String = "",
    val chartStructure: String = "",
    val indicators: Map<String, String> = emptyMap(),
    val supportLevel: String? = null,
    val resistanceLevel: String? = null,
    val ocrConfidenceAverage: Int = 92
)

data class TimeframeSignal(
    val timeframe: String,
    val trend: MarketTrend,
    val signal: TradingSignal,
    val summary: String
)

data class ScoreBreakdown(
    val trendScore: Int = 85,
    val priceActionScore: Int = 80,
    val momentumScore: Int = 75,
    val volumeScore: Int = 85,
    val indicatorConfirmationScore: Int = 80,
    val supportResistanceScore: Int = 80,
    val riskRewardScore: Int = 85,
    val dataQualityScore: Int = 90
)

data class TradingAnalysisResult(
    val symbol: String,
    val price: Double?,
    val timeframe: String,
    val signal: TradingSignal,
    val trend: MarketTrend,
    val modelScore: Int, // 0 to 100
    val dataConfidence: Int, // 0 to 100
    val entryZone: String?,
    val stopLoss: String?,
    val targets: List<String>,
    val riskReward: String?,
    val reasons: List<String>,
    val risks: List<String>,
    val confirmationRequired: Boolean,
    val confirmationNote: String,
    val dataSource: List<String>,
    val analysisTimestamp: String,
    val sensitiveDataShieldActive: Boolean = true,
    val sensitiveItemsMasked: Int = 0,
    val extractedData: ExtractedScreenData? = null,
    val scoreBreakdown: ScoreBreakdown = ScoreBreakdown(),
    val timeframeBreakdown: List<TimeframeSignal> = emptyList(),
    val marketDataVerified: Boolean = false,
    val isConflict: Boolean = false
)
