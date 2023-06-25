package com.example.stmikjayakartapresensi.ui.theme.conponents

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

/* Untuk saat in button tidak terpakai karena hanya 1 kali pemanggilan saja di aplikasi ini */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultButton(textButton: String) {
    Button(
        onClick = { /*TODO*/ }
    ) {
        Text(text = textButton)
    }
}