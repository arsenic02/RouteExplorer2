package com.example.routeexplorer2.data.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.routeexplorer2.data.NavigationItem
import com.example.routeexplorer2.data.signup.SignupViewModel
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

//ovo je zapravo za drawer
class HomeViewModel: ViewModel() {
    private val TAG = HomeViewModel::class.simpleName

    val navigationItemsList = listOf<NavigationItem>(
        NavigationItem(
            title="Home",
            icon=Icons.Default.Map,
            description = "Home Screen",
            itemId = "homeScreen"
        ),
        NavigationItem(
            title="Routes",
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

    fun checkForActiveSession(){
        if(FirebaseAuth.getInstance().currentUser != null){
            Log.d(TAG,"Valid session")

            //dodao sam ja
            isUserLoggedIn.value = true
            RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
        }
        else{
            Log.d(TAG,"User is not logged in")
            isUserLoggedIn.value=false

            //dodao sam ja
            RouterExplorerAppRouter.navigateTo(Screen.SignUpScreen)
        }
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