package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
) {

    private val artwork: ImageView = itemView.findViewById(R.id.ivArtwork)
    private val trackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.tvTrackTime)

    private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    fun bind(track: Track) {
        trackName.text = track.trackName ?: ""
        artistName.text = track.artistName ?: ""

        val timeText = when {
            track.trackTimeMillis != null -> timeFormatter.format(track.trackTimeMillis)
            else -> ""
        }
        trackTime.text = timeText

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder_45)
            .error(R.drawable.ic_track_placeholder_45)
            .centerCrop()
            .into(artwork)
    }
}