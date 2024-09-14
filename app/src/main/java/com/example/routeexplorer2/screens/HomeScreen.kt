package com.example.routeexplorer2.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.routeexplorer2.R
import com.example.routeexplorer2.Screens
import com.example.routeexplorer2.components.AppToolbar
import com.example.routeexplorer2.components.NavigationDrawerBody
import com.example.routeexplorer2.components.NavigationDrawerHeader
import com.example.routeexplorer2.data.home.HomeViewModel
import com.example.routeexplorer2.data.model.User
import com.example.routeexplorer2.viewModels.LoginViewModel
import com.example.routeexplorer2.viewModels.UserViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.launch
import java.net.URL

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    loginViewModel: LoginViewModel,
    userViewModel: UserViewModel


) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    homeViewModel.getUserData()
    val currentUser by userViewModel.currentUser.collectAsState(initial = null)


    val nisCenter = LatLng(43.321445, 21.896104)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nisCenter, 20f)
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }//vrv moze i bez zoomControlsEnabled
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    var markers by remember { mutableStateOf(listOf<LatLng>()) }

    val imageUrl: Uri? = currentUser?.photoPath?.let { Uri.parse(it) }
    val firstName=currentUser?.firstName
    val lastName=currentUser?.lastName
    val phone=currentUser?.phoneNumber
    val username=currentUser?.username
    val mail=currentUser?.email


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
              //  Spacer(modifier = Modifier.height(16.dp)) // Space between profile image and user info

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

                //Spacer(modifier = Modifier.height(32.dp)) // Space between user info and navigation items

               // Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Background color below the divider
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
                        onNavigationItemClicked = {
                            Log.d("Navigation", "${it.itemId} ${it.title}")
                        }
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
                                // Add clicked location to markers list
                                markers = markers + latLng
                                Log.d("MapClick", "Location: ${latLng.latitude}, ${latLng.longitude}")
                            }
                        ) {
                            markers.forEach { markerLocation ->
                                Marker(
                                    state = MarkerState(position = LatLng(markerLocation.latitude, markerLocation.longitude)),
                                    title = "Marker at (${markerLocation.latitude}, ${markerLocation.longitude})"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
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
@Preview
@Composable
fun HomeScreenPreview(){
    //HomeScreen()
}

