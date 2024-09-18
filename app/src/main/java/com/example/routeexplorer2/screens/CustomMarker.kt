package com.example.routeexplorer2.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable?.let {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    return null
}

@Composable
fun MapMarker(
    context: Context,
    position: LatLng,
    title: String,
    snippet: String,
    @DrawableRes iconResourceId: Int,
    onMapClick: (marker: Marker) -> Boolean,
) {
    val icon = bitmapDescriptor(//bitmapDescriptorFromVector
        context, iconResourceId
    )
    Marker(
        state = MarkerState(position = position),
        title = title,
        snippet = snippet,
        icon = icon,
        onClick = { onMapClick(it) }

    )
}

//baca exception
//@Composable
//fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
//    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
//    vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
//    val bitmap = Bitmap.createBitmap(
//        vectorDrawable!!.intrinsicWidth,
//        vectorDrawable.intrinsicHeight,
//        Bitmap.Config.ARGB_8888
//    )
//    val canvas = Canvas(bitmap)
//    vectorDrawable.draw(canvas)
//    return BitmapDescriptorFactory.fromBitmap(bitmap)
//}



//@Composable
//fun MapMarker(
//    context: Context,
//    position: LatLng,
//    title: String,
//    snippet: String,
//    @DrawableRes iconResourceId: Int,
//    onMapClick: (marker: Marker) -> Boolean,
//) {
//    val icon = bitmapDescriptor(
//        context, iconResourceId
//    )
//    Marker(
//        state = MarkerState(position = position),
//        title = title,
//        snippet = snippet,
//        icon = icon,
//        onClick = { onMapClick(it) }
//
//    )
//}

//Darkova fja
fun bitmapDescriptor(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
