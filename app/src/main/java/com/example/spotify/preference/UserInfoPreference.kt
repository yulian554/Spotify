package com.example.spotify.preference

import android.content.Context
import com.example.spotify.util.Constant.SHARED_EMAIL_USER
import com.example.spotify.util.Constant.SHARED_NAME
import com.example.spotify.util.Constant.SHARED_PHOTO_1
import com.example.spotify.util.Constant.SHARED_USER
import com.example.spotify.util.Constant.SHARED_USER_NAME
import com.example.spotify.util.Constant.SHARED_USER_PHOTO

class UserInfoPreference(val context: Context) {

    private val personalInformationUser = context.getSharedPreferences(SHARED_NAME, 0)
    private val photoUser = context.getSharedPreferences(SHARED_PHOTO_1, 0)
    private val emailUser = context.getSharedPreferences(SHARED_EMAIL_USER, 0)

    fun saveNameUser(user: String) = personalInformationUser.edit().putString(SHARED_USER_NAME, user).apply()
    fun getNameUser(): String = personalInformationUser.getString(SHARED_USER_NAME, "")!!
    fun wipeNameUser() = personalInformationUser.edit().clear().apply()

    fun savePhotoUser(photo: String) = photoUser.edit().putString(SHARED_USER_PHOTO, photo).apply()
    fun getPhotoUser(): String = photoUser.getString(SHARED_USER_PHOTO, "")!!
    fun wipePhotoUser() = photoUser.edit().clear().apply()

    fun saveUser(user: String) = emailUser.edit().putString(SHARED_USER, user).apply()
    fun getUser(): String = emailUser.getString(SHARED_USER, "")!!
    fun wipeUser() = emailUser.edit().clear().apply()
}
