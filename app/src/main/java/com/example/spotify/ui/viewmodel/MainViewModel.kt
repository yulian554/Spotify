package com.example.spotify.ui.viewmodel

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.example.spotify.R
import com.example.spotify.data.model.Song
import com.example.spotify.domain.GetSongsUseCase
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _song = MutableLiveData<List<Song>>()
    val song: LiveData<List<Song>> = _song
    private var getSongsUseCase = GetSongsUseCase()

    fun getSongs() {
        viewModelScope.launch {
            _song.value = getSongsUseCase.getSongForAddFavoritesAndUser(false)
        }
    }

    fun playAndPause(mp: MediaPlayer, ivPlayAndPause: ImageView) {
        if (mp.isPlaying) {
            mp.pause()
            ivPlayAndPause.setBackgroundResource(R.drawable.ic_play)
        } else {
            mp.start()
            ivPlayAndPause.setBackgroundResource(R.drawable.ic_pause)
        }
    }

    fun showBottomBarAndHideBottomBar(
        ivCurSongImage: ImageView, vpSong: ViewPager2, ivPlayPause: ImageView, validate: Boolean
    ) {
        if (validate) {
            ivCurSongImage.isVisible = true
            vpSong.isVisible = true
            ivPlayPause.isVisible = true
        } else {
            ivCurSongImage.isVisible = false
            vpSong.isVisible = false
            ivPlayPause.isVisible = false
        }
    }

    fun updateInVpSong(songs: List<Song>, position: Int, bundle: Bundle) {
        bundle.putInt("index", position)
        bundle.putString("image", songs[position].imageUrl)
        bundle.putString("songUrl", songs[position].songUrl)
        bundle.putString("title", songs[position].title)
        bundle.putString("subTitle", songs[position].subtitle)
        bundle.putBoolean("inLibrary", songs[position].inLibrary)
    }
}