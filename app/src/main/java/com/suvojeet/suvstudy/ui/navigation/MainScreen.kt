package com.suvojeet.suvstudy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.suvojeet.suvstudy.ui.screens.GoalsScreen
import com.suvojeet.suvstudy.ui.screens.HomeScreen
import com.suvojeet.suvstudy.ui.screens.InsightsScreen
import com.suvojeet.suvstudy.ui.screens.SubjectsScreen

import com.suvojeet.suvstudy.ui.screens.SettingsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    val items = listOf(
        Screen.Home,
        Screen.Subjects,
        Screen.Insights,
        Screen.Goals
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(
                    onNavigateToSettings = { navController.navigate("settings") },
                    onNavigateToCalendar = { navController.navigate("calendar") }
                ) 
            }
            composable(Screen.Subjects.route) { SubjectsScreen() }
            composable(Screen.Insights.route) { InsightsScreen() }
            composable(Screen.Goals.route) { GoalsScreen() }
            composable("settings") { 
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                ) 
            }
            composable("calendar") {
                com.suvojeet.suvstudy.ui.screens.CalendarScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
