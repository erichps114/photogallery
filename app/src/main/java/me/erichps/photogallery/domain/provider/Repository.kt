package me.erichps.photogallery.domain.provider

import kotlinx.coroutines.flow.Flow
import me.erichps.photogallery.domain.model.Photo
import me.erichps.photogallery.domain.model.Result

interface Repository {
    suspend fun getRandomPhotos(page: Int): Flow<Result<List<Photo>>>
    suspend fun searchPhotos(page: Int, query: String): Flow<Result<List<Photo>>>
}