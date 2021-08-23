package com.example.spotify.ui.view

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.data.model.Song
import com.example.spotify.databinding.FragmentPlaybackBinding
import com.example.spotify.data.network.FirestoreDbService
import com.example.spotify.ui.viewmodel.PlaybackViewModel
import kotlin.properties.Delegates


class PlaybackFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPlaybackBinding? = null
    private val binding get() = _binding!!
    private val service = FirestoreDbService()
    private lateinit var mp: MediaPlayer
    private var songs: List<Song> = listOf()
    private var index: Int = 0
    private lateinit var title: String
    private lateinit var subTitle: String
    private lateinit var image: String
    private lateinit var songUrl: String
    private var inLibrary by Delegates.notNull<Boolean>()
    private val viewModel: PlaybackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaybackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateViewSong()
        initMediaPlayer(songUrl)
        setUpView()
        setUpEvent()
        registerObserver()
    }

    private fun updateViewSong(){
        arguments?.let {
            index = it.getInt("index")
            title = it.getString("title").toString()
            subTitle = it.getString("subTitle").toString()
            image = it.getString("image").toString()
            songUrl = it.get("songUrl").toString()
            inLibrary = it.getBoolean("inLibrary")
        }
    }

    private fun updateOnPassingAndReturnSong (index: Int){
        title = songs[index].title
        subTitle = songs[index].subtitle
        image = songs[index].imageUrl
        inLibrary = songs[index].inLibrary
        updateTitleAndSongImage()
    }

     private fun setUpEvent() {
         binding.ivPlayPause.setOnClickListener(this)
         binding.ivPassSong.setOnClickListener(this)
         binding.ivReturnSong.setOnClickListener(this)
         binding.ivMyFavorite.setOnClickListener(this)
    }
    private fun initMediaPlayer(songUrl: String){
        mp = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(songUrl)
            prepare()
        }
    }
    private fun updateLimitSong(){
        Thread {
            while (true) {
                try {
                    val msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }.start()
    }

    private fun registerObserver(){
        context?.let {
            viewModel.getSongFavoritesToPlay().observe(viewLifecycleOwner, {
                if (it != null && it.isNotEmpty()) {
                    songs = it
                }
            })
        }
    }

    private fun setUpView() {
        binding.seekBar.max = mp.duration
        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )
        updateLimitSong()
        updateTitleAndSongImage()
    }


    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val currentPosition = msg.what
            // update positionBa
            binding.seekBar.progress = currentPosition
            // Update Labels
            val elapsedTime = createTimeLabel(currentPosition)
            binding.tvCurTime.text = elapsedTime
            val remainingTime = createTimeLabel(mp.duration - currentPosition)
            binding.tvSongDuration.text = "-$remainingTime"
        }
    }

    private fun createTimeLabel(time: Int): String {
        var timeLabel: String
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivPlayPause -> {
                if (mp.isPlaying) {
                    mp.pause()
                    binding.ivPlayPause.setBackgroundResource(R.drawable.ic_play_circle)
                } else {
                    mp.start()
                    binding.ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle)
                }
            }
            R.id.ivPassSong -> {
                mp.pause()
                if(index < songs.size - 1){
                    index++
                } else{
                    index = 0
                }
                initMediaPlayer(songs[index].songUrl)
                updateLimitSong()
                binding.ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle)
                updateOnPassingAndReturnSong(index)
                mp.start()
            }
            R.id.ivReturnSong -> {
                mp.pause()
                if(index <= songs.size - 1){
                    index--
                    if(index < 0){
                        index = songs.size-1
                    }
                } else{
                    index = 0
                }
                initMediaPlayer(songs[index].songUrl)
                updateLimitSong()
                binding.ivPlayPause.setBackgroundResource(R.drawable.ic_pause_circle)
                updateOnPassingAndReturnSong(index)
                mp.start()
            }
            R.id.ivMyFavorite -> {
                checkFavoritesButton(songs[index])
                service.updateToFavorites(songs[index])
            }
        }
    }

    private fun updateTitleAndSongImage() {
        val title = "$title - $subTitle"
        binding.tvSongName.text = title
        context?.let { Glide.with(it).load(image).into(binding.ivSongImage) }
        if (inLibrary) {
            binding.ivMyFavorite.setBackgroundResource(R.drawable.ic_favorite2)
        } else {
            binding.ivMyFavorite.setBackgroundResource(R.drawable.ic_favorite1)
        }
    }

    private fun checkFavoritesButton(song: Song){
        song.inLibrary = !song.inLibrary
        if (song.inLibrary) {
            binding.ivMyFavorite.setBackgroundResource(R.drawable.ic_favorite2)
            Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
        } else {
            binding.ivMyFavorite.setBackgroundResource(R.drawable.ic_favorite1)
            Toast.makeText(context, "Eliminada a favoritos", Toast.LENGTH_SHORT).show()
        }
    }

}