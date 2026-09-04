package com.example.data.models

import java.util.UUID

data class PaperPosition(
    val id: String = UUID.randomUUID().toString(),
    val symbol: String,
    val isBuy: Boolean,
    val entryPrice: Double,
    val currentPrice: Double,
    val quantity: Int,
    val stopLoss: Double?,
    val target: Double?,
    val openTimestamp: Long = System.currentTimeMillis()
) {
    val investedAmount: Double get() = entryPrice * quantity
    val currentValue: Double get() = currentPrice * quantity
    val pnl: Double get() = if (isBuy) (currentPrice - entryPrice) * quantity else (entryPrice - currentPrice) * quantity
    val pnlPercent: Double get() = if (investedAmount > 0) (pnl / investedAmount) * 100.0 else 0.0
}

data class ClosedPaperTrade(
    val id: String = UUID.randomUUID().toString(),
    val symbol: String,
    val isBuy: Boolean,
    val entryPrice: Double,
    val exitPrice: Double,
    val quantity: Int,
    val pnl: Double,
    val exitReason: String, // "TARGET_1", "STOP_LOSS", "MANUAL_CLOSE"
    val timestamp: Long = System.currentTimeMillis()
)

data class BacktestTrade(
    val id: Int,
    val dateStr: String,
    val isBuy: Boolean,
    val entryPrice: Double,
    val exitPrice: Double,
    val returnPct: Double,
    val pnlAmount: Double,
    val outcome: String // "WIN", "LOSS"
)

data class BacktestResult(
    val strategyName: String,
    val symbol: String,
    val timeframe: String,
    val totalTrades: Int,
    val winningTrades: Int,
    val losingTrades: Int,
    val winRatePct: Double,
    val profitFactor: Double,
    val maxDrawdownPct: Double,
    val totalReturnPct: Double,
    val trades: List<BacktestTrade>
)

data class IndexQuote(
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val changePoints: Double,
    val changePercent: Double,
    val sentiment: String // "BULLISH", "BEARISH", "SIDEWAYS"
) {
    val isPositive: Boolean get() = changePoints >= 0
}

data class MarketBreadthData(
    val advances: Int,
    val declines: Int,
    val unchanged: Int,
    val advanceDeclineRatio: Double,
    val regime: String
)

data class BrokerAdapterInfo(
    val name: String,
    val brokerSlug: String,
    val appPackage: String,
    val apiName: String,
    val connectionMode: String = "Authorized Read-Only Feed",
    val latencyMs: Int = 24,
    val isConnected: Boolean = true,
    val orderExecutionAllowed: Boolean = false // Strict Safety: Always false
)

data class TradeMistakeInsight(
    val id: String,
    val category: String, // "RISK_MANAGEMENT", "PSYCHOLOGY", "EXECUTION", "TREND"
    val title: String,
    val severity: String, // "CRITICAL", "MODERATE", "OPPORTUNITY"
    val detectedCount: Int,
    val explanation: String,
    val actionableGuideline: String
)
