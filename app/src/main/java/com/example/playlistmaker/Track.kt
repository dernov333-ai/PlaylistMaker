package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackName: String?,
    val artistName: String?,
    val artworkUrl100: String?,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long? = null,
)