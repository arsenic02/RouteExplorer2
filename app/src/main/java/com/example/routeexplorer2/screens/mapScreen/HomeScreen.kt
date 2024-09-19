package com.example.routeexplorer2.screens.mapScreen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.routeexplorer2.MainActivity
import com.example.routeexplorer2.R
import com.example.routeexplorer2.Screens
import com.example.routeexplorer2.components.AppToolbar
import com.example.routeexplorer2.components.NavigationDrawerBody
import com.example.routeexplorer2.viewModels.HomeViewModel
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.data.services.NearbyPlacesDetectionController
import com.example.routeexplorer2.screens.MapMarker
import com.example.routeexplorer2.viewModels.LoginViewModel
import com.example.routeexplorer2.viewModels.MapViewModel
import com.example.routeexplorer2.viewModels.MarkerViewModel
import com.example.routeexplorer2.viewModels.PlaceViewModel
import com.example.routeexplorer2.viewModels.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import hasLocationPermissions
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.launch

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    loginViewModel: LoginViewModel,
    userViewModel: UserViewModel,
    markerViewModel:MarkerViewModel,
    selectPlace:(Place)->Unit,
    defaultNearbyPlaceController: NearbyPlacesDetectionController,
    placeViewModel: PlaceViewModel,
    mapViewModel: MapViewModel= viewModel()


) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    homeViewModel.getUserData()
    val currentUser by userViewModel.currentUser.collectAsState(initial = null)
    val currentUserLocation by userViewModel.currentUserLocation.collectAsState()
    val filteredMarkers by markerViewModel.filteredMarkers.collectAsState()

    var isAddPlaceModalOpen by remember { mutableStateOf(false) }
    var isServiceDialogOpen by remember { mutableStateOf(false) }
    var isServiceRunning by remember { mutableStateOf(getServiceRunningState(context)) } // Load state

    val currentPosition = currentUserLocation ?: LocationData(43.321445, 21.896104)//LocationData iz klase LocationData
