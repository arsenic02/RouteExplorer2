package com.example.routeexplorer2.data.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.routeexplorer2.R
import com.example.routeexplorer2.utils.DefaultLocationClient
import com.example.routeexplorer2.utils.LocationClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

//servis
//class LocationTrackerService: Service() {
//    companion object {//dodato
//        const val ACTION_START = "ACTION_START"
//        const val ACTION_STOP = "ACTION_STOP"
//    }
//
//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private lateinit var locationClient: LocationClient
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        locationClient = DefaultLocationClient(
//            applicationContext,
//            LocationServices.getFusedLocationProviderClient(applicationContext)
//        )
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        //start()
//        when (intent?.action) {
//            ACTION_START -> start()
//            ACTION_STOP -> stop()
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    private fun start() {
//        val notification = NotificationCompat.Builder(this, "locationservicechannel")
//            .setContentTitle("Tracking location...")
//            .setContentText("Location: null")
//            .setSmallIcon(R.drawable.ic_search_24)
//            .setOngoing(true)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        locationClient
//            .getLocationUpdates(1000L)
//            .catch { e -> e.printStackTrace() }
//            .onEach { location ->
//                Log.d("SERVICE", location.toString())
//                val lat = location.latitude.toString()
//                val long = location.longitude.toString()
//                val updatedNotification = notification.setContentText(
//                    "Location: ($lat, $long)"
//                )
//                notificationManager.notify(1, updatedNotification.build())
//            }
//            .launchIn(serviceScope)
//
//        Log.d("LOCATION SERVICE", "Service started.")
//        startForeground(1, notification.build())
//    }
//
//    private fun stop() {
//        Log.d("LOCATION SERVICE", "Service stopped.")
//        stopForeground(STOP_FOREGROUND_REMOVE)
//        stopSelf()
//    }
//
//    override fun stopService(name: Intent?): Boolean {
//        stop()
//        return super.stopService(name)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        serviceScope.cancel()
//    }
//}
//servis

//cahtgpt
//class LocationTrackingService : Service() {
//
//    private val locationClient: LocationClient by lazy {
//        DefaultLocationClient(this)
//    }
//
//    private var job: Job? = null
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null // Ne koristi bind za ovaj servis
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate() {
//        super.onCreate()
//
//        startForeground(
//            NOTIFICATION_ID,
//            createNotification("Tracking location...")
//        )
//
//        // Pokrećemo praćenje lokacije
//        job = CoroutineScope(Dispatchers.IO).launch {
//            locationClient.getLocationUpdates(1000L)
//                .catch { e ->
//                    Log.e("LocationTrackingService", "Location update error", e)
//                }
//                .collect { location ->
//                    // Upravljaj promenama lokacije, možeš slati notifikacije ovde
//                    updateNotification(location.latitude, location.longitude)
//                }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        job?.cancel() // Zaustavi lokacijska ažuriranja kad se servis zaustavi
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotification(message: String): Notification {
//        val notificationChannelId = "LOCATION_CHANNEL"
//        val channel = NotificationChannel(
//            notificationChannelId,
//            "Location Tracking",
//            NotificationManager.IMPORTANCE_LOW
//        )
//
//        val notificationManager = getSystemService(NotificationManager::class.java)
//        notificationManager.createNotificationChannel(channel)
//
//        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
//            .setContentTitle("Location Service")
//            .setContentText(message)
//            .setSmallIcon(R.drawable.ic_search_24)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//
//        return notificationBuilder.build()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun updateNotification(lat: Double, long: Double) {
//        val notification = createNotification("Location: $lat, $long")
//        val notificationManager = getSystemService(NotificationManager::class.java)
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }
//
//    companion object {
//        const val NOTIFICATION_ID = 1
//    }
//}
