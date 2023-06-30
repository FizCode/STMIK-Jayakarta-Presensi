package com.example.stmikjayakartapresensi.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.stmikjayakartapresensi.ui.navigation.HOME_ROUTE
import com.example.stmikjayakartapresensi.ui.navigation.Screen
import com.example.stmikjayakartapresensi.ui.screens.classdetails.ClassDetailsScreen
import com.example.stmikjayakartapresensi.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Home.route,
        route = HOME_ROUTE
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.ClassDetails.route
        ) {
            ClassDetailsScreen(navController = navController)
        }
    }
}