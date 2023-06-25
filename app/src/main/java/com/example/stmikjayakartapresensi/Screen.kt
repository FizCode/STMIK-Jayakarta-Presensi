package com.example.stmikjayakartapresensi

sealed class Screen(val route: String) {
    object SignIn: Screen("signin_screen")
    object Home: Screen("home_screen")
}