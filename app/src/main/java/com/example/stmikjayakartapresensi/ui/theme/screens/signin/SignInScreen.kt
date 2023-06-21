package com.example.stmikjayakartapresensi.ui.theme.screens.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.stmikjayakartapresensi.ui.theme.conponents.EmailTextField
import com.example.stmikjayakartapresensi.ui.theme.conponents.PasswordTextField

@Composable
fun SignInScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        EmailTextField(label = "Alamat Email", placeholder = "Email Jayakarta")
        PasswordTextField(label = "Kata Sandi", placeholder = "Kata Sandi")
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Masuk")
        }
    }
}