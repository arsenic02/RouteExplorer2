package com.example.routeexplorer2.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel : ViewModel() {

    // Drži referencu na instancu Google Mape
    private var googleMap: GoogleMap? = null

    // Lista markera
    private val _markerList = mutableStateListOf<Marker>()
    val markerList: List<Marker> = _markerList

    fun setGoogleMap(map: GoogleMap) {
        googleMap = map
    }

    fun addMarker(latLng: LatLng) {
        googleMap?.let { map ->
            if (_markerList.size >= 2) {
                // Ukloni prvi marker
                _markerList[0].remove()
                _markerList.removeAt(0)
            }

            val marker = map.addMarker(MarkerOptions().position(latLng))
            marker?.let { _markerList.add(it) }

            if (_markerList.size == 2) {
                drawRoute(_markerList[0].position, _markerList[1].position)
            }
        }
    }

    private fun drawRoute(start: LatLng, end: LatLng) {
        // Implementiraj logiku crtanja rute
    }
}
