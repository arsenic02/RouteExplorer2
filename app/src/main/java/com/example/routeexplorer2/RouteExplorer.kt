package com.example.routeexplorer2

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.routeexplorer2.viewModels.HomeViewModel
import com.example.routeexplorer2.data.services.NearbyPlacesDetectionController
import com.example.routeexplorer2.screens.LeaderboardScreen
import com.example.routeexplorer2.viewModels.LoginViewModel
import com.example.routeexplorer2.viewModels.SignupViewModel
import com.example.routeexplorer2.screens.mapScreen.HomeScreen
import com.example.routeexplorer2.screens.LoginScreen
import com.example.routeexplorer2.screens.PlacesScreen
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
    Places("places"),
    Place("place")
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RouteExplorer(
    homeViewModel: HomeViewModel = viewModel(),
    userViewModel: UserViewModel,
    loginViewModel: LoginViewModel,
    signupViewModel: SignupViewModel,
    markerViewModel: MarkerViewModel,
    placeViewModel: PlaceViewModel,
    defaultNearbyPlaceController: NearbyPlacesDetectionController
   // mapView:MapView
) {
    val TAG = HomeViewModel::class.simpleName
    val currentUser by userViewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val navController = rememberNavController()

    LaunchedEffect(navController) {//moze i sa key1=currentUser
        homeViewModel.checkForActiveSession(navController)
    }

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
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                loginViewModel = loginViewModel,
                userViewModel = userViewModel,
                markerViewModel=markerViewModel,
                placeViewModel=placeViewModel,
                selectPlace={placeViewModel.setCurrentPlaceState(it)},
                defaultNearbyPlaceController = defaultNearbyPlaceController
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
                userViewModel=userViewModel,
                placeViewModel = placeViewModel,
            )
        }
        composable(Screens.Leaderboard.name) {
            LeaderboardScreen(
                userViewModel = userViewModel
            )
        }
        composable(Screens.Places.name) {
            PlacesScreen(
                navController = navController,
                userViewModel = userViewModel,
                markerViewModel = markerViewModel,
                placeViewModel=placeViewModel,
                selectPlace = { placeViewModel.setCurrentPlaceState(it)}//.setCurrentFieldState(it)}
            )
        }
    }
}