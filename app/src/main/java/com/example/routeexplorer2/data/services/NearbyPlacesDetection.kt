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
import com.example.routeexplorer2.MainActivity
import com.example.routeexplorer2.R
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.utils.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

//darkov predlog2
class NearbyPlacesDetection : Service() {

    private lateinit var app: LoginFlowApp
    private lateinit var markers: StateFlow<List<Place?>>
    private lateinit var locationClient: LocationClient // Dodato

    private lateinit var notification: NotificationCompat.Builder

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        app = application as LoginFlowApp
        markers = app.container.markerRepository.markers
        locationClient = app.container.locationClient // Pristupanje instanci DefaultLocationClient


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //ai implementacija radi
    private fun start() {


        val intent = Intent(this, LoginFlowApp::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        notification = NotificationCompat.Builder(this, "location"/*channelId*/)
            .setContentTitle("Searching nearby places")
            .setContentText("There isn't any place nearby")
            .setSmallIcon(R.drawable.ic_search_24)
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.cartoon_detective))
//            )
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        locationClient.getLocationUpdates(1000L)
            .catch { e -> Log.e("Servis", "Location update error", e) }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                observeLocationChanges(LocationData(lat, long))
            }
            .launchIn(serviceScope)


        startForeground(1, notification.build())

        // Start observing location and markers changes
        // observeLocationChanges()
        // observeMarkerChanges()
    }


    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel Coroutine
        serviceScope.cancel()
    }

    private fun observeLocationChanges(userLocation: LocationData) {
        Log.d("NearbyPlacesDetection", "User location updated: $userLocation")
        checkProximityToMarkers(markers.value, userLocation)

    }


    private fun checkProximityToMarkers(markers: List<Place?>, userLocation: LocationData) {
        var nearbyPlaceFound = false
        markers.forEach { marker ->
            marker?.let {
                val markerLocation = LocationData(it.latitude, it.longitude)
                val distance = calculateDistance(userLocation, markerLocation)
                Log.d("NearbyPlacesDetection", "Distance to ${it.name}: $distance meters")
                if (distance < 10_000) { // 10km

                    //prvobitan kod, isto salje obavestenja svake sekunde
//                    if (!nearbyPlaceFound) {//valjda ovde za !nearbyPlaceFound = true,ovde je true, nearbyPlaceFound = false
//                        // Ažurirajte obaveštenje sa statusom da je polje pronađeno
//                        updateNotification(
//                            "Nearby place found: ${it.name}",
//                            notification,
//                            /*isNearbyPlaceFound =*/ true  //prvobitno je bilo odkomentarisano
//                        )
//                        nearbyPlaceFound = true //valjda ovde isNearbyPlaceFound?, a ovde je pre bilo nearbyPlaceFound = true
//                    }
                    //ai kod, radi samo salje beskonacno notifikacija
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
//dodato
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a PendingIntent to be fired when the notification is clicked
        val pendingIntent = PendingIntent.getActivity(
            this, // Context
            0, // Request code (can be any integer)
            intent, // The intent to open your app
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Flags to update the intent and for API level restrictions
        )
        //dodato

        // Ažurirajte obaveštenje
        notification
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_search_24)
            .setOngoing(true)

            //dodato
            .setContentIntent(pendingIntent) // Set the PendingIntent so the app opens when clicked
            .setAutoCancel(true) // Dismiss the notification when clicked
        //dodato

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

        Log.d("NearbyPlacesDetection", "Notification updated: $message")
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val TAG = "NearbyPlacesDetection"
    }
}

//darkov predlog broj 1, stize isto  obavestenje na svake 2 sekundi i  ne radi sta treba
/*
class NearbyPlacesDetection : Service() {

    private lateinit var app: LoginFlowApp
    private lateinit var markers: StateFlow<List<Place?>>
    private lateinit var locationClient: LocationClient // Dodato

    private lateinit var notification: NotificationCompat.Builder

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        app = application as LoginFlowApp
        markers = app.container.markerRepository.markers
        locationClient = app.container.locationClient // Pristupanje instanci DefaultLocationClient


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //ai implementacija radi
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
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.cartoon_detective))
//            )
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        locationClient.getLocationUpdates(1000L)
            .catch { e -> Log.e("Servis", "Location update error", e) }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                observeLocationChanges(LocationData(lat, long))
            }
            .launchIn(serviceScope)


        startForeground(1, notification.build())

        // Start observing location and markers changes
        // observeLocationChanges()
        // observeMarkerChanges()
    }


    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel Coroutine
        serviceScope.cancel()
    }

    private fun observeLocationChanges(userLocation: LocationData) {
        Log.d("NearbyPlacesDetection", "User location updated: $userLocation")
        checkProximityToMarkers(markers.value, userLocation)

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
//        val pictureResId = if (isNearbyPlaceFound) {
//            R.drawable.found
//        } else {
//            R.drawable.cartoon_detective
//        }

        // Ažurirajte obaveštenje
        notification
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_search_24)
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(BitmapFactory.decodeResource(resources, pictureResId))
//            )
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

        Log.d("NearbyPlacesDetection", "Notification updated: $message")
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val TAG = "NearbyPlacesDetection"
    }
}
*/


//implementacija koja je radila, samo ne radi kad se pokrene aplikacija
/*
class NearbyPlacesDetection : Service() {

    private lateinit var app: LoginFlowApp
    private lateinit var currentUserLocation: StateFlow<LocationData?>
    private lateinit var markers: StateFlow<List<Place?>>

    private lateinit var locationClient: LocationClient // Dodato

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

        //dodato
        locationClient = app.container.locationClient // Pristupanje instanci DefaultLocationClient

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //ai implementacija radi
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
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.cartoon_detective))
//            )
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        startForeground(1, notification.build())

        // Start observing location and markers changes
        observeLocationChanges()
        observeMarkerChanges()
    }
//prvobitna implementacija ovaj kod iznad

    //servis bez viewModela
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
////            .setStyle(
////                NotificationCompat
////                    .BigPictureStyle()
////                    .bigPicture(
////                        BitmapFactory.decodeResource(resources, R.drawable.cartoon_detective)
////                    )
////            )
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
//        val pictureResId = if (isNearbyPlaceFound) {
//            R.drawable.found
//        } else {
//            R.drawable.cartoon_detective
//        }

        // Ažurirajte obaveštenje
        notification
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_search_24)
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(BitmapFactory.decodeResource(resources, pictureResId))
//            )
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

        Log.d("NearbyPlacesDetection", "Notification updated: $message")
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val TAG = "NearbyPlacesDetection"
    }
}*/