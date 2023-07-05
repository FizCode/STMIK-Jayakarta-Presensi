package com.example.stmikjayakartapresensi.ui.screens.splash

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.stmikjayakartapresensi.ui.navigation.AUTHENTICATION_ROUTE
import com.example.stmikjayakartapresensi.ui.navigation.HOME_ROUTE

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
){
    splashViewModel.onViewLoaded()
    val lifecycleOwner = LocalLifecycleOwner.current
    var token  = "-"

    splashViewModel.shouldOpenHome.observe(lifecycleOwner) {
       navController.navigate(HOME_ROUTE)
    }
    splashViewModel.shouldOpenSignIn.observe(lifecycleOwner) {
        navController.navigate(AUTHENTICATION_ROUTE)
    }
    splashViewModel.userToken.observe(lifecycleOwner) {
        token = it
    }

    Text(text = "Splash Screen. Token: $token") // delete after done making a logo
}