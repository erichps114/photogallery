package me.erichps.photogallery

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import me.erichps.photogallery.data.api.UnsplashApi
import me.erichps.photogallery.data.remote.RemoteDataSource
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


@ExperimentalCoroutinesApi
class RemoteDataSourceTest {
    private val api: UnsplashApi = mock()
    private val classUnderTest = RemoteDataSource(api)


    @Test
    fun `test calling api get photos`() {
        runBlocking {
            classUnderTest.getRandomPhotos(1)
            verify(api).getRandomPhotos(1)
        }
    }

    @Test
    fun `test calling api search photos`() {
        runBlocking {
            classUnderTest.searchPhotos(1, "asda")
            verify(api).searchPhotos(1, "asda")
        }
    }
}