package com.example.routeexplorer2.screens

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routeexplorer2.MainActivity
import com.example.routeexplorer2.components.AppToolbar
import com.example.routeexplorer2.components.NavigationDrawerBody
import com.example.routeexplorer2.components.NavigationDrawerHeader
import com.example.routeexplorer2.data.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.launch
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    homeViewModel.getUserData()



    val nisCenter = LatLng(43.321445, 21.896104)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nisCenter, 20f)
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }//vrv moze i bez zoomControlsEnabled
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    var markers by remember { mutableStateOf(listOf<LatLng>()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled=drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader(homeViewModel.emailId.value)
            NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList,
                onNavigationItemClicked={
                    Log.d("Coming here","Inside_onNavigationItemClicked")
                    Log.d("Coming here","${it.itemId} ${it.title}")
                } )
        }
    ) {
        Scaffold(
            //gesturesEnabled=drawerState.isOpen,
            topBar = {
                AppToolbar(
                    toolbarTitle = "Home",
                    logoutButtonClicked = {
                        homeViewModel.logout()
                    },
                    onMenuClicked = {
                        scope.launch { drawerState.open() } // Otvara meni
                    }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    //.verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Dodaj svoj sadržaj ekrana
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
                            },

//                            onMapClick = { latLng ->
//                                // Add clicked location to markers list
//                                markers = markers + latLng
//                                Log.d("MapClick", "Location: ${latLng.latitude}, ${latLng.longitude}")
//                            }
                        )
                        {
                            markers.forEach { markerLocation ->
                                Marker(
//                                position = markerLocation,
                                    state=MarkerState(position= LatLng(markerLocation.latitude,markerLocation.longitude)),
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

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}

