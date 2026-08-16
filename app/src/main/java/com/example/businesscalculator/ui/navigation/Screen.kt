package com.example.businesscalculator.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object DailyEntry : Screen(
        route = "daily_entry",
        title = "Daily Entry",
        icon = Icons.Default.Calculate
    )

    object AnalyticsHistory : Screen(
        route = "analytics_history",
        title = "Analytics",
        icon = Icons.Default.BarChart
    )

    object Customization : Screen(
        route = "customization",
        title = "Settings",
        icon = Icons.Default.Settings
    )

    companion object {
        val bottomNavItems = listOf(
            DailyEntry,
            AnalyticsHistory,
            Customization
        )
    }
}
