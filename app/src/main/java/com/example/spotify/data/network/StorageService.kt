package com.example.spotify.data.network

import android.net.Uri
import com.example.spotify.ui.view.SplashScreenActivity.Companion.prefs
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class StorageService {

    private val realtimeDbService = RealtimeDbService()

    fun savePhoto(photo: Uri?) {
        val folder: StorageReference =
            FirebaseStorage.getInstance().reference.child("ImageProfile")
        val fileName: StorageReference? =
            photo!!.lastPathSegment?.let { folder.child(it) }
        fileName!!.putFile(photo).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener {
                prefs.savePhotoUser(it.toString())
                realtimeDbService.savePhotoRealtime()
            }
        }
    }
}