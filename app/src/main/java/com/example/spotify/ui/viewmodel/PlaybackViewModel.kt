package com.example.spotify.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotify.data.model.Song
import com.example.spotify.domain.GetSongsUseCase

class PlaybackViewModel: ViewModel() {

    var getSongsUseCase = GetSongsUseCase()

    fun getSongFavoritesToPlay(): MutableLiveData<List<Song>>{
        return getSongsUseCase.getSongFavoritesToPlay()
    }
}