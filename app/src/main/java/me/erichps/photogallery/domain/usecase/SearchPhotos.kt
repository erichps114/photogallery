package me.erichps.photogallery.domain.usecase

import me.erichps.photogallery.domain.provider.Repository
import javax.inject.Inject

class SearchPhotos @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(page: Int, query: String) = repository.searchPhotos(page, query)
}