package com.example.routeexplorer2

import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.routeexplorer2.data.home.HomeViewModel
import com.example.routeexplorer2.viewModels.LoginViewModel
import com.example.routeexplorer2.viewModels.SignupViewModel
import com.example.routeexplorer2.screens.mapScreen.HomeScreen
import com.example.routeexplorer2.screens.LoginScreen
//import com.example.routeexplorer2.screens.RegisterScreen
import com.example.routeexplorer2.screens.SignUpScreen
import com.example.routeexplorer2.screens.mapScreen.PlaceScreen
import com.example.routeexplorer2.viewModels.MarkerViewModel
import com.example.routeexplorer2.viewModels.PlaceViewModel
import com.example.routeexplorer2.viewModels.UserViewModel
import com.google.android.gms.maps.OnMapReadyCallback

enum class Screens(val route: String) {
    Login("login"),
    Register("register"),
    GoogleMap("google_map"),
    Leaderboard("leaderboard"),
    Fields("fields"),
    Field("field"),
    Place("place")
}
@Composable
fun RouteExplorer(
    homeViewModel: HomeViewModel = viewModel(),
    userViewModel: UserViewModel,
    loginViewModel: LoginViewModel,
    signupViewModel: SignupViewModel,
    markerViewModel: MarkerViewModel,
    placeViewModel: PlaceViewModel
   // mapView:MapView
) {
    val TAG = HomeViewModel::class.simpleName
    val currentUser by userViewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val navController = rememberNavController()

    LaunchedEffect(/*key1 = currentUser*/navController) {//moze i sa key1=currentUser
        //isLoading = true //dodato
        homeViewModel.checkForActiveSession(navController)
        //isLoading = false//dodato
    }

    val mapCallback = rememberUpdatedState(OnMapReadyCallback { googleMap ->
        // Your map setup code here
    })
    //nije ni ovo lose, radi, ali ima mali bag, da se otvori na trenutak npr. ekran za logovanje, pa nema nista, pa se onda za stalno otvori ekran za logovanje
    NavHost(
        navController = navController,
        startDestination = if (userViewModel.isUserLoggedIn()) Screens.GoogleMap.route else Screens.Login.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(Screens.GoogleMap.route) {
//            Column(modifier = Modifier.fillMaxSize()) {
//                AndroidView(
//                    factory = { mapView },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    update = { mapView ->
//                        mapView.getMapAsync { googleMap ->
//                            mapCallback.value.onMapReady(googleMap)
//                        }
//                    }
//                )
//            }
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                loginViewModel = loginViewModel,
                userViewModel = userViewModel,
                markerViewModel=markerViewModel,
                selectPlace={placeViewModel.setCurrentPlaceState(it)}
            )
        }
        composable(Screens.Register.route) {
            SignUpScreen(
                signupViewModel = signupViewModel,
                navController = navController
            )
        }
        composable(Screens.Place.route) {
            PlaceScreen(
                //signupViewModel = signupViewModel,
                userViewModel=userViewModel,
                placeViewModel = placeViewModel,
                //navController = navController
            )
        }
    }

    // Ovo koristim da proverim da li je korisnik ulogovan kada opet otvori aplikaciju
//    if (!userViewModel.isUserLoggedIn() || currentUser != null) {
//        isLoading = false
//        Log.d(TAG,"isLoading=${isLoading}")
//    }
//
//    if (isLoading) {
//        // Show a loading spinner while checking the session
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    } else {
//
//        // Show the actual content after loading completes
//        NavHost(
//            navController = navController,
//            startDestination = if (userViewModel.isUserLoggedIn()) Screens.GoogleMap.route else Screens.Login.route
//        ) {
//            composable(Screens.Login.route) {
//                LoginScreen(
//                    navController = navController,
//                    loginViewModel = loginViewModel
//                )
//            }
//            composable(Screens.GoogleMap.route) {
//                HomeScreen(
//                    navController = navController,
//                    homeViewModel = homeViewModel,
//                    loginViewModel = loginViewModel,
//                    userViewModel = userViewModel
//                )
//            }
//        }
//    }
}