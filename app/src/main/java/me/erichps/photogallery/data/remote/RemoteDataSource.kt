package me.erichps.photogallery.data.remote

import me.erichps.photogallery.data.api.UnsplashApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: UnsplashApi) {
    suspend fun getRandomPhotos(page: Int) = api.getRandomPhotos(page)
    suspend fun searchPhotos(page: Int, query: String) = api.searchPhotos(page, query)
}