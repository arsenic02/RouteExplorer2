package com.example.routeexplorer2.data.repository

import android.net.Uri
import android.util.Log
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.pow

//class MarkerRepository (
//    private val auth: FirebaseAuth,
//    private val firestore: FirebaseFirestore,
//    private val storage: FirebaseStorage
//) {
//    private val _markers = MutableStateFlow<List<Place>>(emptyList())
//    val markers: StateFlow<List<Place>> = _markers
//
//    private var markersListenerRegistration: ListenerRegistration? = null
//
//
//    private val _filteredMarkers = MutableStateFlow<List<Place>>(emptyList())
//    val filteredMarkers: StateFlow<List<Place>> = _filteredMarkers.asStateFlow()
//
//
//    init {
//        fetchMarkers()
//    }
//
//
//    private fun fetchMarkers() {
//        val markersCollectionRef = firestore.collection("markers")
//
//        // Dodajte listener za promene u kolekciji
//        markersListenerRegistration =
//            markersCollectionRef.addSnapshotListener { snapshot, exception ->
//                if (exception != null) {
//                    // Handle error
//                    return@addSnapshotListener
//                }
//
//                // Mapirajte rezultate u listu Marker
//                val updatedMarkers = snapshot?.documents?.mapNotNull { document ->
//                    document.toObject(Place::class.java)
//                } ?: emptyList()
//
//                // Ažurirajte _markers sa novim podacima
//                _markers.value = updatedMarkers
//            }
//
//    }

    // Funkcija za uklanjanje listener-a
//    fun removeMarkersListener() {
//        markersListenerRegistration?.remove()
//    }


    // Funkcija za dodavanje LocationData u kolekciju markers s automatski generisanim ID-om
//funkcija po defaultu
//    suspend fun addLocationData(
//        name: String,
//        type: String,
//        address: String,
//        lng: Double,
//        lat: Double,
//        photo: Uri?,
//        currentTime: Timestamp
//    ) {
//        val userId = auth.currentUser!!.uid
//
//        try {
//
//            val userRef = firestore.collection("users").document(userId).get().await()
//            val user = userRef.toObject(User::class.java)
//                ?: User() // Ako ne postoji korisnik, koristi praznog User-a
//
//            val fieldData = Place(
//                "",
//                name,
//                type,
//                address,
//                lng,
//                lat,
//                //mutableListOf(),
//                0.0,
//                0,
//                "",
//                currentTime,
//                user.username
//            )
//            // Dodaj dokument i automatski generiši ID
//            val documentRef = firestore.collection("markers").add(fieldData).await()
//
//            // Uzmi generisani ID
//            val fieldId = documentRef.id
//
//            // Ako postoji slika, sačuvaj je u Firebase Storage
//            if (photo != null) {
//                val fieldPicRef = storage.getReference("field_pictures/$fieldId")
//                fieldPicRef.putFile(photo).await()  // Čeka da se slika upload-uje
//                val downloadUri = fieldPicRef.downloadUrl.await()  // Čeka da se preuzme URL slike
//
//                // Ažuriraj Firestore dokument sa URL-om slike i ID-jem
//                documentRef.update(
//                    mapOf(
//                        "photo" to downloadUri.toString(),
//                        "id" to fieldId
//                    )
//                ).await()
//            } else {
//                // Ako slika nije dostupna, samo ažuriraj ID
//                documentRef.update("id", fieldId).await()
//            }
//
//            // Ažuriraj score korisnika
//            val userDocRef = firestore.collection("users").document(userId)
//            userDocRef.update("score", FieldValue.increment(20)).await()
//        } catch (e: Exception) {
//            Log.e("MarkerRepository", "Error writing LocationData", e)
//        }
//    }

    //funkcija chatgpt
    class MarkerRepository(
        private val firestore: FirebaseFirestore,
        private val storage: FirebaseStorage
    ) {

        private val _markers = MutableStateFlow<List<Place>>(emptyList())
        val markers: StateFlow<List<Place>> = _markers

        // Add marker to Firestore
        suspend fun addMarker(
            name: String,
            lat: Double,
            lng: Double,
            address: String,
            photo: Uri?,
            callback: (Boolean, String) -> Unit
        ) {
            try {
                val markerData = hashMapOf(
                    "name" to name,
                    "lat" to lat,
                    "lng" to lng,
                    "address" to address,
                    "timestamp" to Timestamp.now()
                )

                // Save marker data to Firestore
                firestore.collection("markers").add(markerData)
                    .addOnSuccessListener {
                        callback(true, "Marker added successfully")
                    }
                    .addOnFailureListener {
                        callback(false, "Failed to add marker: ${it.message}")
                    }
            } catch (e: Exception) {
                callback(false, "Exception: ${e.message}")
            }
        }

        // Fetch markers from Firestore
        fun fetchMarkers() {
            firestore.collection("markers").get()
                .addOnSuccessListener { result ->
                    val markerList = result.map { document ->
                        Place(
                            name = document.getString("name") ?: "",
                            latitude = document.getDouble("lat") ?: 0.0,
                            longitude = document.getDouble("lng") ?: 0.0,
                            address = document.getString("address") ?: ""
                        )
                    }
                    _markers.value = markerList
                }
                .addOnFailureListener { exception ->
                    Log.e("MarkerRepository", "Error fetching markers: ${exception.message}")
                }
        }
    }

