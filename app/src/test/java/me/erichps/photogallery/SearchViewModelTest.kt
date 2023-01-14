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
import me.erichps.photogallery.domain.usecase.SearchPhotos
import me.erichps.photogallery.view.viewmodels.FeedViewModel
import me.erichps.photogallery.view.viewmodels.SearchViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    //Rule to mimic background task execution
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var classUnderTest: SearchViewModel
    private lateinit var dispatcher: CoroutineDispatcher

    private val searchPhotos: SearchPhotos = mock()
    private val dummyResponseSearchPhoto = listOf(
        Photo("1","Photo 1", "Photo 1", PhotoUser("A","a"), PhotoUrl("","","")),
        Photo("2","Photo 2", "Photo 2", PhotoUser("B","b"), PhotoUrl("","","")),
        Photo("3","Photo 3", "Photo 3", PhotoUser("C","c"), PhotoUrl("","","")),
    )

    @Test
    fun `test blank query should post error to live data`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = SearchViewModel(searchPhotos, dispatcher)

            classUnderTest.searchPhoto("", 1)

            //check if live data error got new value
            assert(classUnderTest.errorMessage.value != null)
            assert(classUnderTest.errorMessage.value!! == "")
        }
    }

    @Test
    fun `test search photos should call remote with correct page number and query`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = SearchViewModel(searchPhotos, dispatcher)

            //prepare response
            whenever(searchPhotos(any(), any())).thenReturn(
                flow {
                    emit(Result.Success(dummyResponseSearchPhoto))
                }
            )

            // view request search query
            classUnderTest.searchPhoto("abc", 1)
            verify(searchPhotos).invoke(1,"abc")

            //view request another search query
            classUnderTest.searchPhoto("cde",1)
            verify(searchPhotos).invoke(1, "cde")
        }
    }

    @Test
    fun `test search photos success retrieve data`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = SearchViewModel(searchPhotos, dispatcher)

            //prepare response
            whenever(searchPhotos(any(), any())).thenReturn(
                flow {
                    emit(Result.Success(dummyResponseSearchPhoto))
                }
            )

            // view request search query
            classUnderTest.searchPhoto("abc", 1)

            //check if live data got new value
            assert(classUnderTest.photos.value != null)
            assert(classUnderTest.photos.value!!.size == dummyResponseSearchPhoto.size)
            assert(classUnderTest.photos.value!! == dummyResponseSearchPhoto)
        }
    }

    @Test
    fun `test search photos error should post error to live data`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = SearchViewModel(searchPhotos, dispatcher)

            //prepare response
            whenever(searchPhotos(any(), any())).thenReturn(
                flow {
                    emit(Result.Error(""))
                }
            )

            // view request page 1
            classUnderTest.searchPhoto("abc",1)

            //check if live data error got new value
            assert(classUnderTest.errorMessage.value != null)
            assert(classUnderTest.errorMessage.value!! == "")
        }
    }

    @Test
    fun `test load more data should increment the page but keep the latest query`() {
        runTest {
            dispatcher = UnconfinedTestDispatcher(testScheduler)
            classUnderTest = SearchViewModel(searchPhotos, dispatcher)

            //prepare response
            whenever(searchPhotos(any(), any())).thenReturn(
                flow {
                    emit(Result.Success(dummyResponseSearchPhoto))
                }
            )

            // view request page 1 of query "abc"
            classUnderTest.searchPhoto("abc",1)

            //view request more data
            //verify page == 2 and query "abc"
            classUnderTest.loadMoreData()
            verify(searchPhotos).invoke(2,"abc")
        }
    }
}