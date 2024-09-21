package com.example.routeexplorer2.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.routeexplorer2.data.repository.MarkerRepository
import com.example.routeexplorer2.data.repository.PlaceRepository
import com.example.routeexplorer2.data.repository.UserRepository
import com.example.routeexplorer2.utils.DefaultLocationClient
import com.example.routeexplorer2.utils.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class DefaultAppContainer(context:Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    val userRepository: UserRepository by lazy {
        UserRepository(auth, firestore, storage)
    }

    val markerRepository: MarkerRepository by lazy {
        MarkerRepository(auth,firestore, storage)
    }

    val placeRepository: PlaceRepository by lazy {
        PlaceRepository(auth, firestore)
    }

    //dodato i ovo za pracenje mimo viewModela
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context.applicationContext)
    }

    val locationClient: LocationClient by lazy {
        DefaultLocationClient(context.applicationContext/*,fusedLocationProviderClient*/)
    }

    init {
        createNotificationChannel(context)
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "location"
            val channelName = "Location"
            val importance = NotificationManager.IMPORTANCE_LOW

            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            val notificationManager: NotificationManager =
                // Ako ovo koristis u Application, radice i bez context. jer je implicitno prisutan (this.getSystemService) ali ti to ne vidis
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }


}

//prva implementacija, moja radi dobro, osim prvi put kad se aplikacija upali
/*
class DefaultAppContainer(context:Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    val userRepository: UserRepository by lazy {
        UserRepository(auth, firestore, storage)
    }

    val markerRepository: MarkerRepository by lazy {
        MarkerRepository(auth,firestore, storage)
    }

        val placeRepository: PlaceRepository by lazy {
        PlaceRepository(auth, firestore)
    }

    //dodato i ovo za pracenje mimo viewModela
private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
    LocationServices.getFusedLocationProviderClient(context.applicationContext)
}

    val locationClient: LocationClient by lazy {
        DefaultLocationClient(context.applicationContext/*,fusedLocationProviderClient*/)
    }

//    init {
//        createNotificationChannel(context)
//    }
//
//
//    private fun createNotificationChannel(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "location"
//            val channelName = "Location"
//            val importance = NotificationManager.IMPORTANCE_LOW
//
//            val notificationChannel = NotificationChannel(channelId, channelName, importance)
//            val notificationManager: NotificationManager =
//                // Ako ovo koristis u Application, radice i bez context. jer je implicitno prisutan (this.getSystemService) ali ti to ne vidis
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//    }


}*/