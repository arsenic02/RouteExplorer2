package com.example.routeexplorer2.data.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.routeexplorer2.LoginFlowApp
import com.example.routeexplorer2.R
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NearbyPlacesDetection : Service() {

    private lateinit var app: LoginFlowApp
    private lateinit var currentUserLocation: StateFlow<LocationData?>
    private lateinit var markers: StateFlow<List<Place?>>

    private lateinit var notification: NotificationCompat.Builder

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        app = application as LoginFlowApp
        currentUserLocation = app.container.userRepository.currentUserLocation
        markers = app.container.markerRepository.markers


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "location"

        // Kreiranje Notification Channel-a za Android 8.0 i novije
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Tracking",
                NotificationManager.IMPORTANCE_HIGH//IMPORTANCE_LOW
            ).apply {
                description = "Channel for tracking nearby places"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, LoginFlowApp::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Searching nearby places")
            .setContentText("There isn't any place nearby")
            .setSmallIcon(R.drawable.ic_search_24)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.cartoon_detective))
            )
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        startForeground(1, notification.build())

        // Start observing location and markers changes
        observeLocationChanges()
        observeMarkerChanges()
    }
//prvobitna implementacija
//    private fun start() {
//        val intent = Intent(this, LoginFlowApp::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        notification = NotificationCompat.Builder(this, "location")
//            .setContentTitle("Searching nearby places")
//            .setContentText("There isn't any place nearby")
//            .setSmallIcon(R.drawable.ic_search_24)
//            .setStyle(
//                NotificationCompat
//                    .BigPictureStyle()
//                    .bigPicture(
//                        BitmapFactory.decodeResource(resources, R.drawable.cartoon_detective)
//                    )
//            )
//            .setOngoing(true)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        startForeground(1, notification.build())
//        // Start observing location and markers changes
//        observeLocationChanges()
//        observeMarkerChanges()
//    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel Coroutine
        serviceScope.cancel()
    }

    private fun observeLocationChanges() {
        serviceScope.launch {
            currentUserLocation.collect { userLocation ->
                userLocation?.let { location ->
                    // Fetch markers from the StateFlow and check proximity
                    Log.d("NearbyPlacesDetection", "User location updated: $location")
                    checkProximityToMarkers(markers.value, location)
                }
            }
        }
    }

    private fun observeMarkerChanges() {
        serviceScope.launch {
            markers.collect { markerList ->
                Log.d("NearbyPlacesDetection", "Markers updated: $markerList")
                // Fetch current location and check proximity to new markers
                currentUserLocation.value?.let { userLocation ->
                    checkProximityToMarkers(markerList, userLocation)
                }
            }
        }
    }

    private fun checkProximityToMarkers(markers: List<Place?>, userLocation: LocationData) {
        var nearbyPlaceFound = false
        markers.forEach { marker ->
            marker?.let {
                val markerLocation = LocationData(it.latitude, it.longitude)
                val distance = calculateDistance(userLocation, markerLocation)
                Log.d("NearbyPlacesDetection", "Distance to ${it.name}: $distance meters")
                if (distance < 10_000) { // 10km

                    //prvobitan kod
//                    if (!nearbyPlaceFound) {//valjda ovde za !nearbyPlaceFound = true,ovde je true, nearbyPlaceFound = false
//                        // Ažurirajte obaveštenje sa statusom da je polje pronađeno
//                        updateNotification(
//                            "Nearby place found: ${it.name}",
//                            notification,
//                            /*isNearbyPlaceFound =*/ true  //prvobitno je bilo odkomentarisano
//                        )
//                        nearbyPlaceFound = true //valjda ovde isNearbyPlaceFound?, a ovde je pre bilo nearbyPlaceFound = true
//                    }
                    //ai kod
                    if (!nearbyPlaceFound) {
                        updateNotification("Nearby place found: ${it.name}", notification, true)
                        nearbyPlaceFound = true
                    }

                }
            }
        }
        if (!nearbyPlaceFound) {//ovde je valjda nearbyPlaceFound=false, odnosnoo samo nearByPlaceFound
            // Ažurirajte obaveštenje sa statusom da polje nije pronađeno
            updateNotification(
                "There isn't any place nearby",
                notification,
                isNearbyPlaceFound = false
            )
        }
    }

    private fun calculateDistance(location1: LocationData, location2: LocationData): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            location1.latitude,
            location1.longitude,
            location2.latitude,
            location2.longitude,
            results
        )
        return results[0]
    }

    private fun updateNotification(
        message: String,
        notification: NotificationCompat.Builder,
        isNearbyPlaceFound: Boolean
    ) {
        val pictureResId = if (isNearbyPlaceFound) {
            R.drawable.found
        } else {
            R.drawable.cartoon_detective
        }

        // Ažurirajte obaveštenje
        notification
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_search_24)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(resources, pictureResId))
            )
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

        Log.d("NearbyPlacesDetection", "Notification updated: $message")
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val TAG = "NearbyFieldsDetection"
    }
}