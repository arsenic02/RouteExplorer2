package com.example.routeexplorer2.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.data.model.Review
import com.example.routeexplorer2.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceViewModel(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    var comment by mutableStateOf("")
    var selectedStars by mutableStateOf(1)

    var selectedPlaceState: Place by mutableStateOf(Place())
        private set

    fun setCurrentPlaceState(place: Place) {
        selectedPlaceState = place
    }

    val selectedPlace: StateFlow<Place?> get() = placeRepository.selectedPlace


    val places = MutableStateFlow<List<Place>>(emptyList()) //ja sam dodao
    fun loadPlace(placeId: String) {
        viewModelScope.launch {
            placeRepository.addPlaceSnapshotListener(placeId)//ovde se poziva PlaceRepository i onda tamo exception
        }
    }

    fun createReview(placeId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val hasReviewed = placeRepository.hasUserReviewed()
            if (!hasReviewed) {
                placeRepository.addReview(placeId, text = comment, rating = selectedStars)
                callback(true)

            } else {
                callback(false)
            }
        }
    }

    fun resetReviewData() {
        comment = ""
        selectedStars = 1
    }


    fun handleLikedReviews(review: Review, isLiked: Boolean) {
        viewModelScope.launch {
            placeRepository.handleLikedReview(review, isLiked)
        }
    }


    override fun onCleared() {
        super.onCleared()
        placeRepository.stopListening()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            places.value = placeRepository.getAllPlaces() // Get all places from repository
        }
    }


}


class PlaceViewModelFactory(
    private val placeRepository: PlaceRepository // Dodano polje za FieldRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(placeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}