package com.example.businesscalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.businesscalculator.data.database.BusinessDatabase
import com.example.businesscalculator.data.repository.BusinessRepository
import com.example.businesscalculator.ui.navigation.Screen
import com.example.businesscalculator.ui.screens.AnalyticsHistoryScreen
import com.example.businesscalculator.ui.screens.CustomizationScreen
import com.example.businesscalculator.ui.screens.DailyEntryScreen
import com.example.businesscalculator.ui.theme.BusinessCalculatorTheme
import com.example.businesscalculator.ui.theme.PrimaryRed
import com.example.businesscalculator.viewmodel.BusinessViewModel
import com.example.businesscalculator.viewmodel.BusinessViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: BusinessViewModel by viewModels {
        val db = BusinessDatabase.getDatabase(applicationContext)
        val repo = BusinessRepository(db.menuItemDao(), db.dailyRecordDao())
        BusinessViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCalculatorTheme {
                MainAppStructure(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppStructure(viewModel: BusinessViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                Screen.bottomNavItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(text = screen.title)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryRed,
                            selectedTextColor = PrimaryRed,
                            indicatorColor = Color(0xFFFFEBEE),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.DailyEntry.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.DailyEntry.route) {
                DailyEntryScreen(viewModel = viewModel)
            }
            composable(Screen.AnalyticsHistory.route) {
                AnalyticsHistoryScreen(viewModel = viewModel)
            }
            composable(Screen.Customization.route) {
                CustomizationScreen(viewModel = viewModel)
            }
        }
    }
}
