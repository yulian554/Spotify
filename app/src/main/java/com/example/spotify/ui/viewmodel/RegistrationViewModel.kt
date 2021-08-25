package com.example.spotify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.model.Song
import com.example.spotify.domain.GetSongsUseCase
import kotlinx.coroutines.launch

class RegistrationViewModel: ViewModel() {

    private var getSongsUseCase = GetSongsUseCase()

    fun getSong() {
        viewModelScope.launch {
            getSongsUseCase.getSongForAddFavoritesAndUser(true)
        }
    }
}