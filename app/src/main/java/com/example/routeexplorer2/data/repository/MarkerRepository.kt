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
import com.google.firebase.storage.FirebaseStorage
import com.google.maps.android.compose.Marker
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

class MarkerRepository(
        private val auth: FirebaseAuth,
        private val firestore: FirebaseFirestore,
        private val storage: FirebaseStorage
    ) {

        private val _markers = MutableStateFlow<List<Place>>(emptyList())
        val markers: StateFlow<List<Place>> = _markers

    private val _filteredMarkers = MutableStateFlow<List<Place>>(emptyList())
    val filteredMarkers: StateFlow<List<Place>> = _filteredMarkers.asStateFlow()

//chatgpt
suspend fun addMarker(
    name: String,
    lat: Double,
    lng: Double,
    address: String,
    photo: Uri?, // The photo parameter
    iconResId: Int,
    selectedOption: String,
    callback: (Boolean, String) -> Unit,
    currentTime: Timestamp
) {
    val userId = auth.currentUser!!.uid//dodato
    Log.d("userId", "${userId}")
    try {

        //dodato userRef i user
        val userRef = firestore.collection("users").document(userId).get().await()
        val user = userRef.toObject(User::class.java)
            ?: User() // Ako ne postoji korisnik, koristi praznog User-a

        //Log.d("MarkerRepository", "Author: ${user.username}")

        //ovako je bilo
        val markerData = hashMapOf(
            "name" to name,
            "lat" to lat,
            "lng" to lng,
            "address" to address,
            "iconResId" to iconResId,
            "selectedOption" to selectedOption,
            "timestamp" to currentTime, // Use provided currentTime
            "author" to user.username
        )

        Log.d("MarkerRepository", "Marker Data: $markerData")
        Log.d("MarkerRepository", "Author: ${user.username}")


//        val markerData = Place(
//            "",
//            name,
//            selectedOption,
//            "",
//            lng,
//            lat,
//            selectedOption,
//            iconResId,
//            mutableListOf(),
//            0.0,
//            0,
//            "",
//            currentTime,
//            user.username
//        )

        // Save marker data to Firestore and get a reference to the newly added document
        val documentRef = firestore.collection("markers").add(markerData).await()

        val placeId = documentRef.id // Get the document ID

        if (photo != null) {
            // If there's a photo, upload it to Firebase Storage
            val storageRef = storage.getReference("place_pictures/$placeId")
            storageRef.putFile(photo).await() // Upload the photo
            val downloadUri = storageRef.downloadUrl.await() // Get the download URL

            // Update Firestore document with photo URL
            documentRef.update(
                mapOf(
                    "photo" to downloadUri.toString(),
                    "id" to placeId,
                    "author" to user.username
                )
            ).await()
        }
        //pojavi se neko obavestenje bezveze, ali se kreira lepo
//        val userDocRef = firestore.collection("users").document(userId)//dodato
//        userDocRef.update("score", FieldValue.increment(20)).await()//dodato

        callback(true, "Marker added successfully with ID: $placeId")
    } catch (e: Exception) {
        callback(false, "Exception: ${e.message}")
    }
}



    //moja metoda, dodaje ikonice u bazu i prikazuje odgovarajuce ikonice,
    //jedino ne pamti slike i id dokumenta
//    suspend fun addMarker(
//        name: String,
//        lat: Double,
//        lng: Double,
//        address: String,
//        photo: Uri?,
//        iconResId:Int,//dodato
//        selectedOption: String,
//        callback: (Boolean, String) -> Unit,
//        currentTime: Timestamp
//    ) {
//
//            try {
//
//                val markerData = hashMapOf(
//                    "name" to name,
//                    "lat" to lat,
//                    "lng" to lng,
//                    "address" to address,
//                    "iconResId" to iconResId,
//                    "selectedOption" to selectedOption,
//                    "timestamp" to Timestamp.now()
//                )
//
//                // Save marker data to Firestore,
//                firestore.collection("markers").add(markerData)
//                    .addOnSuccessListener {
//                        callback(true, "Marker added successfully")
//                    }
//                    .addOnFailureListener {
//                        callback(false, "Failed to add marker: ${it.message}")
//                    }
//
//
//            } catch (e: Exception) {
//                callback(false, "Exception: ${e.message}")
//            }
//        }

    //Darkova logika, ne dodaje se marker, jedino se slika u bazu dodaje
//        suspend fun addMarker(
//            name: String,
//            lat: Double,
//            lng: Double,
//            address: String,
//            photo: Uri?,
//           iconResId:Int,//dodato
//            selectedOption: String,
//            callback: (Boolean, String) -> Unit,
//            currentTime: Timestamp
//        ) {

//            val userId = auth.currentUser!!.uid
//
//            try {
//                Log.d("MarkerRepository", "Fetching user data for userId: $userId")
//                val userRef = firestore.collection("users").document(userId).get().await()
//                val user = userRef.toObject(User::class.java)
//                    ?: User() // Ako ne postoji korisnik, koristi praznog User-a
//
//                val placeData = Place(
//                    "",
//                    name,
//                    "",
//                    address,
//                    lng,
//                    lat,
//                   selectedOption,
//                    iconResId,
//                    mutableListOf(),
//                    0.0,
//                    0,
//                    "",
//                   currentTime,
//                    user.username,
//                )
//                Log.d("MarkerRepository", "Adding marker to Firestore")
//                // Dodaj dokument i automatski generiši ID
//                val documentRef = firestore.collection("markers").add(placeData).await()
//
//                // Uzmi generisani ID
//                val placeId = documentRef.id
//
//                // Ako postoji slika, sačuvaj je u Firebase Storage
//                if (photo != null) {
//                    Log.d("MarkerRepository", "Uploading photo for placeId: $placeId")
//                    val placePicRef = storage.getReference("place_pictures/$placeId")
//                    placePicRef.putFile(photo).await()  // Čeka da se slika upload-uje
//                    val downloadUri = placePicRef.downloadUrl.await()  // Čeka da se preuzme URL slike
//
//                    Log.d("MarkerRepository", "Updating document with photo URL")
//                    // Ažuriraj Firestore dokument sa URL-om slike i ID-jem
//                    documentRef.update(
//                        mapOf(
//                            "photo" to downloadUri.toString(),
//                            "id" to placeId
//                        )
//                    ).await()
//                } else {
//                    // Ako slika nije dostupna, samo ažuriraj ID
//                    Log.d("MarkerRepository", "No photo provided, updating only the ID")
//                    documentRef.update("id", placeId).await()
//                }
//
//                Log.d("MarkerRepository", "Updating user score")
//                // Ažuriraj score korisnika
//                val userDocRef = firestore.collection("users").document(userId)
//                userDocRef.update("score", FieldValue.increment(20)).await()
//            } catch (e: Exception) {
//                Log.e("MarkerRepository", "Error writing LocationData", e)
//                callback(false, "Failed to add marker: ${e.message}")//dodato radi provere
//            }

        // Fetch markers from Firestore
        fun fetchMarkers() {
            firestore.collection("markers").get()
                .addOnSuccessListener { result ->
                    val markerList = result.map { document ->
                        Place(
                            id=document.getString("id") ?: "",
                            name = document.getString("name") ?: "",
                            latitude = document.getDouble("lat") ?: 0.0,
                            longitude = document.getDouble("lng") ?: 0.0,
                            address = document.getString("address") ?: "",
                            selectedOption = document.getString("selectedOption")?:"Run",
                            //icon=document.getDouble("iconResId")?:0//zasto nece
                        )
                    }
                    _markers.value = markerList
                }
                .addOnFailureListener { exception ->
                    Log.e("MarkerRepository", "Error fetching markers: ${exception.message}")
                }
        }

    fun applyFilters(
        callback: (Boolean) -> Unit,
        author: String,
        type: String,
        date: String,
        radius: Int?,
        currentLoc: LocationData?
    ) {
        Log.i("ApplyFilters", "Author: $author, Type: $type, Date: $date, Radius: $radius")
        val filteredList = _markers.value.filter { location ->

            var authorMatch = location.author.contains(author, ignoreCase = true)
            var typeMatch = location.type.contains(type, ignoreCase = true)
            val dateMatch = date.isEmpty() || isDateInRange(location.timeCreated.toDate(), date)

            if (author.isBlank()) {
                authorMatch = true
            }
            if (type == "Any Type") {
                typeMatch = true
            }


            val withinRadius: Boolean
            // radijus je null ako korisnik ne odobri da se prati lokacija, tako da apliakciaj i dalje radi
            if (radius != null && currentLoc != null) {
                // Check if the marker is within the specified radius
                // Ako korisnik nije nista uneo sto se radijusa tice, onda se gleda kao tacno
                if (radius == 0) {
                    withinRadius = true
                } else {
                    val distance = calculateDistance(
                        currentLoc.latitude,
                        currentLoc.longitude,
                        location.latitude,
                        location.longitude
                    )
                    withinRadius = distance < radius
                }

            } else {
                withinRadius = true

            }


            authorMatch && typeMatch && dateMatch && withinRadius
        }


        if (filteredList.isNotEmpty()) {
            callback(true)
            _filteredMarkers.value = filteredList
        } else {
            callback(false)
        }

    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val radiusOfEarth = 6371 // Earth's radius in kilometers

        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad
        val a =
            Math.sin(dLat / 2).pow(2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2)
                .pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return radiusOfEarth * c
    }


    fun removeFilters() {
        _filteredMarkers.value = emptyList()
    }

    private fun isDateInRange(locationDate: Date, filterDate: String): Boolean {
        val locale = Locale.getDefault()
        val dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, locale)
        val filterDateParts = filterDate.split(" - ")

        try {
            if (filterDateParts.size == 2) {
                val startDate = dateFormat.parse(filterDateParts[0])
                val endDate = dateFormat.parse(filterDateParts[1])

                Log.i(
                    "ApplyFilters",
                    "StartDate: $startDate, EndDate $endDate - LocationDate: $locationDate"
                )


                if (startDate != null && endDate != null) {
                    return locationDate >= startDate && locationDate <= endDate
                }
            }
        } catch (e: ParseException) {
            Log.d("ParseException", e.message.toString())
        }
        return false
    }
}

