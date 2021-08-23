package com.example.spotify.data.model

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val songUrl: String = "",
    val imageUrl: String = "",
    var inLibrary: Boolean = false
)

