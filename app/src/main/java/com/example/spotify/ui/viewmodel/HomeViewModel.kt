package com.example.spotify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.model.Song
import com.example.spotify.domain.GetSongsUseCase
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val _song = MutableLiveData<List<Song>>()
    val song: LiveData<List<Song>> = _song
    private var getSongsUseCase = GetSongsUseCase()

    fun getSong(){
        viewModelScope.launch {
        _song.value = getSongsUseCase.getSongForAddFavoritesAndUser(false)
        }
    }
}