package me.erichps.photogallery.data.api

import me.erichps.photogallery.domain.model.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("photos")
    suspend fun getRandomPhotos(@Query("page") page: Int): List<Photo>
}