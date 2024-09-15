package com.example.routeexplorer2.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.routeexplorer2.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.awaitMap
//import com.google.api.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

//nema greske, ali ne crta rutu
// Function to draw the route between two markers
//@Composable
//fun drawRoute(map: GoogleMap?, start: LatLng, end: LatLng) {
//    Log.d("Usli smo u drawRoute","!")
//    Log.d("Google map","${map}")
//    Log.d("Start","${start}")
//    Log.d("End","${end}")
//    // Here you can implement the logic to request the route between two points
//    // For example, use Google Directions API to get the route and draw it on the map
//    // Add Polyline to the map to show the route
//    val routePoints = listOf(start, end) // Replace this with actual route points
//
//    val polylineOptions = PolylineOptions().apply {
//        addAll(routePoints)
//        color(R.color.higherDrawerColor)
//        width(10f)
//    }
//
//    // Add the polyline to the map
//    map?.addPolyline(polylineOptions)

//ima sintaksne greske
//fun drawRoute(map: GoogleMap?, start: LatLng, end: LatLng) {
//    val context = LocalContext.current
//
//    // Google Directions API URL
//    val url = getDirectionsUrl(start, end, context.getString(R.string.google_maps_key))
//
//    // Fetch route data from the Google Directions API
//    GlobalScope.launch(Dispatchers.IO) {
//        try {
//            val result = URL(url).readText()
//            val jsonObject = JSONObject(result)
//            val routes = jsonObject.getJSONArray("routes")
//            if (routes.length() > 0) {
//                val points = mutableListOf<LatLng>()
//                val legs = routes.getJSONObject(0).getJSONArray("legs")
//                val steps = legs.getJSONObject(0).getJSONArray("steps")
//                for (i in 0 until steps.length()) {
//                    val polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
//                    points.addAll(decodePolyline(polyline))
//                }
//
//                withContext(Dispatchers.Main) {
//                    // Draw polyline on the map
//                    val polylineOptions = PolylineOptions().addAll(points).width(10f).color(R.color.higherDrawerColor)
//                    map?.addPolyline(polylineOptions)
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("RouteError", "Failed to fetch route: ${e.message}")
//        }
//    }
//}


//fun drawRoute(context: Context, map: GoogleMap?, start: LatLng, end: LatLng) {
//    val routePoints = listOf(start, end)
//    Log.d("Instanca mape: ", "Before coroutine: $map") // Check if map is null here
//    Log.d("Instanca mape: ", "${map}")
//    // Use CoroutineScope properly here if needed, avoid GlobalScope
//    CoroutineScope(Dispatchers.IO).launch {
//        Log.d("Instanca mape: ", "Before coroutine: $map") // Check if map is null here
//        Log.d("Instanca mape: ", "${map}")
//        // Replace this with an actual route calculation, for now, using direct connection
//        val polylineOptions = PolylineOptions().apply {
//            addAll(routePoints)
//            color(ContextCompat.getColor(context, R.color.higherDrawerColor))
//            //color(R.color.higherDrawerColor)
//            width(10f)
//            Log.d("Instanca mape: ", "Before coroutine: $map") // Check if map is null here
//            Log.d("Instanca mape: ", "${map}")
//        }
//
//        withContext(Dispatchers.Main) {
//            // Add the polyline to the map on the main thread
//            Log.d("Instanca mape: ", "${map}")
//            map?.addPolyline(polylineOptions)
//        }
//    }
//}


fun drawRoute(context: Context, map: GoogleMap?, start: LatLng, end: LatLng) {
    val apiKey = context.getString(R.string.google_maps_key)  // Fetch API Key from resources
    val url = getDirectionsUrl(start, end, apiKey)  // Construct the Directions API URL

    // Using CoroutineScope for async tasks
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Fetch the result from the Directions API
            val result = URL(url).readText()
            val jsonObject = JSONObject(result)
            val routes = jsonObject.getJSONArray("routes")

            if (routes.length() > 0) {
                val points = mutableListOf<LatLng>()
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")

                // Decode polyline points
                for (i in 0 until steps.length()) {
                    val polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    points.addAll(decodePolyline(polyline))
                }

                withContext(Dispatchers.Main) {
                    // Draw the polyline on the map (on the main thread)
                    val polylineOptions = PolylineOptions()
                        .addAll(points)
                        .width(10f)
                        .color(context.getColor(R.color.higherDrawerColor)) // Set color

                    map?.addPolyline(polylineOptions)
                }
            } else {
                Log.e("RouteError", "No routes found")
            }
        } catch (e: Exception) {
            Log.e("RouteError", "Failed to fetch route: ${e.message}")
        }
    }
}


fun getDirectionsUrl(start: LatLng, end: LatLng, apiKey: String): String {
    val origin = "origin=${start.latitude},${start.longitude}"
    val destination = "destination=${end.latitude},${end.longitude}"
    val mode = "mode=driving" // You can change this to walking, bicycling, etc.
    return "https://maps.googleapis.com/maps/api/directions/json?$origin&$destination&$mode&key=$apiKey"
}

// Decode polyline points from Directions API response
fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val point = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
        poly.add(point)
    }
    return poly
}



@Composable
fun GoogleMapScreen(onMapReady: (GoogleMap) -> Unit) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val coroutineScope = rememberCoroutineScope()

    // Manage the MapView lifecycle
    DisposableEffect(key1 = mapView) {
        mapView.onCreate(null)
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDestroy()
        }
    }

    // Display the MapView inside Compose
//    AndroidView(factory = { mapView }) { mapView ->
//        LaunchedEffect(mapView) {
//            val googleMap = mapView.awaitMap()  // Await the map to be ready
//            googleMap.setOnMapLoadedCallback {
//                Log.d("MapReady", "GoogleMap is fully loaded and ready")
//                // You can add markers, configure settings, etc. here
//            }
//
//            // Example of setting a marker
//            googleMap.addMarker(
//                MarkerOptions()
//                    .position(LatLng(37.7749, -122.4194)) // San Francisco
//                    .title("Marker in San Francisco")
//            )
//        }
//    }
    AndroidView(
        factory = { mapView },
        update = { mapView ->
            coroutineScope.launch {
                val googleMap = mapView.awaitMap()  // Wait for the map to load
//                googleMap.setOnMapLoadedCallback {
//                    Log.d("MapReady", "GoogleMap is fully loaded and ready")
//                    // You can add markers, configure settings, etc. here
//                }
                onMapReady(googleMap)

                // Example of adding a marker
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(37.7749, -122.4194)) // San Francisco
                        .title("Marker in San Francisco")
                )
            }
        }
    )
}