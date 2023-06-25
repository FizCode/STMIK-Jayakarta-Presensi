package com.example.stmikjayakartapresensi.ui.theme.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stmikjayakartapresensi.R
import com.example.stmikjayakartapresensi.Screen
import com.example.stmikjayakartapresensi.ui.theme.conponents.EmailTextField
import com.example.stmikjayakartapresensi.ui.theme.conponents.PasswordTextField
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SignInScreen(navController: NavController) {
    val logo = painterResource(R.drawable.stmikjayakarta_logo)
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        MaterialTheme.colorScheme.background,
        darkIcons = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = logo,
                    contentDescription = "STMIK Jayakarta Logo",
                    modifier = Modifier
                        .size(188.dp)
                        .aspectRatio(1f)
                )
                Text(
                    text = "Masuk",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = "Gunakan email Jayakarta",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            EmailTextField(
                value = emailValue,
                onValueChanged = { emailValue = it },
                label = "Alamat Email",
                placeholder = "Email Jayakarta"
            )
            PasswordTextField(
                value = passwordValue,
                onValueChanged = { passwordValue = it },
                label = "Kata Sandi",
                placeholder = "Kata Sandi"
            )

            // delete this after testing
            Text(text = emailValue)
            Text(text = passwordValue)
        }

        // Sign in Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = {
                    navController.navigate(route = Screen.Home.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Masuk")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(navController = rememberNavController())
}