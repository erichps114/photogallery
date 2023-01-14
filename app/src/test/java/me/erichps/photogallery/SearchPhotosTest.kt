package me.erichps.photogallery

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import me.erichps.photogallery.domain.provider.Repository
import me.erichps.photogallery.domain.usecase.GetRandomPhotos
import me.erichps.photogallery.domain.usecase.SearchPhotos
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class SearchPhotosTest {
    private val repository: Repository = mock()
    private val classUnderTest = SearchPhotos(repository)

    @Test
    fun `test repository get random photos`() {
        runBlocking {
            classUnderTest(2, "ichigo")
            verify(repository).searchPhotos(2, "ichigo")
        }
    }
}