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
import com.example.spotify.databinding.FragmentFavoritesBinding
import com.example.spotify.data.network.FirestoreDbService
import com.example.spotify.ui.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment(), SongsAdapter.IFavorites {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModels = FirestoreDbService()
    private val viewModel: FavoritesViewModel by viewModels()


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        subscribeToObservers()
        return binding.root
    }

    private fun subscribeToObservers() {
        viewModel.getSongFavorites().observe(viewLifecycleOwner, {
            if (it != null && it.isNotEmpty()) {
                binding.tvListFavorites.isVisible = false
                binding.rvFavoritesSongs.layoutManager = LinearLayoutManager(context)
                binding.rvFavoritesSongs.adapter = SongsAdapter(context, it, this)
            }

        })
    }

    override fun onItemFavorites(song: Song) {
            viewModels.updateToFavorites(song)
    }
}