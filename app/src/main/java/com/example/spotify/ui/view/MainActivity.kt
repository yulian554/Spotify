package com.example.spotify.ui.view

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.adapters.SwipeSongAdapter
import com.example.spotify.data.model.Song
import com.example.spotify.databinding.ActivityMainBinding
import com.example.spotify.ui.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), View.OnClickListener,
    SwipeSongAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeSongAdapter: SwipeSongAdapter
    private lateinit var mp: MediaPlayer
    private val viewModel: MainViewModel by viewModels()
    private var songs: List<Song> = listOf()
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Spotify)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToObservers()
        setUpEvent()
        val bottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun setUpEvent() {
        binding.ivPlayPause.setOnClickListener(this)
        binding.vpSong.setOnClickListener(this)
    }

    private fun setUpAdapter(songs: List<Song>, index: Int) {
        swipeSongAdapter = SwipeSongAdapter(this, songs, this)
        binding.vpSong.adapter = swipeSongAdapter
        Glide.with(this).load(songs[index].imageUrl).into(binding.ivCurSongImage);
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivPlayPause -> {
                viewModel.playAndPause(mp, binding.ivPlayPause)
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.getSongs()
        viewModel.song.observe(this, { song ->
            if (song != null && song.isNotEmpty()) {
                songs = song
                val url = song[0].songUrl
                try {
                    mp = MediaPlayer().apply {
                        setAudioStreamType(AudioManager.STREAM_MUSIC)
                        setDataSource(url)
                        prepareAsync()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "algo salio mal", Toast.LENGTH_SHORT).show()
                }
                setUpAdapter(song, 0)
            }
        })
    }

    override fun onItemClick(position: Int) {
        viewModel.updateInVpSong(songs, position, bundle)
        binding.navHostFragment.findNavController().navigate(
            R.id.fragmentSong, bundle)
        binding.navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentSong -> hideBottomBar()
                R.id.fragmentHome -> showBottomBar()
                else -> showBottomBar()
            }
        }
    }

    private fun hideBottomBar() {
    binding.navView.isVisible = false
        viewModel.showBottomBarAndHideBottomBar(
            binding.ivCurSongImage, binding.vpSong, binding.ivPlayPause, false
        )
    }

    private fun showBottomBar() {
        binding.navView.isVisible = true
        viewModel.showBottomBarAndHideBottomBar(
            binding.ivCurSongImage, binding.vpSong, binding.ivPlayPause, true
        )
    }

}
