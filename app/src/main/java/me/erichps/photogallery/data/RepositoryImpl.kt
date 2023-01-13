package me.erichps.photogallery.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import me.erichps.photogallery.data.remote.RemoteDataSource
import me.erichps.photogallery.domain.model.Photo
import me.erichps.photogallery.domain.model.Result
import me.erichps.photogallery.domain.provider.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor (
    private val remoteDataSource: RemoteDataSource
): Repository {

    override suspend fun getRandomPhotos(page: Int): Flow<Result<List<Photo>>> {
        return flow<Result<List<Photo>>> {
            val photos = remoteDataSource.getRandomPhotos(page)
            emit(Result.Success(photos))
        }.catch { e ->
            emit(Result.Error(e.message?:""))
        }
    }

    override suspend fun searchPhotos(page: Int, query: String): Flow<Result<List<Photo>>> {
        return flow<Result<List<Photo>>> {
            val photos = remoteDataSource.searchPhotos(page, query)
            emit(Result.Success(photos.results))
        }.catch { e->
            emit(Result.Error(e.message?:""))
        }
    }
}