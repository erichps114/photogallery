package me.erichps.photogallery.domain.model

sealed class Result<T: Any> {
    data class Success<T: Any>(val data: T): Result<T>()
    data class Error<T: Any>(val msg: String): Result<T>()
}
