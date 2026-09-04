package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradingAnalysisResult
import com.example.ui.MainViewModel
import com.example.ui.UiState
import com.example.ui.screens.AnalysisResultScreen
import com.example.ui.screens.AnalyzeScreen
import com.example.ui.screens.BacktestScreen
import com.example.ui.screens.LiveCaptureOverlaySheet
import com.example.ui.screens.MarketContextScreen
import com.example.ui.screens.PaperTradingScreen
import com.example.ui.screens.TradingJournalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PolishBluePrimary
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TradeBackgroundDark
import com.example.ui.theme.TradeCardBorderDark
import com.example.ui.theme.TradeCardSurface

enum class ScreenRoute(val label: String, val icon: ImageVector) {
    ANALYZE("Scan", Icons.Default.AutoGraph),
    MARKETS("Markets", Icons.AutoMirrored.Filled.ShowChart),
    PAPER_TRADE("Paper", Icons.Default.AccountBalanceWallet),
    BACKTEST("Backtest", Icons.Default.Psychology),
    JOURNAL("Journal", Icons.Default.HistoryEdu),
    RESULT("Result", Icons.Default.AutoGraph)
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentBitmap by viewModel.currentBitmap.collectAsState()
    val selectedPreset by viewModel.selectedPreset.collectAsState()
    val isLiveCaptureActive by viewModel.isLiveCaptureActive.collectAsState()
    val marketCrossCheckEnabled by viewModel.marketCrossCheckEnabled.collectAsState()
    val selectedTimeframe by viewModel.selectedTimeframe.collectAsState()
    val journalEntries by viewModel.journalEntries.collectAsState()

    val cashBalance by viewModel.cashBalance.collectAsState()
    val openPositions by viewModel.openPositions.collectAsState()
    val closedTrades by viewModel.closedTrades.collectAsState()
    val prefillEntry by viewModel.calculatorPrefillEntry.collectAsState()
    val prefillSl by viewModel.calculatorPrefillSl.collectAsState()

    var currentScreen by remember { mutableStateOf(ScreenRoute.ANALYZE) }
    var activeResult by remember { mutableStateOf<TradingAnalysisResult?>(null) }
    var showLiveCaptureSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Automatically navigate to Result screen when analysis completes
    LaunchedEffect(uiState) {
        if (uiState is UiState.ResultReady) {
            activeResult = (uiState as UiState.ResultReady).result
            currentScreen = ScreenRoute.RESULT
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(TradeBackgroundDark),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            // Hide bottom bar only during fullscreen Result view
            if (currentScreen != ScreenRoute.RESULT) {
                NavigationBar(
                    containerColor = Color(0xFF0F172A),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TradeCardBorderDark),
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        ScreenRoute.ANALYZE,
                        ScreenRoute.MARKETS,
                        ScreenRoute.PAPER_TRADE,
                        ScreenRoute.BACKTEST,
                        ScreenRoute.JOURNAL
                    )

                    navItems.forEach { route ->
                        val isSelected = currentScreen == route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentScreen = route },
                            icon = {
                                Icon(
                                    imageVector = route.icon,
                                    contentDescription = route.label,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = route.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PolishBluePrimary,
                                selectedTextColor = PolishBluePrimary,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark,
                                indicatorColor = Color(0xFF1E293B)
                            ),
                            modifier = Modifier.testTag("nav_tab_${route.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TradeBackgroundDark)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenRoute.ANALYZE -> {
                    AnalyzeScreen(
                        currentBitmap = currentBitmap,
                        selectedPreset = selectedPreset,
                        uiState = uiState,
                        marketCrossCheckEnabled = marketCrossCheckEnabled,
                        selectedTimeframe = selectedTimeframe,
                        onSelectPreset = { preset -> viewModel.selectPreset(preset) },
                        onSelectImageUri = { uri -> viewModel.setImageUri(uri) },
                        onToggleMarketCrossCheck = { enabled -> viewModel.setMarketCrossCheck(enabled) },
                        onSelectTimeframe = { tf -> viewModel.setTimeframe(tf) },
                        onStartAnalysis = { viewModel.startAnalysis() },
                        onOpenLiveCapture = { showLiveCaptureSheet = true },
                        onOpenJournal = { currentScreen = ScreenRoute.JOURNAL }
                    )
                }
                ScreenRoute.MARKETS -> {
                    MarketContextScreen()
                }
                ScreenRoute.PAPER_TRADE -> {
                    PaperTradingScreen(
                        cashBalance = cashBalance,
                        positions = openPositions,
                        closedTrades = closedTrades,
                        onOpenPosition = { sym, isBuy, entry, qty, sl, target ->
                            viewModel.openPaperPosition(sym, isBuy, entry, qty, sl, target)
                        },
                        onClosePosition = { id ->
                            viewModel.closePaperPosition(id)
                        },
                        onResetPortfolio = {
                            viewModel.resetPortfolio()
                        },
                        initialCalculatorEntry = prefillEntry,
                        initialCalculatorSl = prefillSl
                    )
                }
                ScreenRoute.BACKTEST -> {
                    BacktestScreen(journalEntries = journalEntries)
                }
                ScreenRoute.JOURNAL -> {
                    TradingJournalScreen(
                        journalEntries = journalEntries,
                        onBack = { currentScreen = ScreenRoute.ANALYZE },
                        onDeleteEntry = { id -> viewModel.deleteJournalEntry(id) },
                        onUpdateStatus = { entity, status -> viewModel.updateJournalOutcome(entity, status) }
                    )
                }
                ScreenRoute.RESULT -> {
                    activeResult?.let { result ->
                        AnalysisResultScreen(
                            result = result,
                            onBack = {
                                viewModel.resetToIdle()
                                currentScreen = ScreenRoute.ANALYZE
                            },
                            onSaveToJournal = {
                                viewModel.saveCurrentResultToJournal(result)
                            },
                            onOpenPositionSizer = { entry, sl ->
                                viewModel.prefillCalculator(entry, sl)
                                viewModel.resetToIdle()
                                currentScreen = ScreenRoute.PAPER_TRADE
                            }
                        )
                    } ?: run {
                        currentScreen = ScreenRoute.ANALYZE
                    }
                }
            }

            if (showLiveCaptureSheet) {
                LiveCaptureOverlaySheet(
                    isLiveCaptureActive = isLiveCaptureActive,
                    onToggleLiveCapture = { active -> viewModel.setLiveCaptureActive(active) },
                    onCaptureCurrentFrame = {
                        viewModel.startAnalysis()
                    },
                    onDismiss = { showLiveCaptureSheet = false }
                )
            }
        }
    }
}

