package com.example.routeexplorer2

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routeexplorer2.data.home.HomeViewModel
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.example.routeexplorer2.screens.HomeScreen
import com.example.routeexplorer2.screens.LoginScreen
//import com.example.routeexplorer2.screens.RegisterScreen
import com.example.routeexplorer2.screens.SignUpScreen
import com.example.routeexplorer2.screens.TermsAndConditionsScreen



@Composable
fun RouteExplorer(homeViewModel: HomeViewModel=viewModel()){

    val TAG = HomeViewModel::class.simpleName
    homeViewModel.checkForActiveSession()
    Surface (
        modifier= Modifier.fillMaxSize(),
//        color = Color.White
    ) {
        //SignUpScreen()
        if(homeViewModel.isUserLoggedIn.value==true){
            Log.d(TAG,"Korisnik je logovan")
            RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
        }
        Crossfade(targetState = RouterExplorerAppRouter.currentScreen) { currentState ->
            when(currentState.value){
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }
                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                is Screen.HomeScreen ->{
                    HomeScreen()
                }
//                is Screen.RegisterScreen -> {
////                    RegisterScreen()
//                }
            }
        }
    }
}