package com.example.routeexplorer2.data.model

data class Review(
    val id: String = "",
    val user: String = "",
    val rating: Int = 0,
    val text: String = "",
    var likes: Int = 0,
    val markerId: String = ""
)