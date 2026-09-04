package com.example.data

import com.example.data.db.AnalysisHistoryEntity
import com.example.data.models.BrokerAdapterInfo
import com.example.data.models.IndexQuote
import com.example.data.models.TradeMistakeInsight

object TradeAiMarketData {

    val marketQuotes = listOf(
        IndexQuote(
            name = "NIFTY 50",
            symbol = "NIFTY",
            currentPrice = 24862.40,
            changePoints = 142.15,
            changePercent = 0.58,
            sentiment = "BULLISH"
        ),
        IndexQuote(
            name = "BANK NIFTY",
            symbol = "BANKNIFTY",
            currentPrice = 51380.90,
            changePoints = -86.50,
            changePercent = -0.17,
            sentiment = "SIDEWAYS"
        ),
        IndexQuote(
            name = "INDIA VIX",
            symbol = "INDIAVIX",
            currentPrice = 13.42,
            changePoints = -0.68,
            changePercent = -4.82,
            sentiment = "NORMAL_VOLATILITY"
        ),
        IndexQuote(
            name = "NIFTY IT",
            symbol = "NIFTYIT",
            currentPrice = 41920.10,
            changePoints = 310.40,
            changePercent = 0.75,
            sentiment = "BULLISH"
        )
    )

    val marketBreadth = com.example.data.models.MarketBreadthData(
        advances = 1420,
        declines = 980,
        unchanged = 110,
        advanceDeclineRatio = 1.45,
        regime = "Moderate Bullish Breadth"
    )

    val supportedBrokers = listOf(
        BrokerAdapterInfo(
            name = "Zerodha Kite Connect",
            brokerSlug = "zerodha",
            appPackage = "com.zerodha.kite3",
            apiName = "Kite Connect v3 REST API",
            connectionMode = "Authorized Read-Only Feed",
            latencyMs = 28,
            isConnected = true
        ),
        BrokerAdapterInfo(
            name = "Upstox Pro",
            brokerSlug = "upstox",
            appPackage = "in.upstox.app",
            apiName = "Upstox Interactive API v2",
            connectionMode = "Authorized Read-Only Feed",
            latencyMs = 32,
            isConnected = true
        ),
        BrokerAdapterInfo(
            name = "Angel One",
            brokerSlug = "angelone",
            appPackage = "com.msf.angelmobile",
            apiName = "SmartAPI Publisher API",
            connectionMode = "Authorized Read-Only Feed",
            latencyMs = 35,
            isConnected = true
        ),
        BrokerAdapterInfo(
            name = "Groww",
            brokerSlug = "groww",
            appPackage = "com.nextbillion.groww",
            apiName = "Groww Authorized Market Feed",
            connectionMode = "Authorized Read-Only Feed",
            latencyMs = 40,
            isConnected = true
        ),
        BrokerAdapterInfo(
            name = "ICICI Direct",
            brokerSlug = "icici",
            appPackage = "com.icicidirect",
            apiName = "Breeze API v1.2",
            connectionMode = "Authorized Read-Only Feed",
            latencyMs = 44,
            isConnected = false
        ),
        BrokerAdapterInfo(
            name = "5paisa",
            brokerSlug = "5paisa",
            appPackage = "com.fivepaisa.trade",
            apiName = "Developer Open API v2",
            connectionMode = "Authorized Read-Only Feed",
            latencyMs = 48,
            isConnected = false
        )
    )

