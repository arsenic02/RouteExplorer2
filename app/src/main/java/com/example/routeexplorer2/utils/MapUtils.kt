package com.example.routeexplorer2.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

object MapUtils {

    fun addPolyline(googleMap: GoogleMap, points: List<LatLng>, tag: String = ""): Polyline {
        val polyline = googleMap.addPolyline(
            PolylineOptions().clickable(true).addAll(points)
        )
        polyline.tag = tag
        stylePolyline(polyline)
        return polyline
    }

    private fun stylePolyline(polyline: Polyline) {
        val type = polyline.tag?.toString() ?: ""
        polyline.color = when (type) {
            "A" -> -0x1000000 // Black color
            else -> -0x657db // Default color
        }
        polyline.width = 12f
        polyline.jointType = com.google.android.gms.maps.model.JointType.ROUND
    }
}


