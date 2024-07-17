package com.spongycode.calendar.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.calendar.screen.calendar.CalendarScreen
import com.spongycode.calendar.screen.task.TaskScreen

@Composable
fun NavContainer(startDestination: String) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "calendar") {
            CalendarScreen(navController = navController)
        }
        composable(route = "task") {
            TaskScreen()
        }
    }
}