    fun evaluateJournalMistakes(entries: List<AnalysisHistoryEntity>): List<TradeMistakeInsight> {
        val insights = mutableListOf<TradeMistakeInsight>()

        val lossEntries = entries.filter { it.outcomeStatus == "HIT_SL" }
        val targetEntries = entries.filter { it.outcomeStatus == "HIT_TARGET" }
        val totalDecided = lossEntries.size + targetEntries.size

        // 1. Poor Risk-to-Reward Ratio analysis
        val poorRrCount = entries.count { entry ->
            val rr = entry.riskReward
            if (rr != null && rr.contains(":")) {
                val ratio = rr.split(":").lastOrNull()?.toDoubleOrNull() ?: 2.0
                ratio < 1.5
            } else false
        }
        if (poorRrCount > 0) {
            insights.add(
                TradeMistakeInsight(
                    id = "rr_ratio",
                    category = "RISK_MANAGEMENT",
                    title = "Sub-optimal Risk:Reward Setups (< 1:1.5)",
                    severity = "CRITICAL",
                    detectedCount = poorRrCount,
                    explanation = "$poorRrCount recorded setup(s) have a projected risk-to-reward ratio lower than 1:1.5. In trading mathematics, low R:R requires an unsustainably high win-rate (>67%) just to break even.",
                    actionableGuideline = "Only execute trades when target distance is at least 2x stop-loss distance (R:R >= 1:2.0). Automatically ignore lower R:R setups."
                )
            )
        }

        // 2. Counter-Trend trading
        val counterTrendCount = entries.count { entry ->
            (entry.trend == "BEARISH" && entry.signal == "BUY_SETUP") ||
            (entry.trend == "BULLISH" && entry.signal == "SELL_SETUP")
        }
        if (counterTrendCount > 0) {
            insights.add(
                TradeMistakeInsight(
                    id = "counter_trend",
                    category = "TREND",
                    title = "Counter-Trend Execution Attempt",
                    severity = "MODERATE",
                    detectedCount = counterTrendCount,
                    explanation = "Identified $counterTrendCount setups where the trade direction opposed the higher-timeframe dominant trend. Fighting structural momentum leads to sharp stop-outs.",
                    actionableGuideline = "Align intraday entries with the 1H/1D EMA 50 trend. Wait for pullback confirmation rather than predicting tops/bottoms."
                )
            )
        }

        // 3. Fast Overtrading Clustering Check (entries logged within 5 minutes of each other)
        var rapidTrades = 0
        val sortedEntries = entries.sortedBy { it.timestamp }
        for (i in 1 until sortedEntries.size) {
            val delta = sortedEntries[i].timestamp - sortedEntries[i - 1].timestamp
            if (delta < 300_000) { // < 5 minutes
                rapidTrades++
            }
        }
        if (rapidTrades > 0) {
            insights.add(
                TradeMistakeInsight(
                    id = "overtrading",
                    category = "PSYCHOLOGY",
                    title = "Clustered Rapid Entries (Possible Revenge Trading)",
                    severity = "CRITICAL",
                    detectedCount = rapidTrades,
                    explanation = "Detected $rapidTrades trades logged within 5 minutes of previous setups. Rapid-fire trade entry after a loss is a primary symptom of emotional revenge trading.",
                    actionableGuideline = "Implement a mandatory 15-minute cool-down rule after any stop-loss before re-scanning charts."
                )
            )
        }

        // Standard best-practice insights when journal is small
        if (insights.isEmpty()) {
            insights.add(
                TradeMistakeInsight(
                    id = "default_sl_rule",
                    category = "RISK_MANAGEMENT",
                    title = "Dynamic ATR Stop-Loss Discipline",
                    severity = "OPPORTUNITY",
                    detectedCount = entries.size,
                    explanation = "Always anchor your stop-loss beneath the swing pivot plus 1.5x ATR rather than a round percentage number.",
                    actionableGuideline = "Maintain strict discipline by pre-defining exit levels before opening any broker position."
                )
            )
            insights.add(
                TradeMistakeInsight(
                    id = "default_wait_rule",
                    category = "EXECUTION",
                    title = "Respecting NO_TRADE Decisions",
                    severity = "OPPORTUNITY",
                    detectedCount = entries.count { it.signal == "NO_TRADE" },
                    explanation = "Preserving capital during choppy, low-confidence or range-bound markets is 50% of professional edge.",
                    actionableGuideline = "When TRADEAI returns NO TRADE due to OCR confidence or timeframe conflict, stay in cash."
                )
            )
        }

        return insights
    }
}
