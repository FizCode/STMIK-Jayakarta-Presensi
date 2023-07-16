package com.example.stmikjayakartapresensi.ui.screens.classdetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
    var currentLocation by remember { mutableStateOf(LocationModel(0.toDouble(), 0.toDouble())) }
    var userId by remember { mutableStateOf(0) }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        MaterialTheme.colorScheme.background,
        darkIcons = true
    )

    classDetailsViewModel.onViewLoaded(classesId = argsId)
    val classesDetails = classDetailsViewModel.classDetailState.collectAsState()
    val myPresenceStatus = classDetailsViewModel.myPresenceStatusState.collectAsState()
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
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // User Interface
    Scaffold(
        modifier = Modifier.background(color = Color.White),

        // Top App Bar
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .clip(shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                TopAppBar(
                    title = { Text(text = "")},
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to Home")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Option")
                        }
                    }
                )
                Column(
                    modifier = Modifier.padding(16.dp),
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$day, $classStarted - $classEnded",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = lecturer,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Chip(classRoom = classRoom)
                    }

                }
            }
        },

        // Contents
        containerColor = Color.White,
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.primary),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mahasiswa",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }

                    LazyColumn() {
                        itemsIndexed(
                            items = classesDetails.value.data.classDetails
                        ) { _, item ->
                            val presenceStatuses = item.student?.presenceStatus
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item.student?.name?.let {
                                    Text(
                                        text = it,
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                if (presenceStatuses!!.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = "Presence Status",
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.HourglassBottom,
                                        tint = MaterialTheme.colorScheme.outline,
                                        contentDescription = "Presence Status",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },

        // Presence FAB
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {

            val myPresence = myPresenceStatus.value.presenceStatus

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

            fun isCurrentTimeInRange(): Boolean {
                if (startTimeString == null || endTimeString == null) {
                    return false
                }

                val startTime = LocalTime.parse(startTimeString)
                val endTime = LocalTime.parse(endTimeString)
                val currentTime = LocalTime.now()

                return currentTime.isAfter(startTime) && currentTime.isBefore(endTime)
            }

            if (myPresence.isEmpty() && isCurrentTimeInRange()) {
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
                                            classDetailsViewModel.postStudentPresence(studentsId = userId, classesId = argsId)
                                            Toast.makeText(context, "Berhasil Presensi!", Toast.LENGTH_SHORT).show()
                                            classDetailsViewModel.onViewLoaded(argsId)
                                        } else {
                                            Toast.makeText(
                                                context, "Pastikan kamu berada dalam kelas dan GPS menyala.",
                                                Toast.LENGTH_SHORT
                                            ).show()
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