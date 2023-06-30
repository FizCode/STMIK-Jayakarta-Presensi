package com.example.stmikjayakartapresensi.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.stmikjayakartapresensi.ui.navigation.AUTHENTICATION_ROUTE
import com.example.stmikjayakartapresensi.ui.navigation.Screen
import com.example.stmikjayakartapresensi.ui.screens.signin.SignInScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.SignIn.route,
        route = AUTHENTICATION_ROUTE
    ) {
        composable(route = Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }
    }
}