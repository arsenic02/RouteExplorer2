package com.example.routeexplorer2

import android.app.Application
import com.example.routeexplorer2.data.DefaultAppContainer
import com.example.routeexplorer2.data.repository.UserRepository
import com.google.firebase.FirebaseApp

class LoginFlowApp :Application(){

    lateinit var container: DefaultAppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = DefaultAppContainer(this)
    }
}

//class DefaultAppContainer(application: Application) {
//    val userRepository: UserRepository = UserRepository(application)
//}