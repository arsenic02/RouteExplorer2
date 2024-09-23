package com.example.routeexplorer2.viewModels

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Route
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.routeexplorer2.Screens
import com.example.routeexplorer2.data.NavigationItem
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel: ViewModel() {
    private val TAG = HomeViewModel::class.simpleName
    val isLoading = MutableLiveData(true)  // Initialize as loading

    val navigationItemsList = listOf<NavigationItem>(
        NavigationItem(
            title="Places",
            icon=Icons.Default.Route,//neka ikonica treba za rute
            description = "Routes",
            itemId = "routes"
        ),
        NavigationItem(
            title="Leaderboards",
            icon=Icons.Default.Leaderboard,//neka ikonica treba za leaderboards
            description = "Leaderboards",
            itemId = "leaderboards"
        ),
    )

    val isUserLoggedIn:MutableLiveData<Boolean> =MutableLiveData()

    fun checkForActiveSession(navController: NavController){
        isLoading.value = true
        if(FirebaseAuth.getInstance().currentUser != null){
            Log.d(TAG,"Valid session")

            isUserLoggedIn.value = true
            navController.navigate(Screens.GoogleMap.route) {

                popUpTo(navController.graph.startDestinationId) { inclusive = true }

            }
        }
        else{
            Log.d(TAG,"User is not logged in")
            isUserLoggedIn.value=false

            navController.navigate(Screens.Login.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
        isLoading.value = false
    }

    val emailId:MutableLiveData<String> = MutableLiveData()

    fun getUserData(){
        FirebaseAuth.getInstance().currentUser?.also{
            it.email?.also { email->
                emailId.value=email
            }
        }
    }
}