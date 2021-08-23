package com.example.spotify.domain

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.spotify.data.network.RealtimeDbService
import com.example.spotify.data.network.StorageService
import de.hdodenhof.circleimageview.CircleImageView

class StorageUseCase {

    private val service1 = StorageService()
    private val service2 = RealtimeDbService()

    fun savePhoto(photo: Uri?) = service1.savePhoto(photo)
    fun getPhotoForShow(context: Context, setImage: CircleImageView, progress: MutableLiveData<Boolean>) = service2.getPhotoForShow(context, setImage, progress)
}