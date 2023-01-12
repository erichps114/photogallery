package me.erichps.photogallery.domain.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: String,
    val description: String?,
    @SerializedName("alt_description") val altDescription: String?,
    val user: PhotoUser,
    val urls: PhotoUrl
)
