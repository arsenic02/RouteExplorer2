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
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

//ovo je zapravo za drawer
class HomeViewModel: ViewModel() {
    private val TAG = HomeViewModel::class.simpleName
    val isLoading = MutableLiveData(true)  // Initialize as loading

    val navigationItemsList = listOf<NavigationItem>(
//        NavigationItem(
//            title="Home",
//            icon=Icons.Default.Map,
//            description = "Home Screen",
//            itemId = "homeScreen"
//        ),
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
    fun logout(){
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()
        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign out complete")
                RouterExplorerAppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out NOT completed")
            }
        }

        firebaseAuth.addAuthStateListener (authStateListener)
    }

    fun checkForActiveSession(navController: NavController){
        isLoading.value = true
        if(FirebaseAuth.getInstance().currentUser != null){
            Log.d(TAG,"Valid session")

            //dodao sam ja
            isUserLoggedIn.value = true
            navController.navigate(Screens.GoogleMap.route) {

                popUpTo(navController.graph.startDestinationId) { inclusive = true }

            }
           // RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
        }
        else{
            Log.d(TAG,"User is not logged in")
            isUserLoggedIn.value=false

            //dodao sam ja
           // RouterExplorerAppRouter.navigateTo(Screen.SignUpScreen)
            navController.navigate(Screens.Login.route) {
                // Optional: Clear the back stack if needed
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
               // popUpTo(0)
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
//       if( FirebaseAuth.getInstance().currentUser!=null) {
//
//       }
    }
}