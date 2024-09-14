import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import java.util.Locale
fun hasLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun reverseGeocodeLocation(context: Context, coordinate: LatLng) : String {
    val geocoder = Geocoder(context, Locale.getDefault())
    // it can be multiple addresses that fir to certain location, zato je stavlja u listu
    val addresses:MutableList<Address>? = geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
    return if(addresses?.isNotEmpty() == true) {
        addresses[0].getAddressLine(0)
    } else {
        "Addresses not found"
    }

}


//package com.example.routeexplorer2.utils
//
//import android.Manifest
//import android.content.pm.PackageManager
//import androidx.core.app.ActivityCompat
//
//fun checkLocationPermission(){
//
//    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
//        && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED ){
//        //get location
//    } else{
//        requestForPermissions();
//    }
//}
//
//fun requestForPermissions() {
//    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
//    TODO("Not yet implemented")
//}
