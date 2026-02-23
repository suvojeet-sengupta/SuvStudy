package com.suvojeet.suvstudy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Subjects : Screen("subjects", "Subjects", Icons.Default.List)
    object Insights : Screen("insights", "Insights", Icons.Default.Timeline)
    object Goals : Screen("goals", "Goals", Icons.Default.Star)
}
