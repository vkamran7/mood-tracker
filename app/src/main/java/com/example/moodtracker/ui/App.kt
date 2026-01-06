package com.example.moodtracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.data.PreferencesManager
import com.example.moodtracker.ui.history.HistoryScreen
import com.example.moodtracker.ui.home.HomeScreen
import com.example.moodtracker.ui.settings.SettingsScreen
import com.example.moodtracker.ui.statistics.StatisticsScreen

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App(repo: MoodRepository) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route ?: Routes.HOME
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Tracker") },
                actions = {
                    if (route == Routes.SETTINGS) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    } else {
                        IconButton(onClick = { navController.navigate(Routes.SETTINGS) { launchSingleTop = true } }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (route != Routes.SETTINGS) {
                NavigationBar {
                    NavigationBarItem(
                        selected = route == Routes.HOME,
                        onClick = { navController.navigate(Routes.HOME) { launchSingleTop = true } },
                        label = { Text("Today") },
                        icon = { Text("🙂") }
                    )
                    NavigationBarItem(
                        selected = route == Routes.HISTORY,
                        onClick = { navController.navigate(Routes.HISTORY) { launchSingleTop = true } },
                        label = { Text("History") },
                        icon = { Text("🗓️") }
                    )
                    NavigationBarItem(
                        selected = route == Routes.STATISTICS,
                        onClick = { navController.navigate(Routes.STATISTICS) { launchSingleTop = true } },
                        label = { Text("Stats") },
                        icon = { Text("📊") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) { HomeScreen(repo = repo) }
            composable(Routes.HISTORY) { HistoryScreen(repo = repo) }
            composable(Routes.STATISTICS) { StatisticsScreen(repo = repo) }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    preferencesManager = preferencesManager
                )
            }
        }
    }
}
