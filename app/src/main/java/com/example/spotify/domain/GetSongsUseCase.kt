package com.example.spotify.domain

import com.example.spotify.data.network.FirestoreDbService

class GetSongsUseCase {

    private val service = FirestoreDbService()


    suspend fun getSongForAddFavoritesAndUser(fromRegister: Boolean)  = service.getSongForAddFavoritesAndUser(fromRegister)
    fun getSongFavorites() = service.getSongFavorites()
    fun getSongFavoritesToPlay() = service.getSongFavoritesToPlay()
}