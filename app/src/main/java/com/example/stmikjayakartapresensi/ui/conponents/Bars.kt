package com.example.stmikjayakartapresensi.ui.conponents

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusAndNavBarColorBackground() {
    val isDarkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()
    
    if (isDarkTheme) {
        systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.background,darkIcons = false)
        systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.background,darkIcons = false)
    } else {
        systemUiController.setStatusBarColor(color = MaterialTheme.colorScheme.background,darkIcons = true)
        systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.background,darkIcons = true)
    }
}

@Composable
fun StatusAndNavBarColorPrimaryContainer() {
    val isDarkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    if (isDarkTheme) {
        systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.primaryContainer,darkIcons = false)
        systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.background,darkIcons = false)
    } else {
        systemUiController.setStatusBarColor(color = MaterialTheme.colorScheme.primaryContainer,darkIcons = true)
        systemUiController.setNavigationBarColor(color = MaterialTheme.colorScheme.background,darkIcons = true)
    }
}