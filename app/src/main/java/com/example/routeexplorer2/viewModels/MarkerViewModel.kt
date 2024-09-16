
package com.example.routeexplorer2.viewModels

//import com.google.android.gms.maps.model.LatLng

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.data.repository.MarkerRepository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarkerViewModel(private val markerRepository: MarkerRepository) : ViewModel() {

    var name by mutableStateOf("")
    var lat: Double by mutableStateOf(0.0)
    var lng: Double by mutableStateOf(0.0)
    var address: String by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null)

    var filteredRadius by mutableStateOf<Int?>(null)
    var dateRange by mutableStateOf("")

    val markers: StateFlow<List<Place>> = markerRepository.markers

    // Function to create a marker and save it to Firestore
    fun createMarker(callback: (Boolean, String) -> Unit) {
        if (!isValidName(name)) {
            callback(false, "Name cannot be empty")
            return
        }

        viewModelScope.launch {
            markerRepository.addMarker(
                name = name,
                lat = lat,
                lng = lng,
                address = address,
                photo = imageUri,
                callback = callback
            )
            resetState()
        }
    }

    // Fetch markers from Firestore
    fun fetchMarkers() {
        viewModelScope.launch {
            markerRepository.fetchMarkers()
        }
    }

    fun setLatLng(latLng: LatLng) {
        lat = latLng.latitude
        lng = latLng.longitude
    }

    fun resetState() {
        name = ""
        lat = 0.0
        lng = 0.0
        address = ""
        imageUri = null
    }

    fun resetFilter(){

    }

    fun isValidName(name: String): Boolean = name.isNotBlank()
}

class MarkerViewModelFactory(
    private val markerRepository: MarkerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarkerViewModel::class.java)) {
            return MarkerViewModel(markerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}