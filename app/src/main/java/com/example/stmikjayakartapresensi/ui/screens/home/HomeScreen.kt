package com.example.stmikjayakartapresensi.ui.screens.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stmikjayakartapresensi.MainActivity
import com.example.stmikjayakartapresensi.common.DatetimeFormat
import com.example.stmikjayakartapresensi.ui.conponents.ClassesCard
import com.example.stmikjayakartapresensi.ui.conponents.StatusAndNavBarColorBackground
import com.example.stmikjayakartapresensi.ui.navigation.Screen
import kotlin.system.exitProcess

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // Remember State
    val openDialog= remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }

    // Bind ViewModel
    homeViewModel.onViewLoaded()
    val classes = homeViewModel.todayClassesState.collectAsState()
    val showError = homeViewModel.shouldShowError.value
    val errorMessage = homeViewModel.errorMessage.value
    homeViewModel.shouldShowUser.observe(lifecycleOwner) {
        email = it.email
        nim = it.nim
        name = it.name
        major = it.major
    }

    // error handler
    if (showError) Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

    // User Interface
    StatusAndNavBarColorBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Icon Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            TextButton(
                onClick = {
                    openDialog.value = true
                    // navController.navigate(route = Screen.ClassDetails.passId(7))
                },
            ) {
                Text(text = "Halo, $name")
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Profile"
                )
            }
        }

        // Headline
        Text(
            text = "Kelas Hari Ini",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        // Cards
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val dataSortedByTime = classes.value.data.sortedBy { it.classDetails?.classStarted }
            itemsIndexed(
                items = dataSortedByTime
            ) {_, item ->
                val startClass = DatetimeFormat.timeFormatter(item.classDetails?.classStarted.toString()).toString()
                val endClass = DatetimeFormat.timeFormatter(item.classDetails?.classEnded.toString()).toString()

                ClassesCard(
                    day = item.classDetails?.day.toString(),
                    startClass = startClass,
                    endClass = endClass,
                    subject = item.classDetails?.className.toString(),
                    lecturer = item.classDetails?.lecture?.name.toString(),
                    classRoom = item.classDetails?.classRoom.toString(),
                    onClick = {
                        navController.navigate(route = Screen.ClassDetails.passId(item.classesId!!))
                    }
                )
            }
        }

    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Profil Mahasiswa") },
            text = {
                Row() {
                    Column() {
                        Text(text = "Email ")
                        Text(text = "NIM ")
                        Text(text = "Nama ")
                        Text(text = "Prodi ")
                    }
                    Column() {
                        Text(text = ": $email")
                        Text(text = ": $nim")
                        Text(text = ": $name")
                        Text(text = ": $major")
                    }
                }
                   },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "KEMBALI")
                }
            }
        )
    }
    
    BackHandler(enabled = true, onBack = {
        val activity = MainActivity()
        activity.finish()
        exitProcess(0)
    })
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}