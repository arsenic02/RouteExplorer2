package com.example.routeexplorer2

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.example.routeexplorer2.screens.LoginScreen
//import com.example.routeexplorer2.screens.RegisterScreen
import com.example.routeexplorer2.screens.SignUpScreen
import com.example.routeexplorer2.screens.TermsAndConditionsScreen



@Composable
fun RouteExplorer(){
    Surface (
        modifier= Modifier.fillMaxSize(),
//        color = Color.White
    ) {
        //SignUpScreen()
        
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
//                is Screen.RegisterScreen -> {
////                    RegisterScreen()
//                }
            }
        }
    }
}