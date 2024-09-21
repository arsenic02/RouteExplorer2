package com.example.routeexplorer2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.User
import com.example.routeexplorer2.data.repository.UserRepository
import com.example.routeexplorer2.utils.LocationClient
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val locationClient: LocationClient
) : ViewModel() {

    val allUsers: StateFlow<List<User>> = userRepository.allUsers

    val currentUser: StateFlow<User?> = userRepository.currentUser

    val currentUserLocation: StateFlow<LocationData?> = userRepository.currentUserLocation


    init {
        loadCurrentUser()
        loadAllUsers()
    }

    fun isUserLoggedIn() : Boolean {
        return userRepository.isUserLoggedIn()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            userRepository.startObservingUser()
        }
    }


    private fun loadAllUsers() {
        viewModelScope.launch {
            userRepository.fetchAllUsers()
        }
    }

    fun updateLocation() {
        viewModelScope.launch {
            locationClient.getLocationUpdates(1000L)
                .catch { e -> Log.e("UserViewModel", "Location update error", e) }
                .onEach { location ->
                    val lat = location.latitude
                    val long = location.longitude
                    userRepository.updateLocationData(LocationData(lat, long))
                }
                .launchIn(viewModelScope)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Stop listening when ViewModel is cleared
        userRepository.stopFetchAllUsers()
        userRepository.stopObservingUser()
    }
}

class UserViewModelFactory(
    private val userRepository: UserRepository,
    private val locationClient: LocationClient
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository, locationClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

