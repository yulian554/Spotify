package com.example.spotify.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotify.adapters.SongsAdapter
import com.example.spotify.data.model.Song
import com.example.spotify.databinding.FragmentHomeBinding
import com.example.spotify.data.network.FirestoreDbService
import com.example.spotify.ui.viewmodel.HomeViewModel


class HomeFragment : Fragment(), SongsAdapter.IFavorites{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModels = FirestoreDbService()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        subscribeToObservers()
        return binding.root
    }

    //OBTIENENR DATOS PARA MOSTRAR EN EL RECYCLER
    private fun subscribeToObservers() {
        viewModel.getSong()
        context?.let {
            viewModel.song.observe(viewLifecycleOwner, {
                if (it != null && it.isNotEmpty()) {
                    binding.allSongsProgressBar.isVisible = false
                    binding.rvAllSongs.layoutManager = LinearLayoutManager(context)
                    binding.rvAllSongs.adapter = SongsAdapter(context, it, this)
                }

            })
        }
    }

    override fun onItemFavorites(song: Song) {
            viewModels.updateToFavorites(song)
    }

}