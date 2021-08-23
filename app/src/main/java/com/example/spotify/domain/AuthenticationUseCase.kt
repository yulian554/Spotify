package com.example.spotify.domain

import com.example.spotify.data.network.AuthenticationService

class AuthenticationUseCase {

    private val service = AuthenticationService()

    operator fun invoke() = service.saveNameAndPhotoWhenStartingAssignment()
}