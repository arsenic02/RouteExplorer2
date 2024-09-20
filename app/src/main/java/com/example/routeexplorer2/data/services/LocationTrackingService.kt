package com.example.routeexplorer2.data.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.routeexplorer2.R
import com.example.routeexplorer2.utils.DefaultLocationClient
import com.example.routeexplorer2.utils.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LocationTrackingService : Service() {

    private val locationClient: LocationClient by lazy {
        DefaultLocationClient(this)
    }

    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null // Ne koristi bind za ovaj servis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        startForeground(
            NOTIFICATION_ID,
            createNotification("Tracking location...")
        )

        // Pokrećemo praćenje lokacije
        job = CoroutineScope(Dispatchers.IO).launch {
            locationClient.getLocationUpdates(1000L)
                .catch { e ->
                    Log.e("LocationTrackingService", "Location update error", e)
                }
                .collect { location ->
                    // Upravljaj promenama lokacije, možeš slati notifikacije ovde
                    updateNotification(location.latitude, location.longitude)
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel() // Zaustavi lokacijska ažuriranja kad se servis zaustavi
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(message: String): Notification {
        val notificationChannelId = "LOCATION_CHANNEL"
        val channel = NotificationChannel(
            notificationChannelId,
            "Location Tracking",
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Location Service")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_search_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return notificationBuilder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateNotification(lat: Double, long: Double) {
        val notification = createNotification("Location: $lat, $long")
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 1
    }
}
