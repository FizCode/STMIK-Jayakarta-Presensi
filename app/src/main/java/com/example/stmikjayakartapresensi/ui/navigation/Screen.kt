package com.example.stmikjayakartapresensi.ui.navigation

const val ROOT_ROUTE = "root"
const val SPLASH_ROUTE = "splash"
const val AUTHENTICATION_ROUTE = "authentication"
const val HOME_ROUTE = "home"
sealed class Screen(val route: String) {
    object Splash: Screen("splash_screen")
    object SignIn: Screen("signin_screen")
    object Home: Screen("home_screen")
    object ClassDetails: Screen("class_detail_screen")
}