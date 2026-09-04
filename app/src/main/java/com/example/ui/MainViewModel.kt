package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiVisionAnalyzer
import com.example.data.MarketTrend
import com.example.data.TradingAnalysisResult
import com.example.data.TradingPresets
import com.example.data.TradingScreenPreset
import com.example.data.TradingSignal
import com.example.data.db.AnalysisHistoryDao
import com.example.data.db.AnalysisHistoryEntity
import com.example.data.db.AppDatabase
import com.example.data.models.ClosedPaperTrade
import com.example.data.models.PaperPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Idle : UiState
    data class Analyzing(val stepMessage: String) : UiState
    data class ResultReady(val result: TradingAnalysisResult) : UiState
    data class Error(val message: String) : UiState
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val analyzer = GeminiVisionAnalyzer()
    private val database = AppDatabase.getDatabase(application)
    private val historyDao: AnalysisHistoryDao = database.analysisHistoryDao()

    val journalEntries: StateFlow<List<AnalysisHistoryEntity>> = historyDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Virtual Paper Trading Portfolio State
    private val _cashBalance = MutableStateFlow(1_000_000.0)
    val cashBalance: StateFlow<Double> = _cashBalance.asStateFlow()

    private val _openPositions = MutableStateFlow<List<PaperPosition>>(
        listOf(
            PaperPosition(
                symbol = "RELIANCE",
                isBuy = true,
                entryPrice = 2940.0,
                currentPrice = 2968.50,
                quantity = 150,
                stopLoss = 2910.0,
                target = 3010.0
            )
        )
    )
    val openPositions: StateFlow<List<PaperPosition>> = _openPositions.asStateFlow()

    private val _closedTrades = MutableStateFlow<List<ClosedPaperTrade>>(
        listOf(
            ClosedPaperTrade(
                symbol = "TCS",
                isBuy = true,
                entryPrice = 4380.0,
                exitPrice = 4450.0,
                quantity = 50,
                pnl = 3500.0,
                exitReason = "TARGET_1"
            ),
            ClosedPaperTrade(
                symbol = "BANKNIFTY",
                isBuy = false,
                entryPrice = 51400.0,
                exitPrice = 51200.0,
                quantity = 30,
                pnl = 6000.0,
                exitReason = "MANUAL_CLOSE"
            )
        )
    )
    val closedTrades: StateFlow<List<ClosedPaperTrade>> = _closedTrades.asStateFlow()

    private val _calculatorPrefillEntry = MutableStateFlow<Double?>(null)
    val calculatorPrefillEntry: StateFlow<Double?> = _calculatorPrefillEntry.asStateFlow()

    private val _calculatorPrefillSl = MutableStateFlow<Double?>(null)
    val calculatorPrefillSl: StateFlow<Double?> = _calculatorPrefillSl.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _currentBitmap = MutableStateFlow<Bitmap?>(null)
    val currentBitmap: StateFlow<Bitmap?> = _currentBitmap.asStateFlow()

    private val _selectedPreset = MutableStateFlow<TradingScreenPreset?>(null)
    val selectedPreset: StateFlow<TradingScreenPreset?> = _selectedPreset.asStateFlow()

    private val _isLiveCaptureActive = MutableStateFlow(false)
    val isLiveCaptureActive: StateFlow<Boolean> = _isLiveCaptureActive.asStateFlow()

    private val _marketCrossCheckEnabled = MutableStateFlow(true)
    val marketCrossCheckEnabled: StateFlow<Boolean> = _marketCrossCheckEnabled.asStateFlow()

    private val _selectedTimeframe = MutableStateFlow("5m")
    val selectedTimeframe: StateFlow<String> = _selectedTimeframe.asStateFlow()

    private val _customNote = MutableStateFlow("")
    val customNote: StateFlow<String> = _customNote.asStateFlow()

    init {
        // Pre-select the first preset (Zerodha Reliance) as default active demo chart
        selectPreset(TradingPresets.presets[0])
    }

    fun selectPreset(preset: TradingScreenPreset) {
        _selectedPreset.value = preset
        val bmp = TradingPresets.generatePresetBitmap(preset)
        _currentBitmap.value = bmp
    }

    fun setImageUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                if (bitmap != null) {
                    _currentBitmap.value = bitmap
                    _selectedPreset.value = null
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load selected screenshot: ${e.localizedMessage}")
            }
        }
    }

    fun setLiveCaptureActive(active: Boolean) {
        _isLiveCaptureActive.value = active
    }

    fun setMarketCrossCheck(enabled: Boolean) {
        _marketCrossCheckEnabled.value = enabled
    }

    fun setTimeframe(tf: String) {
        _selectedTimeframe.value = tf
    }

    fun setCustomNote(note: String) {
        _customNote.value = note
    }

    fun startAnalysis() {
        val bitmap = _currentBitmap.value
        if (bitmap == null) {
            _uiState.value = UiState.Error("Please upload a trading screenshot or select a broker preset first.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Analyzing("Scanning screen chart & parsing OCR data...")
            try {
                // If a preset was selected, we provide that asset's symbol as context for verification
                val context = _selectedPreset.value?.symbol ?: _customNote.value
                val result = analyzer.analyzeTradingScreen(
                    bitmap = bitmap,
                    customPromptContext = context,
                    marketDataCheckEnabled = _marketCrossCheckEnabled.value,
                    selectedTimeframes = listOf(_selectedTimeframe.value, "15m", "1H")
                )
                _uiState.value = UiState.ResultReady(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Analysis error: ${e.localizedMessage ?: "Unknown failure"}")
            }
        }
    }

    fun saveCurrentResultToJournal(result: TradingAnalysisResult) {
        viewModelScope.launch {
            val entity = AnalysisHistoryEntity(
                symbol = result.symbol,
                price = result.price ?: 0.0,
                timeframe = result.timeframe,
                signal = result.signal.name,
                trend = result.trend.name,
                modelScore = result.modelScore,
                dataConfidence = result.dataConfidence,
                entryZone = result.entryZone,
                stopLoss = result.stopLoss,
                targets = result.targets.joinToString(", "),
                riskReward = result.riskReward,
                reasonsSummary = result.reasons.take(2).joinToString("; "),
                timestamp = System.currentTimeMillis(),
                outcomeStatus = "PENDING"
            )
            historyDao.insert(entity)
        }
    }

    fun deleteJournalEntry(id: Long) {
        viewModelScope.launch {
            historyDao.deleteById(id)
        }
    }

    fun updateJournalOutcome(entity: AnalysisHistoryEntity, status: String) {
        viewModelScope.launch {
            historyDao.update(entity.copy(outcomeStatus = status))
        }
    }

    fun prefillCalculator(entry: Double?, sl: Double?) {
        _calculatorPrefillEntry.value = entry
        _calculatorPrefillSl.value = sl
    }

    fun openPaperPosition(
        symbol: String,
        isBuy: Boolean,
        entryPrice: Double,
        quantity: Int,
        stopLoss: Double?,
        target: Double?
    ) {
        val totalCost = entryPrice * quantity
        if (_cashBalance.value >= totalCost) {
            _cashBalance.value -= totalCost
            val newPosition = PaperPosition(
                symbol = symbol,
                isBuy = isBuy,
                entryPrice = entryPrice,
                currentPrice = entryPrice,
                quantity = quantity,
                stopLoss = stopLoss,
                target = target
            )
            _openPositions.value = _openPositions.value + newPosition
        }
    }

    fun closePaperPosition(id: String) {
        val pos = _openPositions.value.find { it.id == id } ?: return
        val proceeds = pos.currentValue + pos.pnl
        _cashBalance.value += (pos.investedAmount + pos.pnl)
        
        val closed = ClosedPaperTrade(
            symbol = pos.symbol,
            isBuy = pos.isBuy,
            entryPrice = pos.entryPrice,
            exitPrice = pos.currentPrice,
            quantity = pos.quantity,
            pnl = pos.pnl,
            exitReason = if (pos.target != null && pos.currentPrice >= pos.target) "TARGET_HIT" else "MANUAL_CLOSE"
        )
        _closedTrades.value = listOf(closed) + _closedTrades.value
        _openPositions.value = _openPositions.value.filter { it.id != id }
    }

    fun resetPortfolio() {
        _cashBalance.value = 1_000_000.0
        _openPositions.value = emptyList()
        _closedTrades.value = emptyList()
    }

    fun resetToIdle() {
        _uiState.value = UiState.Idle
    }
}
