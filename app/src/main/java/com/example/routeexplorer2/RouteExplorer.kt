package com.example.routeexplorer2

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.routeexplorer2.data.home.HomeViewModel
import com.example.routeexplorer2.data.login.LoginViewModel
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.example.routeexplorer2.screens.HomeScreen
import com.example.routeexplorer2.screens.LoginScreen
//import com.example.routeexplorer2.screens.RegisterScreen
import com.example.routeexplorer2.screens.SignUpScreen
import com.example.routeexplorer2.screens.TermsAndConditionsScreen
import com.example.routeexplorer2.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//enum class Screens {
//    Login,
//    Register,
//    GoogleMap,
//    Leaderboard,
//    Fields,
//    Field
//}
//
//@Composable
//fun RouteExplorer(homeViewModel: HomeViewModel=viewModel(),
//                  userViewModel: UserViewModel,
//                  loginViewModel: LoginViewModel
//                 // registerViewModel: RegisterViewModel,
//
//                  ){
//
//    val TAG = HomeViewModel::class.simpleName
//    val currentUser by userViewModel.currentUser.collectAsState()
//    var isLoading by remember { mutableStateOf(true) }
//    homeViewModel.checkForActiveSession()
//
//    if(!userViewModel.isUserLoggedIn() || currentUser != null){/*homeViewModel.isUserLoggedIn.value==true*/
//        Log.d(TAG,"Korisnik je logovan")
//        RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
//    }
//
////    Surface (
////        modifier= Modifier.fillMaxSize(),
////
////    ) {
////
////        Crossfade(targetState = RouterExplorerAppRouter.currentScreen) { currentState ->
////            when(currentState.value){
////                is Screen.SignUpScreen -> {
////                    SignUpScreen()
////                }
////                is Screen.TermsAndConditionsScreen -> {
////                    TermsAndConditionsScreen()
////                }
////                is Screen.LoginScreen -> {
////                    LoginScreen()
////                }
////                is Screen.HomeScreen ->{
////                   // HomeScreen(homeViewModel,loginViewModel)
////                    RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
////                }
////            }
////        }
////    }
//
//    if (isLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                CircularProgressIndicator()
//                Spacer(Modifier.height(4.dp))
//                Text("Loading...")
//            }
//        }
//    } else {
//        val navController: NavHostController = rememberNavController()
//        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//        val coroutineScope: CoroutineScope = rememberCoroutineScope()
//
//        val startDestination =
//            if (currentUser != null) Screens.GoogleMap.name else Screens.Login.name
//
//        ModalNavigationDrawer(
//            drawerState = drawerState,
//            gesturesEnabled = false,
//            drawerContent = {
//                ModalDrawerSheet {
//                    DrawerContent(
//                        currentUser = currentUser,
//                        onAction = { route ->
//                            coroutineScope.launch {
//                                drawerState.close() // Zatvori drawer u oba slučaja
//                                route?.let {
//                                    navController.navigate(it) // Navigiraj ako ruta nije null
//                                }
//                            }
//                        }
//
//
//                    )
//                }
//            }
//        ) {
//            Scaffold(
////                topBar = {
////                    CustomAppBar(
////                        drawerState = drawerState,
////                        navController = navController,
////                        //selectedFieldName = fieldViewModel.selectedFieldState.name,
////                        loginViewModel = loginViewModel,
////                        //removeFilter = { markerViewModel.removeFilters() },
////                    )
////                },
//                content = { paddingValues ->
//                    NavHost(
//                        navController = navController,
//                        startDestination = startDestination,
//                        Modifier.padding(paddingValues)
//                    ) {
//                        composable(Screens.Register.name) {
////                            RegisterScreen(
////                                navController = navController,
////                                registerViewModel = registerViewModel
////                            )
//                        }
//                        composable(Screens.Login.name) {
//                            LoginScreen(
//                                navController = navController,
//                                loginViewModel = loginViewModel
//                            )
//                        }
//                        composable(Screens.GoogleMap.name) {
//                            HomeScreen(
//                                navController = navController,
//                                userViewModel = userViewModel,
//                                loginViewModel = loginViewModel,
//                                homeViewModel = homeViewModel
//
////                                markerViewModel = markerViewModel,
////                                selectField = { fieldViewModel.setCurrentFieldState(it)},
////                                defaultnearbyfieldcontroller = defaultnearbyfieldcontroller
//                            )
//                        }
//
////                        composable(Screens.Leaderboard.name) {
////                            LeaderboardScreen(
////                                userViewModel = userViewModel
////                            )
////                        }
//
////                        composable(Screens.Fields.name) {
////                            FieldsScreen(
////                                navController = navController,
////                                userViewModel = userViewModel,
////                                markerViewModel = markerViewModel,
////                                selectField = { fieldViewModel.setCurrentFieldState(it)}
////                            )
////                        }
//
////                        composable(Screens.Field.name) {
////                            FieldScreen(
////                                userViewModel = userViewModel,
////                                fieldViewModel = fieldViewModel
////                            )
////                        }
//                    }
//                }
//            )
//        }
//    }
//}
enum class Screens(val route: String) {
    Login("login"),
    Register("register"),
    GoogleMap("google_map"),
    Leaderboard("leaderboard"),
    Fields("fields"),
    Field("field")
}
@Composable
fun RouteExplorer(
    homeViewModel: HomeViewModel = viewModel(),
    userViewModel: UserViewModel,
    loginViewModel: LoginViewModel
) {
    val TAG = HomeViewModel::class.simpleName
    val currentUser by userViewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(true) }



    homeViewModel.checkForActiveSession()

    if (currentUser != null || userViewModel.isUserLoggedIn()) {
        Log.d(TAG, "Korisnik je logovan")
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(4.dp))
                Text("Loading...")
            }
        }
    } else {
        val navController: NavHostController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val selectedFieldName = "Selected Field"
        val title = when (currentRoute) {
            Screens.GoogleMap.name -> "Google Map"
            Screens.Register.name -> "Register"
            Screens.Login.name -> "Login"
            Screens.Leaderboard.name -> "Leaderboard"
            Screens.Fields.name -> "Fields"
//        Screens.Field.name -> selectedFieldName
            else -> "Redirecting..." // Podrazumevani naslov
        }
        NavHost(
            navController = navController,
            startDestination = if (currentUser != null) Screens.GoogleMap.route else Screens.Login.route
        ) {
            composable(Screens.Login.route) {
                LoginScreen(navController = navController, loginViewModel = loginViewModel)
            }
            composable(Screens.GoogleMap.route) {
                HomeScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    loginViewModel = loginViewModel,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}