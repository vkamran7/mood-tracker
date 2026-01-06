package com.example.moodtracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.*
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.ui.history.HistoryScreen
import com.example.moodtracker.ui.home.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App(repo: MoodRepository) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route ?: Routes.HOME

    Scaffold(
        bottomBar = {
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
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) { HomeScreen(repo = repo) }
            composable(Routes.HISTORY) { HistoryScreen(repo = repo) }
        }
    }
}
