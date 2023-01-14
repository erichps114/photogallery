package me.erichps.photogallery

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.erichps.photogallery.data.RepositoryImpl
import me.erichps.photogallery.data.remote.RemoteDataSource
import me.erichps.photogallery.domain.model.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@ExperimentalCoroutinesApi
class RepositoryImplTest {
    private val remoteDataSource: RemoteDataSource = mock()
    private val classUnderTest = RepositoryImpl(remoteDataSource)
    private val dummyResponseRandomPhoto = listOf(
        Photo("1","Photo 1", "Photo 1", PhotoUser("A","a"), PhotoUrl("","","")),
        Photo("2","Photo 2", "Photo 2", PhotoUser("B","b"), PhotoUrl("","","")),
        Photo("3","Photo 3", "Photo 3", PhotoUser("C","c"), PhotoUrl("","","")),
    )
    private val dummyResponseSearchPhoto = SearchResponse(dummyResponseRandomPhoto)

    @Test
    fun `test repository get random photos should return remote data as flow`() {
        runBlocking {
            whenever(remoteDataSource.getRandomPhotos(1)).thenReturn(dummyResponseRandomPhoto)
            val response = classUnderTest.getRandomPhotos(1).first()
            assert(response is Result.Success)

            val list = (response as Result.Success).data
            assert(list == dummyResponseRandomPhoto)
        }
    }

    @Test
    fun `test repository get random photos should emit error when exception occur`() {
        runBlocking {
            whenever(remoteDataSource.getRandomPhotos(1)).thenThrow(NullPointerException())
            val response = classUnderTest.getRandomPhotos(1).first()
            assert(response is Result.Error)
            assert((response as Result.Error).msg == "")
        }
    }

    @Test
    fun `test repository search photos should return remote data as flow`() {
        runBlocking {
            whenever(remoteDataSource.searchPhotos(1,"erich")).thenReturn(dummyResponseSearchPhoto)
            val response = classUnderTest.searchPhotos(1,"erich").first()
            assert(response is Result.Success)

            val list = (response as Result.Success).data
            assert(list == dummyResponseRandomPhoto)
        }
    }

    @Test
    fun `test repository search photos should emit error when exception occur`() {
        runBlocking {
            whenever(remoteDataSource.searchPhotos(1,"erich")).thenThrow(NullPointerException())
            val response = classUnderTest.searchPhotos(1,"erich").first()
            assert(response is Result.Error)
            assert((response as Result.Error).msg == "")
        }
    }
}