//    val nisCenter = LatLng(43.321445, 21.896104)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                userViewModel.updateLocation()
            } else {
                handlePermissionRationale(context, permissions)
            }
        }
    )

    // Request permission and start location updates if permissions are granted
    LaunchedEffect(Unit) {
        if (hasLocationPermissions(context)) {

            userViewModel.updateLocation()

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            //nisCenter, 20f)
            LatLng(
                currentPosition.latitude,
                currentPosition.longitude
            ),14f
        )
    }
    LaunchedEffect(currentUserLocation) {
        currentUserLocation.let {
            if (it != null) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), 15f // Adjust zoom level as needed
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        markerViewModel.fetchMarkers()
    }

    // Loading the custom icon


    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }//vrv moze i bez zoomControlsEnabled
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    //var markers by remember { mutableStateOf(listOf<LatLng>()) }
    val markers by markerViewModel.markers.collectAsState()

    val imageUrl: Uri? = currentUser?.photoPath?.let { Uri.parse(it) }
    val firstName=currentUser?.firstName
    val lastName=currentUser?.lastName
    val phone=currentUser?.phoneNumber
    val username=currentUser?.username
    val mail=currentUser?.email

    var showFilterDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Column(
                modifier = Modifier
                    //.fillMaxSize()
                    // .fillMaxHeight()
                    .padding(16.dp)
                    .width(280.dp) // Adjust drawer width here
            ) {
                // Background color above the divider
//                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(Color.Blue) // First shade of blue
                        .background(colorResource(id = R.color.higherDrawerColor))
                )
                {

                    if (drawerState.isOpen) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center),
                                //.padding(15.dp),
                                //.align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop,
                            placeholder = rememberVectorPainter(image = Icons.Default.AccountCircle),
                            error = rememberVectorPainter(image = Icons.Default.AccountCircle),
                            contentDescription = null,
                        )
                       // Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color.Blue) // Background color for user info section
                        .background(colorResource(id = R.color.higherDrawerColor))
                        .fillMaxWidth()
                        //.padding(16.dp)
                ) {
                    UserInfoSection(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        email = mail,
                        username = username
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.lowerDrawerColor))
                       // .background(Color.Green) // Second shade of blue
                ) {
                    // Navigation links (Home, Leaderboards, Routes)
                    NavigationDrawerBody(
                        navigationDrawerItems = homeViewModel.navigationItemsList,
                        imageUrl = imageUrl,
                        onImageChange = { uri -> /* Handle image change here if needed */ },

                        onNavigationItemClicked = { item ->
                            when (item.itemId) {
                                "routes" -> {
                                    navController.navigate(Screens.Places.route) {
                                        //popUpTo(Screens.GoogleMap.route) { inclusive = true } //suvisno

                                    }
                                }
                                "leaderboards" -> {
                                    navController.navigate(Screens.Leaderboard.route) {
                                        //popUpTo(Screens.GoogleMap.route) { inclusive = true }
                                    }
                                }
                                else -> {
                                    Log.d("Navigation", "Unknown navigation item clicked")
                                }
                            }
                        }

                        //ovako je bilo
//                        onNavigationItemClicked = {
//                            Log.d("Navigation", "${it.itemId} ${it.title}")
//                            navController.navigate(Screens.Leaderboard.route)
//                        } //ovako je bilo
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppToolbar(
                    toolbarTitle = "RouteExplorer",
                    logoutButtonClicked = {
                        loginViewModel.signOut { success ->
                            if (success) {
                                navController.navigate(Screens.Login.route) {
                                    popUpTo(Screens.GoogleMap.route) { inclusive = true }
                                }
                                Toast.makeText(context, "Successfully signed out", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Sign out failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onMenuClicked = {
                        scope.launch { drawerState.open() } // Opens the drawer
                    },
                    onSearchClicked = {
                        showFilterDialog = true  // Show the filter dialog when search is clicked
                    }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = properties,
                            uiSettings = uiSettings,
                            onMapLongClick = { latLng ->

                                isAddPlaceModalOpen=true
                                markerViewModel.setLatLng(latLng)
                               // markerViewModel.setNewAddress(reverseGeocodeLocation(context = context, it))
                                //uklonio sam ovo, mislim da je suvisno
//                                markerViewModel.createMarker { success, message ->
//                                    if (success) {
//                                        Toast.makeText(context, "Marker added!", Toast.LENGTH_SHORT).show()
//                                    } else {
//                                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
                            }
                            //nije ni ovo lose, samo ne moze da se plamti u bazi
//                            onMapLongClick = { latLng ->
//                                markers = markers + latLng
//                                Log.d("MapClick", "Location: ${latLng.latitude}, ${latLng.longitude}")
//                            }


                        ) {

                            currentUserLocation?.let {
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(it.latitude, it.longitude)
                                    ),
                                    title = "You are here",
                                )

                            }
////ovde filteredMarkers ubaci
                            //markers.forEach
                            val markersToDisplay =
                                if (filteredMarkers.isNotEmpty()) filteredMarkers else markers
                            markersToDisplay.forEach { markerLocation ->
                                val type = markerLocation.selectedOption
                                //Log.d("Type=", type)
                                val iconResId = when (type) {
                                    "Run" -> R.drawable.ic_run_24
                                    "Bike" -> R.drawable.baseline_directions_bike_24
                                    "Car" -> R.drawable.ic_car_24
                                    else -> R.drawable.ic_car_24 // A default marker icon if needed
                                }
                                //zasad zakomentarisano
                                //val icon = bitmapDescriptorFromVector(context, iconResId) // iconResId from Firestore or wherever markers are fetched
//                                Marker(
//                                    state = MarkerState(position = LatLng(markerLocation.latitude, markerLocation.longitude)),
//                                    title = "Marker at (${markerLocation.latitude}, ${markerLocation.longitude})",
//                                    icon = icon
//                                )
                                MapMarker(
                                    context = context,
                                    position = LatLng(markerLocation.latitude, markerLocation.longitude),
                                    title = markerLocation.name,
                                    snippet = "Marker in ${markerLocation.name}",
                                    iconResourceId = iconResId
                                ) {
                                    selectPlace(markerLocation)
                                    Log.d("selectPlace","$markerLocation")
                                    navController.navigate(Screens.Place.route)
                                    true // Return true to indicate that the click event is consumed
                                }
                            }
                        }

                        if (isAddPlaceModalOpen) {
                            AddPlaceModal(
                                context,
                                markerViewModel,
                                onDismiss = { isAddPlaceModalOpen = false }
                            )
                        }

                        if (showFilterDialog) {
                            FilterPlaceModal(
                                context = context,
                                currentUserLocation = currentUserLocation,
                                markerViewModel = markerViewModel,
                                onDismiss = { showFilterDialog = false }  // Close the dialog
                            )
                        }

                        if (filteredMarkers.isNotEmpty()) {
                            IconButton(
                                onClick = { markerViewModel.removeFilters() },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(
                                        top = 16.dp,
                                        bottom = 24.dp,
                                        start = 130.dp
                                    ) // Adjust padding as needed
                                    .size(40.dp) // Larger size for the button
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search_off_24),
                                    contentDescription = "Remove filters",
                                    tint = MaterialTheme.colorScheme.primary, // Adjust icon color if needed
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        currentUserLocation?.let {
                            IconButton(
                                onClick = { isServiceDialogOpen = true },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp) // Adjust padding as needed
                                    .size(40.dp) // Larger size for the button
                            ) {
                                Icon(
                                    painter = if (isServiceRunning) painterResource(id = R.drawable.notifications_active_24) else painterResource(
                                        id = R.drawable.notifications_24
                                    ),
                                    contentDescription = if (isServiceRunning) "Notifications" else "Notifications Off",
                                    tint = MaterialTheme.colorScheme.primary, // Adjust icon color if needed
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        if (isServiceDialogOpen) {
                            ServiceControllDialog(
                                isServiceRunning = isServiceRunning,
                                onConfirm = {
                                    isServiceRunning = !isServiceRunning
                                    saveServiceRunningState(context, isServiceRunning) // Save the new state
                                    if (isServiceRunning) {
                                        defaultNearbyPlaceController.startNearbyPlacesDetectionService()
                                    } else {
                                        defaultNearbyPlaceController.stopNearbyPlacesDetectionService()
                                    }
                                },
                                onDismiss = {
                                    isServiceDialogOpen = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
fun saveServiceRunningState(context: Context, isRunning: Boolean) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isServiceRunning", isRunning)
    editor.apply()
}

@Composable
fun UserInfoSection(
    firstName: String?,
    lastName: String?,
    phone: String?,
    email: String?,
    username: String?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Icon for First Name and Last Name
        UserInfoRow(
            iconId = R.drawable.ic_person_24,
            label = "$firstName $lastName"
        )
        // Icon for Email
        UserInfoRow(
            iconId = R.drawable.ic_mail_24,
            label = email
        )
        // Icon for Username
        UserInfoRow(
            iconId = R.drawable.ic_person_24,
            label = username
        )
        // Icon for Phone Number
        UserInfoRow(
            iconId = R.drawable.ic_phone_android_24,
            label = phone
        )
    }
}

@Composable
fun UserInfoRow(iconId: Int, label: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label ?: "Unknown")
    }
}


fun getServiceRunningState(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isServiceRunning", false)
}
@Preview
@Composable
fun HomeScreenPreview(){
    //HomeScreen()
}

fun handlePermissionRationale(context: Context, permissions: Map<String, Boolean>) {
    val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
        context as MainActivity,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) || ActivityCompat.shouldShowRequestPermissionRationale(
        context as MainActivity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    if (rationalRequired) {
        Toast.makeText(
            context,
            "Location Permission is required for this feature to work",
            Toast.LENGTH_LONG
        ).show()
    } else {
        Toast.makeText(
            context,
            "Location Permission is required, please enable it in the Android Settings",
            Toast.LENGTH_LONG
        ).show()
    }
}

