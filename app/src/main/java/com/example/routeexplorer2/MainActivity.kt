package com.example.routeexplorer2

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
//import com.example.routeexplorer2.data.services.LocationTrackerService
import com.example.routeexplorer2.viewModels.HomeViewModel
import com.example.routeexplorer2.data.services.NearbyPlacesDetection
import com.example.routeexplorer2.data.services.NearbyPlacesDetectionController
import com.example.routeexplorer2.viewModels.LoginViewModel
import com.example.routeexplorer2.viewModels.LoginViewModelFactory
import com.example.routeexplorer2.viewModels.SignupViewModel
import com.example.routeexplorer2.ui.theme.RouteExplorer2Theme
import com.example.routeexplorer2.viewModels.MarkerViewModel
import com.example.routeexplorer2.viewModels.MarkerViewModelFactory
import com.example.routeexplorer2.viewModels.PlaceViewModel
import com.example.routeexplorer2.viewModels.PlaceViewModelFactory
//import com.example.routeexplorer2.viewModels.MarkerViewModelFactory
import com.example.routeexplorer2.viewModels.RegisterViewModelFactory
import com.example.routeexplorer2.viewModels.UserViewModel
import com.example.routeexplorer2.viewModels.UserViewModelFactory

//import com.example.routeexplorer2.utils.checkLocationPermission

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            userRepository = (application as LoginFlowApp).container.userRepository,
            locationClient = (application as LoginFlowApp).container.locationClient
        )
    }
    private val homeViewModel: HomeViewModel by viewModels()

    // private val loginViewModel: LoginViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userRepository = (application as LoginFlowApp).container.userRepository
        )
    }
    private val signupViewModel: SignupViewModel by viewModels {
        RegisterViewModelFactory(
            userRepository = (application as LoginFlowApp).container.userRepository
        )
    }
    private val markerViewModel: MarkerViewModel by viewModels {
        MarkerViewModelFactory((application as LoginFlowApp).container.markerRepository)
    }

    private val placeViewModel: PlaceViewModel by viewModels {
        PlaceViewModelFactory(placeRepository = (application as LoginFlowApp).container.placeRepository)
    }


    // private val markerViewModel:MarkerViewModel by viewModels()
    //private val homeViewModel:HomeViewModel

//    private lateinit var mapView: MapView
//    private var googleMap: GoogleMap? = null

    var i: Intent? = null//dodato

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //dodato za servis da se prati van viewModela
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//            ),
//            0
//        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            requestBGLocationPermission()
        // }//dodato za servis da se prati van viewModela

        val defaultNearbyPlacesController = object : NearbyPlacesDetectionController {
            override fun startNearbyPlacesDetectionService() {
                Intent(applicationContext, NearbyPlacesDetection::class.java).apply {
                    action = NearbyPlacesDetection.ACTION_START
                    startService(this)
                }
            }

            override fun stopNearbyPlacesDetectionService() {
                Intent(applicationContext, NearbyPlacesDetection::class.java).apply {
                    action = NearbyPlacesDetection.ACTION_STOP
                    startService(this)
                }
            }
        }
        setContent {
            RouteExplorer2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting("Android")
                    RouteExplorer(
                        homeViewModel,
                        userViewModel,
                        loginViewModel,
                        signupViewModel,
                        markerViewModel,
                        placeViewModel,
                        defaultNearbyPlacesController
//                        mapView
                    )
                }
            }

//            checkLocationPermission();
        }
    }

    //dodato da se prati iz servisa
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onStart() {
//        super.onStart()
//        i = Intent(applicationContext, LocationTrackerService::class.java)
//        startForegroundService(i)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        stopService(i)
//    }
//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun requestBGLocationPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ),
//            0
//        )
//    }
//dodato da se prati iz servisa
//}


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        RouteExplorer2Theme {
            Greeting("Android")
        }
    }
}
//@Preview
//@Composable
//fun DefaultPreview(){
//    RouteExplorer()
//}