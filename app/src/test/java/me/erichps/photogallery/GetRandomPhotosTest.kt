package me.erichps.photogallery

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import me.erichps.photogallery.domain.provider.Repository
import me.erichps.photogallery.domain.usecase.GetRandomPhotos
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class GetRandomPhotosTest {
    private val repository: Repository = mock()
    private val classUnderTest = GetRandomPhotos(repository)

    @Test
    fun `test repository get random photos`() {
        runBlocking {
            classUnderTest(1)
            verify(repository).getRandomPhotos(1)
        }
    }
}