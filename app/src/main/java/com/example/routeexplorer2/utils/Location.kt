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
