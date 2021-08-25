package com.example.spotify.data.network

import androidx.lifecycle.MutableLiveData
import com.example.spotify.data.model.Song
import com.example.spotify.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirestoreDbService {

    var list1 = mutableListOf<Song>()
    private var auth = FirebaseAuth.getInstance()
    private val songsMd = MutableLiveData<List<Song>>()
    private val songsUser = MutableLiveData<List<Song>>()
    private val songsMdFavorites = MutableLiveData<List<Song>>()
    private val db = FirebaseFirestore.getInstance()

    suspend fun getSongForAddFavoritesAndUser(fromRegister: Boolean): List<Song> {
        val list = mutableListOf<Song>()
        withContext(Dispatchers.IO) {
            db.collection(Constant.SONG_COLLECTION).get().addOnSuccessListener { result ->
                for (s in result) {
                    val id = s.getString(Constant.ID)
                    val imageUrl = s.getString(Constant.IMAGE_URL)
                    val title = s.getString(Constant.TITLE)
                    val subtitle = s.getString(Constant.SUBTITLE)
                    val songUrl = s.getString(Constant.SONG_URL)
                    val inLibrary = s.getBoolean(Constant.IN_LIBRARY)
                    val song =
                        Song(id!!, title!!, subtitle!!, songUrl!!, imageUrl!!, inLibrary!!)
                    list.add(song)
                }
                list1 = list
            }
        }
            return if (fromRegister) {
                addAllSongsToFavorites(list)
                list
            } else
                list1

    }

    fun getSongFavorites(): MutableLiveData<List<Song>> {
        db.collection(auth.currentUser?.uid!!).get().addOnSuccessListener { result ->
            val list = mutableListOf<Song>()
            for (s in result) {
                val id = s.getString(Constant.ID)
                val imageUrl = s.getString(Constant.IMAGE_URL)
                val title = s.getString(Constant.TITLE)
                val subtitle = s.getString(Constant.SUBTITLE)
                val songUrl = s.getString(Constant.SONG_URL)
                val inLibrary = s.getBoolean(Constant.IN_LIBRARY)
                val song =
                    Song(id!!, title!!, subtitle!!, songUrl!!, imageUrl!!, inLibrary!!)
                list.add(song)
            }
            songsMdFavorites.value = list.filter { it.inLibrary }
        }
        return songsMdFavorites
    }

    fun getSongFavoritesToPlay(): MutableLiveData<List<Song>> {
        db.collection(auth.currentUser?.uid!!).get().addOnSuccessListener { result ->
            val list = mutableListOf<Song>()
            for (s in result) {
                val id = s.getString(Constant.ID)
                val imageUrl = s.getString(Constant.IMAGE_URL)
                val title = s.getString(Constant.TITLE)
                val subtitle = s.getString(Constant.SUBTITLE)
                val songUrl = s.getString(Constant.SONG_URL)
                val inLibrary = s.getBoolean(Constant.IN_LIBRARY)
                val song =
                    Song(id!!, title!!, subtitle!!, songUrl!!, imageUrl!!, inLibrary!!)
                list.add(song)
            }
            songsMdFavorites.value = list
        }
        return songsMdFavorites
    }

    private fun addAllSongsToFavorites(songs: List<Song>) {
        for (song in songs) {
            updateToFavorites(song)
        }
    }

    fun updateToFavorites(song: Song) {
        db.collection(auth.currentUser?.uid!!).document(song.mediaId).set(song)
    }

}