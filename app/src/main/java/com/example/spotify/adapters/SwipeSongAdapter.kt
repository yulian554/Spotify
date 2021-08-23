package com.example.spotify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.data.model.Song
import com.example.spotify.databinding.ItemSwipeBinding
import com.example.spotify.R


class SwipeSongAdapter (val context: Context, val song: List<Song>, val listener: OnItemClickListener): RecyclerView.Adapter<SwipeSongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeSongAdapter.SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SongViewHolder(layoutInflater.inflate(R.layout.item_swipe, parent, false))
    }

    override fun onBindViewHolder(holder: SwipeSongAdapter.SongViewHolder, position: Int) {
        holder.render(song[position])
    }

    override fun getItemCount(): Int {
        return song.size
    }

    inner class SongViewHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener {
        private val binding = ItemSwipeBinding.bind(view)
        fun render (song: Song){
            val text = "${song.title} - ${song.subtitle}"
            binding.tvPrimarySw.text = text
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}