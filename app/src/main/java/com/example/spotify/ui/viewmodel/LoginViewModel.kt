package com.example.spotify.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spotify.domain.AuthenticationUseCase

class LoginViewModel: ViewModel() {

    private var authenticationUseCase = AuthenticationUseCase()

    fun saveNameAndPhotoWhenStartingAssignment(){
        authenticationUseCase()
    }
}