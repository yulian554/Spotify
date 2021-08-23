package com.example.spotify.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotify.data.model.Song
import com.example.spotify.domain.GetSongsUseCase

class FavoritesViewModel: ViewModel() {

    var getSongsUseCase = GetSongsUseCase()

    fun getSongFavorites(): MutableLiveData<List<Song>>{
        return getSongsUseCase.getSongFavorites()
    }
}