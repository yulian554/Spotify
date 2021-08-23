package com.example.spotify.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotify.domain.StorageUseCase
import de.hdodenhof.circleimageview.CircleImageView

class ProfileViewModel: ViewModel() {

    private var storageUseCase = StorageUseCase()
    var progress = MutableLiveData<Boolean>()

    fun savePhoto(photo: Uri?) = storageUseCase.savePhoto(photo)
    fun getPhotoForShow(context: Context, setImage: CircleImageView) = storageUseCase.getPhotoForShow(context, setImage, progress)

}