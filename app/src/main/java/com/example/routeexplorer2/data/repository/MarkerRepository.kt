package com.example.routeexplorer2.data.repository

import android.net.Uri
import android.util.Log
import com.example.routeexplorer2.data.model.LocationData
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.data.model.Review
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

suspend fun addMarker(
    name: String,
    lat: Double,
    lng: Double,
    address: String,
    photo: Uri?,
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
            ?: User()

        //Log.d("MarkerRepository", "Author: ${user.username}")

        //ovako je bilo
        val markerData = hashMapOf(
            "name" to name,
            "lat" to lat,
            "lng" to lng,
            "address" to address,
            "iconResId" to iconResId,
            "selectedOption" to selectedOption,
            "timestamp" to currentTime,
            "author" to user.username
        )

        Log.d("MarkerRepository", "Marker Data: $markerData")
        Log.d("MarkerRepository", "Author: ${user.username}")

        val documentRef = firestore.collection("markers").add(markerData).await()

        val placeId = documentRef.id

        if (photo != null) {
            val storageRef = storage.getReference("place_pictures/$placeId")
            storageRef.putFile(photo).await()
            val downloadUri = storageRef.downloadUrl.await()


            documentRef.update(
                mapOf(
                    "photo" to downloadUri.toString(),
                    "id" to placeId,
                    "author" to user.username
                )
            ).await()
        }
        callback(true, "Marker added successfully with ID: $placeId")
    } catch (e: Exception) {
        callback(false, "Exception: ${e.message}")
    }
}
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
                            reviews = (document.get("reviews") as? List<Review> ?: emptyList()).toMutableList(),// as MutableList<Review>,
                            avgRating = document.getDouble("avgRating") ?: 0.0,
                            //type=document.getString("type")?: "",
                            type = document.getString("selectedOption")?:"Run",
//                            reviewCount = (document.get("reviewCount") ?: 0) as Int,//baca exception
                            reviewCount = (document.get("reviewCount") as? Number)?.toInt() ?: 0,
                            photo = document.getString("photo") ?: "",
                            author = document.getString("author") ?: "",
                            timeCreated = document.getTimestamp("timestamp")?: Timestamp.now()
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

            Log.d("place filter:location ","$location")


            var authorMatch = location.author.contains(author, ignoreCase = true)
            var typeMatch = location.type.contains(type, ignoreCase = true)
            val dateMatch = date.isEmpty() || isDateInRange(location.timeCreated.toDate(), date)

            Log.d("authorMatch","$authorMatch")
            Log.d("typeMatch","$typeMatch")
            Log.d("authorMatch","$authorMatch")
            Log.d("dateMatch","$dateMatch")

            if (author.isBlank()) {
                authorMatch = true
            }
            if (type == "Any Type") {
                typeMatch = true
            }


            val withinRadius: Boolean
            // radijus je null ako korisnik ne odobri da se prati lokacija, tako da apliakciaj i dalje radi
            if (radius != null && currentLoc != null) {
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
            //Log.d("authorMatch && typeMatch && dateMatch && withinRadius","${authorMatch && typeMatch && dateMatch && withinRadius}")
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

