package com.example.stmikjayakartapresensi.ui.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.stmikjayakartapresensi.ui.navigation.ROOT_ROUTE
import com.example.stmikjayakartapresensi.ui.navigation.SPLASH_ROUTE

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = SPLASH_ROUTE,
        route = ROOT_ROUTE
    ) {
        splashNavGraph(navController = navController)
        homeNavGraph(navController = navController)
        authNavGraph(navController = navController)
    }
}