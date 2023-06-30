package com.example.stmikjayakartapresensi.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.stmikjayakartapresensi.ui.navigation.SPLASH_ROUTE
import com.example.stmikjayakartapresensi.ui.navigation.Screen
import com.example.stmikjayakartapresensi.ui.screens.splash.SplashScreen

fun NavGraphBuilder.splashNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Splash.route,
        route = SPLASH_ROUTE
    ) {
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(navController = navController)
        }
    }
}