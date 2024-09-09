package com.example.routeexplorer2.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen{

    object SignUpScreen: Screen()
    object TermsAndConditionsScreen: Screen()
    object LoginScreen:Screen()
//    object RegisterScreen:Screen()
}

object RouterExplorerAppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)

    fun navigateTo(destination: Screen){
        currentScreen.value=destination
    }
}