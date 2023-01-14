package me.erichps.photogallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.erichps.photogallery.domain.model.Photo
import me.erichps.photogallery.domain.model.PhotoUrl
import me.erichps.photogallery.domain.model.PhotoUser
import me.erichps.photogallery.domain.model.Result
import me.erichps.photogallery.domain.usecase.GetRandomPhotos
import me.erichps.photogallery.view.viewmodels.FeedViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*
import java.io.IOException
import java.lang.NullPointerException

@ExperimentalCoroutinesApi
class FeedViewModelTest {

    //Rule to mimic background task execution
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var classUnderTest: FeedViewModel
    private lateinit var dispatcher: CoroutineDispatcher

    private val getRandomPhotos: GetRandomPhotos = mock()
    private val dummyResponseRandomPhoto = listOf(
        Photo("1","Photo 1", "Photo 1", PhotoUser("A","a"), PhotoUrl("","","")),
        Photo("2","Photo 2", "Photo 2", PhotoUser("B","b"), PhotoUrl("","","")),
        Photo("3","Photo 3", "Photo 3", PhotoUser("C","c"), PhotoUrl("","","")),
    )

    @Test
    fun `test get random photos should call remote with correct page`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = FeedViewModel(getRandomPhotos, dispatcher)

            //prepare response
            whenever(getRandomPhotos(1)).thenReturn(
                flow {
                    emit(Result.Success(dummyResponseRandomPhoto))
                }
            )

            // view request page 1
            classUnderTest.getPhoto()
            verify(getRandomPhotos).invoke(1)

            //view request another page
            classUnderTest.getPhoto()
            verify(getRandomPhotos).invoke(2)
        }
    }

    @Test
    fun `test get random photos success retrieve data`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = FeedViewModel(getRandomPhotos, dispatcher)

            //prepare response
            whenever(getRandomPhotos(1)).thenReturn(
                flow {
                    emit(Result.Success(dummyResponseRandomPhoto))
                }
            )

            // view request page 1
            classUnderTest.getPhoto()

            //check if live data got new value
            assert(classUnderTest.photos.value != null)
            assert(classUnderTest.photos.value!!.size == dummyResponseRandomPhoto.size)
            assert(classUnderTest.photos.value!! == dummyResponseRandomPhoto)
        }
    }

    @Test
    fun `test get random photos error should post error to live data`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = FeedViewModel(getRandomPhotos, dispatcher)

            //prepare response
            whenever(getRandomPhotos(any())).thenReturn(
                flow {
                    emit(Result.Error(""))
                }
            )

            // view request page 1
            classUnderTest.getPhoto()

            //check if live data error got new value
            assert(classUnderTest.errorMessage.value != null)
            assert(classUnderTest.errorMessage.value!! == "")
        }
    }

    @Test
    fun `test refresh page random photos should begin from page one again`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = FeedViewModel(getRandomPhotos, dispatcher)

            //prepare response
            whenever(getRandomPhotos(any())).thenReturn(
                flow {
                    emit(Result.Success(dummyResponseRandomPhoto))
                }
            )

            // view request page 1 and 2
            classUnderTest.getPhoto()
            classUnderTest.getPhoto()

            //test refresh should begin from pg 1
            //verify getRandomPhotos() with page 1 should called twice
            //Once from the above setup, and another one after refresh
            classUnderTest.refresh()
            classUnderTest.getPhoto()
            verify(getRandomPhotos, times(2)).invoke(1)
        }
    }
}