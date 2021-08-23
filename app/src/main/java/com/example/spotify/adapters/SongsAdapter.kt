package com.example.spotify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify.R
import com.example.spotify.data.model.Song
import com.example.spotify.databinding.ItemViewBinding

class SongsAdapter(

    val context: Context?, private val songs: List<Song>, val listener: IFavorites? = null) :
    RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SongViewHolder(layoutInflater.inflate(R.layout.item_view, parent, false))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.render(songs[position])
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemViewBinding.bind(view)

        fun render(song: Song) {
            binding.tvPrimary.text = song.title
            binding.tvSecondary.text = song.subtitle
            Glide.with(context!!).load(song.imageUrl).into(binding.ivItemImage)
            if (song.inLibrary) {
                binding.ivMyFavorite.setBackgroundResource(R.drawable.ic_favorite2)
            } else {
                binding.ivMyFavorite.setBackgroundResource(R.drawable.ic_favorite1)
            }
            binding.ivMyFavorite.setOnClickListener {
                song.inLibrary = !song.inLibrary
                if (song.inLibrary) {
                    it.setBackgroundResource(R.drawable.ic_favorite2)
                    Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                } else {
                    it.setBackgroundResource(R.drawable.ic_favorite1)
                    Toast.makeText(context, "Eliminada a favoritos", Toast.LENGTH_SHORT).show()
                }
                listener?.onItemFavorites(song)
                notifyDataSetChanged()
                //notifyItemRemoved()
            }
            itemView.setOnClickListener { Toast.makeText(context, "Toco ${song.title}", Toast.LENGTH_SHORT).show() }
        }
    }

    interface IFavorites {
        fun onItemFavorites(song: Song)
    }
}