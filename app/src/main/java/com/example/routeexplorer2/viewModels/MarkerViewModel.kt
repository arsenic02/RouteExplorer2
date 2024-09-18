
package com.example.routeexplorer2.viewModels

//import com.google.android.gms.maps.model.LatLng

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routeexplorer2.R
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

    var selectedOption by mutableStateOf("Run")
    val markers: StateFlow<List<Place>> = markerRepository.markers

    var filteredName by mutableStateOf("")
    var filteredSelectedOption by mutableStateOf("Any Type")
    var filteredRadius by mutableStateOf<Int?>(null)
    var dateRange by mutableStateOf("")

    val filteredMarkers: StateFlow<List<Place>> = markerRepository.filteredMarkers

    // Function to create a marker and save it to Firestore
    fun createMarker(callback: (Boolean, String) -> Unit) {
        if (!isValidName(name)) {
            callback(false, "Name cannot be empty")
            return
        }
        Log.d("MarkerViewModel", "Starting marker creation")
        val iconResId = when (selectedOption) {
            "Run" -> R.drawable.ic_run_24   //ic_car_24 opet on crta sam run ikonu
            "Bike" -> R.drawable.baseline_directions_bike_24
            "Car" -> R.drawable.ic_car_24
            else -> R.drawable.ic_car_24 // A default marker icon if needed
        }
        viewModelScope.launch {
            markerRepository.addMarker(
                name = name,
                lat = lat,
                lng = lng,
                address = address,
                photo = imageUri,
                selectedOption=selectedOption,
                iconResId=iconResId,//dodato
                callback = callback,
                currentTime =  Timestamp.now()
            )
//            callback(true, "Successfully added marker") //mislim da nije od znacaja za id
            Log.d("MarkerViewModel", "Marker creation process finished")
//            resetState()
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
        selectedOption="Run"
        lat = 0.0
        lng = 0.0
        address = ""
        imageUri = null
    }

    fun resetFilter(){
        filteredName = ""
        filteredSelectedOption = "Any Type"
        filteredRadius = 0
        dateRange = ""
    }

    fun applyFilters(currentUserLocationData: LocationData?, cb: (Boolean) -> Unit) {

        viewModelScope.launch {
            markerRepository.applyFilters(callback = cb,filteredName, filteredSelectedOption, dateRange, filteredRadius, currentUserLocationData)
        }
    }

    fun removeFilters() {
        viewModelScope.launch {
            markerRepository.removeFilters()
        }
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