package me.erichps.photogallery.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import me.erichps.photogallery.di.IODispatcher
import me.erichps.photogallery.domain.model.Photo
import me.erichps.photogallery.domain.model.Result
import me.erichps.photogallery.domain.usecase.GetRandomPhotos
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getRandomPhotos: GetRandomPhotos,
    @IODispatcher private val dispatcherIO: CoroutineDispatcher
): ViewModel() {

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> = _photos

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var page = 1


    fun getPhoto() {
        viewModelScope.launch(dispatcherIO) {
            getRandomPhotos(page).collect {
                when (it) {
                    is Result.Success -> {
                        _photos.postValue(it.data)
                        page++
                    }
                    else -> _errorMessage.postValue("")
                }
            }
        }
    }
}