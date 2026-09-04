package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_history")
data class AnalysisHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,
    val price: Double,
    val timeframe: String,
    val signal: String, // BUY_SETUP, SELL_SETUP, HOLD, NO_TRADE
    val trend: String,
    val modelScore: Int,
    val dataConfidence: Int,
    val entryZone: String?,
    val stopLoss: String?,
    val targets: String, // comma-separated
    val riskReward: String?,
    val reasonsSummary: String,
    val timestamp: Long = System.currentTimeMillis(),
    val outcomeStatus: String = "PENDING" // PENDING, HIT_TARGET, HIT_SL, EXPIRED, CANCELLED
)
