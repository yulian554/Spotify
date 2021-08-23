package com.example.spotify.domain

import android.net.Uri
import com.example.spotify.data.network.FirestoreDbService

class GetSongsUseCase {

    private val service = FirestoreDbService()

    fun getSongs() = service.getUserSong()
    fun getSong()  = service.getSong(true)
    fun getSongFavorites() = service.getSongFavorites()
    fun getSongFavoritesToPlay() = service.getSongFavoritesToPlay()
}