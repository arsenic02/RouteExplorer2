package com.example.routeexplorer2.data.services

import android.os.Build
import androidx.annotation.RequiresApi

interface NearbyPlacesDetectionController {
    @RequiresApi(Build.VERSION_CODES.O)
    fun startNearbyPlacesDetectionService() //u homeScreen se poziva

    @RequiresApi(Build.VERSION_CODES.O)
    fun stopNearbyPlacesDetectionService()
}