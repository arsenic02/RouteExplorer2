package com.example.routeexplorer2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.routeexplorer2.data.DefaultAppContainer
import com.example.routeexplorer2.data.repository.UserRepository
import com.google.firebase.FirebaseApp

class LoginFlowApp :Application(){

    lateinit var container: DefaultAppContainer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = DefaultAppContainer(this)

        //dodato za pracenje lokacije direktno iz servisa
//        val channel = NotificationChannel(
//            "locationservicechannel",
//            "Location",
//            NotificationManager.IMPORTANCE_LOW
//        )
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
        //dodato za pracenje lokacije direktno iz servisa
    }
}

//class DefaultAppContainer(application: Application) {
//    val userRepository: UserRepository = UserRepository(application)
//}