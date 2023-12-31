package com.example.stmikjayakartapresensi.ui.screens.signin

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stmikjayakartapresensi.MainActivity
import com.example.stmikjayakartapresensi.R
import com.example.stmikjayakartapresensi.ui.conponents.EmailTextField
import com.example.stmikjayakartapresensi.ui.conponents.PasswordTextField
import com.example.stmikjayakartapresensi.ui.conponents.StatusAndNavBarColorBackground
import com.example.stmikjayakartapresensi.ui.navigation.HOME_ROUTE
import kotlin.system.exitProcess

@Composable
fun SignInScreen(navController: NavController, signInViewModel: SignInViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isDarkTheme = isSystemInDarkTheme()
    val logo = if (isDarkTheme) R.drawable.stmikjayakarta_dark else R.drawable.stmikjayakarta_light


    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }


    // BindViewModels
    signInViewModel.onChangeEmail(emailValue)
    signInViewModel.onChangePassword(passwordValue)

    val loading = signInViewModel.shouldShowLoading.value
    val showError = signInViewModel.shouldShowError.value
    val errorMessage = signInViewModel.errorMessage.value

    // Sign in handler
    signInViewModel.shouldOpenHomePage.observe(lifecycleOwner) {
        navController.navigate(route = HOME_ROUTE)
    }

    // User Interface
    StatusAndNavBarColorBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Loading & error handler
            if (loading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            if (showError) Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

            // Jayakarta Icon
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = "STMIK Jayakarta Logo",
                    modifier = Modifier
                        .size(188.dp)
                        .aspectRatio(1f)
                )

                // Welcome Text
                Text(
                    text = "Masuk",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = "Gunakan email Jayakarta",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            // Email TextField
            EmailTextField(
                value = emailValue,
                onValueChanged = { emailValue = it },
                label = "Alamat Email",
                placeholder = "Email Jayakarta"
            )

            // Password Textfield
            PasswordTextField(
                value = passwordValue,
                onValueChanged = { passwordValue = it },
                label = "Kata Sandi",
                placeholder = "Kata Sandi"
            )
        }

        // Sign in Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = {
                    signInViewModel.onClickSignIn()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Masuk")
            }
        }
    }

    BackHandler(enabled = true, onBack = {
        val activity = MainActivity()
        activity.finish()
        exitProcess(0)
    })
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(navController = rememberNavController())
}