package com.example.stmikjayakartapresensi.ui.screens.classdetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.CountDownTimer
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stmikjayakartapresensi.common.DatetimeFormat
import com.example.stmikjayakartapresensi.model.LocationModel
import com.example.stmikjayakartapresensi.ui.conponents.Chip
import com.example.stmikjayakartapresensi.ui.conponents.StatusAndNavBarColorPrimaryContainer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.time.LocalTime


private var locationCallback: LocationCallback? = null
var fusedLocationClient: FusedLocationProviderClient? = null
private var locationRequired = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailsScreen(navController: NavController, argsId: Int, classDetailsViewModel: ClassDetailsViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as FragmentActivity

    // Remember states
    var currentLocation by remember { mutableStateOf(LocationModel(0.toDouble(), 0.toDouble())) }
    var userId by remember { mutableStateOf(0) }
    val openDialog= remember { mutableStateOf(false) }
    val loading= remember { mutableStateOf(false) }

    // BindViewModel
    classDetailsViewModel.onViewLoaded(classesId = argsId)
    val classesDetails = classDetailsViewModel.classDetailState.collectAsState()
    val myPresenceStatus = classDetailsViewModel.myPresenceStatusState.collectAsState()
    val isPresence = myPresenceStatus.value.presenceStatus
    val showError = classDetailsViewModel.shouldShowError.value
    val errorMessage = classDetailsViewModel.errorMessage.value
    classDetailsViewModel.shouldShowUser.observe(lifecycleOwner) {
        userId = it.id
        classDetailsViewModel.getMyPresenceStatus(studentsId = it.id, classesId = argsId)
    }

    // Loading & Error handler
    if (showError) Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

    // Multiple Permission GPS request
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMap ->
        val areGranted = permissionMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates()
            Toast.makeText(context, "Silakan lakukan presensi kembali", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // User Interface
    StatusAndNavBarColorPrimaryContainer()
    Scaffold(
        // Top App Bar
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .clip(shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    title = { Text(text = "")},
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = "Back to Home"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            classDetailsViewModel.onViewLoaded(classesId = argsId)
                            Toast.makeText(context, "Data Diperbarui", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = "More Option"
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    classesDetails.value.data.let {
                        val className = it.className.toString()
                        val day = it.day.toString()
                        val classStarted = DatetimeFormat.timeFormatter(it.classStarted.toString())
                        val classEnded = DatetimeFormat.timeFormatter(it.classEnded.toString())
                        val lecturer = it.lecture?.name.toString()
                        val classRoom = it.classRoom.toString()

                        Text(
                            text = className,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$day, $classStarted - $classEnded",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = lecturer,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Chip(classRoom = classRoom)
                    }
                }
            }
        },

        // Contents
        containerColor = MaterialTheme.colorScheme.surface,
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
//                    // Table Head
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(color = MaterialTheme.colorScheme.primary),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "Mahasiswa",
//                            modifier = Modifier.padding(8.dp),
//                            style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.onPrimary,
//                        )
//                    }
//
//                    // loading
//                    if (loading.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//
//                    // Table Body
//                    LazyColumn {
//                        val itemSortedByName = classesDetails.value.data.classDetails.sortedBy { it.student?.name }
//                        itemsIndexed(
//                            items = itemSortedByName
//                        ) { _, item ->
//                            val presence = item.student?.presenceStatus
//                            Column(
//                                modifier = Modifier.background(
//                                    color = if (item.studentsId == userId) {
//                                        MaterialTheme.colorScheme.inversePrimary
//                                    }
//                                    else { MaterialTheme.colorScheme.primaryContainer }
//                                )
//                            ) {
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(vertical = 8.dp, horizontal = 16.dp),
//                                    verticalAlignment = Alignment.CenterVertically,
//                                ) {
//                                    item.student?.name?.let {
//                                        Text(
//                                            text = it,
//                                            modifier = Modifier.weight(1f),
//                                            color = MaterialTheme.colorScheme.onPrimaryContainer
//                                        )
//                                    }
//                                    if (presence!!.isNotEmpty()) {
//                                        Icon(
//                                            imageVector = Icons.Rounded.CheckCircle,
//                                            tint = MaterialTheme.colorScheme.primary,
//                                            contentDescription = "Presence Status",
//                                        )
//                                    } else {
//                                        Icon(
//                                            imageVector = Icons.Rounded.HourglassBottom,
//                                            tint = MaterialTheme.colorScheme.outline,
//                                            contentDescription = "Presence Status",
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }

                // User Presence Status
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val notPresence = isPresence.isEmpty()

                    val startTimeString: String? = classesDetails.value.data.classStarted
                    val endTimeString: String? = classesDetails.value.data.classEnded
                    val inClassTime = isCurrentTimeInRange(startTimeString, endTimeString)
                    val isBefore = isBefore(startTimeString)

                    var statusIcon  by remember { mutableStateOf(Icons.Rounded.HourglassBottom) }
                    var iconColor by remember { mutableStateOf(Color.Black) }
                    var statusTitle by remember { mutableStateOf("") }
                    var statusDesc by remember { mutableStateOf("") }

                    when {
                        notPresence -> {
                            if (!inClassTime) {
                                if (isBefore) {
                                    statusIcon = Icons.Rounded.HourglassBottom
                                    iconColor = MaterialTheme.colorScheme.outline
                                    statusTitle = "Belum Hadir"
                                    statusDesc = "Lakukan kehadiran pada jam kelas."
                                } else {
                                    statusIcon = Icons.Rounded.Cancel
                                    iconColor = MaterialTheme.colorScheme.error
                                    statusTitle = "Kelas Terlewat"
                                    statusDesc = "Kelas sudah selesai dan kamu tidak melakukan presensi kelas."
                                }

                            } else {
                                statusIcon = Icons.Rounded.HourglassBottom
                                iconColor = MaterialTheme.colorScheme.outline
                                statusTitle = "Belum Dimulai"
                                statusDesc = "Kamu belum hadir. Pergi ke kelas dan lakukan hadir."
                            }
                        }
                        else -> {
                            statusIcon = Icons.Rounded.CheckCircle
                            iconColor = MaterialTheme.colorScheme.primary
                            statusTitle = "Hadir"
                            statusDesc = "Kamu sudah melakukan presensi."
                        }
                    }

                    Icon(
                        imageVector = statusIcon,
                        contentDescription = "Status Icon",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(100.dp),
                        tint = iconColor
                    )
                    Text(
                        text = statusTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = iconColor
                    )
                    Text(
                        text = statusDesc,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Dialog
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    title = { Text(text = "Tidak Dalam Radius") },
                    text = { Text(text = "Kamu tidak dalam radius Jayakarta. Pastikan kamu berada dalam kelas dan GPS menyala lalu lakukan  presensi kembali.")},
                    confirmButton = {
                        TextButton(onClick = {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            context.startActivity(intent)
                        }) {
                            Text(text = "NYALAKAN GPS")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { openDialog.value = false }) {
                            Text(text = "KEMBALI")
                        }
                    },
                )
            }
        },

        // Presence FAB
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (lo in p0.locations) {
                        currentLocation = LocationModel(lo.latitude, lo.longitude)
                    }
                }
            }

            val startTimeString: String? = classesDetails.value.data.classStarted
            val endTimeString: String? = classesDetails.value.data.classEnded
            val inClassTime = isCurrentTimeInRange(startTimeString, endTimeString)

            if (isPresence.isEmpty() && inClassTime) {
                ExtendedFloatingActionButton(
                    onClick = {
                        val permissions = arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )

                        // Check the GPS permissions
                        if (permissions.all {
                                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                            }) {
                            startLocationUpdates() // update the user location before auth.

                            // Start Authentication process
                            val availabilityCheck = Biometric.status(context)
                            if (availabilityCheck) {
                                Biometric.authenticate(
                                    activity = activity,
                                    context = context,
                                    title = "Biometric Authentication",
                                    subtitle = "Authenticate to process",
                                    description = "Untuk dapat presensi, silakan pakai autentikasi",
                                    negativeText = "Batal",
                                    onSuccess = {
                                        val onRadius = isWithinRadius(
                                            currentLatitude = currentLocation.latitude,
                                            currentLongitude = currentLocation.longitude
                                        )

                                        if (onRadius) {
                                            loading.value = true
                                            classDetailsViewModel.postStudentPresence(studentsId = userId, classesId = argsId)
                                            Toast.makeText(context, "Berhasil Presensi!", Toast.LENGTH_SHORT).show()
                                            val timer = object : CountDownTimer(2000, 1000) {
                                                override fun onTick(millisUntilFinished: Long){}
                                                override fun onFinish() {
                                                    classDetailsViewModel.onViewLoaded(classesId = argsId)
                                                    loading.value = false
                                                }
                                            }
                                            timer.start()
                                        } else {
                                            openDialog.value = true
                                        }
                                    },
                                    onError = { errorCode, errorString->
                                        Toast.makeText(
                                            context,
                                            "$errorCode: $errorString",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    Toast.makeText(
                                        context,
                                        "Authentication gagal. Pastikan sidik jarimu bersih",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(context, "Auth Tidak tersedia", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            launcherMultiplePermissions.launch(permissions)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Presence Button"
                        )
                    },
                    text = {
                        Text(
                            text = ("Hadir"),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }

        }
    )

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (locationRequired) {
                        startLocationUpdates()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    locationCallback?.let {
                        fusedLocationClient?.removeLocationUpdates(it)
                    }
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer = observer)
    }
}

// Logic for class time in range
private fun isCurrentTimeInRange(startTimeString: String?, endTimeString: String?): Boolean {
    if (startTimeString == null || endTimeString == null) {
        return false
    }

    val startTime = LocalTime.parse(startTimeString)
    val endTime = LocalTime.parse(endTimeString)
    val currentTime = LocalTime.now()

    return currentTime.isAfter(startTime) && currentTime.isBefore(endTime)
}

private fun isBefore(startTimeString: String?): Boolean {
    if (startTimeString == null) return false

    val startTime = LocalTime.parse(startTimeString)
    val currentTime = LocalTime.now()

    return currentTime.isBefore(startTime)
}

// Get the user Location logic
@SuppressLint("MissingPermission")
private fun startLocationUpdates() {
    val locationInterval = 1000L
    val locationFastestInterval = 500L
    val locationMaxWaitTime = 1000L

    locationCallback?.let {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(locationFastestInterval)
            .setMaxUpdateAgeMillis(locationMaxWaitTime)
            .build()

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}

// Logic for range Latitude & Longitude Jayakarta
fun isWithinRadius(currentLatitude: Double, currentLongitude: Double): Boolean {
    val targetLocation = Location("").apply {
        latitude = -6.198570
        longitude = 106.850700
    }

    val currentLocation = Location("").apply {
        latitude = currentLatitude
        longitude = currentLongitude
    }

    val distanceInMeters = currentLocation.distanceTo(targetLocation)

    return distanceInMeters <= 100 // Radius in Meters
}

@Preview(showBackground = true)
@Composable
fun ClassDetailsPreview() {
    ClassDetailsScreen(navController = rememberNavController(), 1)
}