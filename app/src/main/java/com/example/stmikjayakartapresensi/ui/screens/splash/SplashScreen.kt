package com.example.stmikjayakartapresensi.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stmikjayakartapresensi.R
import com.example.stmikjayakartapresensi.ui.navigation.AUTHENTICATION_ROUTE
import com.example.stmikjayakartapresensi.ui.navigation.HOME_ROUTE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
){
    splashViewModel.onViewLoaded()
    val lifecycleOwner = LocalLifecycleOwner.current

    splashViewModel.shouldOpenHome.observe(lifecycleOwner) {
       navController.navigate(HOME_ROUTE)
    }
    splashViewModel.shouldOpenSignIn.observe(lifecycleOwner) {
        navController.navigate(AUTHENTICATION_ROUTE)
    }

    val isDarkTheme = isSystemInDarkTheme()
    val logo = if (isDarkTheme) R.drawable.stmikjayakarta_dark else R.drawable.stmikjayakarta_light
    val configuration = LocalConfiguration.current
    val screenWith = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    Scaffold(
        content =  { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = "STMIK Jayakarta Logo",
                    modifier = Modifier
                        .size(width = screenWith, height = screenHeight).padding(16.dp)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}