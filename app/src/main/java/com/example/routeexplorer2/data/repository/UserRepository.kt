package com.example.routeexplorer2.data.repository

import android.net.Uri
import android.util.Log
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class UserRepository (

    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
){
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private var userListenerRegistration: ListenerRegistration? = null


    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    private var allUserslistenerRegistration: ListenerRegistration? = null

    private val _currentUserLocation = MutableStateFlow<LocationData?>(null)
    val currentUserLocation: StateFlow<LocationData?> = _currentUserLocation

    fun updateLocationData(locationData: LocationData) {
        _currentUserLocation.value = locationData
    }


    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        try {
            // Sign in with email and password
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await() // Wait for authentication result

            if (authResult.user != null) {
                startObservingUser()
                callback(true)
            } else {
                callback(false)
            }

        } catch (e: Exception) {
            // Handle errors
            e.printStackTrace()
            callback(false)
        }

    }

    suspend fun registerUser(
        email: String,
        password: String,
        username: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        imageUri: Uri?,
        callback: (Boolean) -> Unit
    ) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return callback(false)

            Log.d("UserRepository authresult","$authResult")
            Log.d("UserRepository userId","$authResult")
            // Ovo si mogao sa User class da instanciras, rekli su na firestore https://firebase.google.com/docs/firestore/manage-data/add-data
            val user = mapOf(
                "id" to userId,
                "email" to email,
                "username" to username,
                "firstName" to firstName,
                "lastName" to lastName,
               "phoneNumber" to phoneNumber,
                "score" to 0,
                "likedReviews" to mutableListOf<String>(),
                "photoPath" to ""
            )

          firestore.collection("users").document(userId).set(user).await()

            imageUri?.let {
                val profilePicRef = storage.getReference("profile_pictures/$userId")
                profilePicRef.putFile(it).await()
                val downloadUri = profilePicRef.downloadUrl.await()
                firestore.collection("users").document(userId)
                    .update("photoPath", downloadUri.toString()).await()
            }

            callback(true)
        } catch (e: Exception) {
            Log.d("UserRepository catch","Evo me u catch")
            callback(false)
        }
    }

    fun isUserLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun startObservingUser()  {
        val userId = auth.currentUser?.uid ?: return
        val userDocRef = firestore.collection("users").document(userId)

        userListenerRegistration = userDocRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("UserRepository", "Error fetching user data", exception)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                _currentUser.value = user

                Log.i("UserRepository", "User data updated: $user")

            } else {
                _currentUser.value = null
            }
        }
    }

    fun stopObservingUser() {
        userListenerRegistration?.remove()
    }



    // Dobro je
    fun fetchAllUsers() {
        allUserslistenerRegistration = firestore.collection("users")
            .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("UserRepository", "Error fetching users", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    // Convert query snapshot to list of users
                    val users = snapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }
                    // Update StateFlow with the list of users
                    _allUsers.value = users
                }
            }
    }

    fun stopFetchAllUsers() {
        allUserslistenerRegistration?.remove()
    }


    fun signOut(callback: (Boolean) -> Unit) {
        try {
            auth.signOut()
            stopObservingUser()
            callback(true)
        } catch (e: Exception) {
            // Log error or handle failure
            callback(false)
        }
    }
}