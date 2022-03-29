package com.example.poidem_gulyat.ui.homeActivity.dashboard

import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.example.poidem_gulyat.data.ErrorApp
import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.data.repository.filter.FiltersRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.markers.MarkerManager
import com.example.poidem_gulyat.data.source.local.UserPreferences
import com.example.poidem_gulyat.ui.homeActivity.home.HomeViewModel
import com.example.poidem_gulyat.utils.attraction
import com.example.poidem_gulyat.utils.photoZone
import com.example.poidem_gulyat.utils.userPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterViewModel(
    private val userPreferences: UserPreferences,
    private val filtersRepository: FiltersRepository,
    private val markerManager: MarkerManager,
) : ViewModel() {
    private val _sharedStateFlowError = MutableSharedFlow<ErrorApp<Any?>>(replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedStateFlowError = _sharedStateFlowError.asSharedFlow()
    private val coroutineException = CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            _sharedStateFlowError.emit(ErrorApp.FailureUnknown(throwable.toString()))
            Log.d("FilterViewModel", throwable.toString())
        }
    } + CoroutineName("FilterViewModel")

    private val _sharedStateFlowDataBase = MutableSharedFlow<ResponseDataBase<Any?>>(replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedStateFlowDataBase = _sharedStateFlowDataBase.asSharedFlow()

    private val _sharedStateFlowString = MutableSharedFlow<String>(replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedStateFlowString = _sharedStateFlowString.asSharedFlow()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    fun getAllMarkers() {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {

            markerManager.markerRepository.getAllMarksers().collect { markerList ->
                when (markerList) {
                    ResponseDataBase.Empty -> {
                        _sharedStateFlowDataBase.emit(ResponseDataBase.Empty)
                    }
                    is ResponseDataBase.Success -> {
                        var markerListThis = markerList.value
                        filtersRepository.getFilters().collect { filterList ->
                            when (filterList) {
                                is ResponseDataBase.Success -> {
                                    filterList.value.forEachIndexed { index, filter ->
                                        when (index) {
                                            0 -> {
                                                if (filter.sortByRating == 1) {
                                                    markerListThis = markerList.value.sortedByDescending  { it.rating }
                                                }
                                            }
                                            1 -> {
                                                if (filter.attraction == 1)
                                                    markerListThis =
                                                        markerList.value.filter { it.type == attraction }
                                                if (filter.photoZone == 1)
                                                    markerListThis =
                                                        markerList.value.filter { it.type == photoZone }
                                                if (filter.userPoint == 1)
                                                    markerListThis =
                                                        markerList.value.filter { it.type == userPoint }
                                                _sharedStateFlowDataBase.emit(ResponseDataBase.Success(
                                                    markerListThis))
                                            }
                                            2 -> {

                                            }
                                            3 -> {

                                            }
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                    is ResponseDataBase.Failure -> {
                        _sharedStateFlowDataBase.emit(ResponseDataBase.Failure(markerList.errorBody))
                    }
                }
            }
        }
    }

    fun serchStateChanged(string: String) {
        viewModelScope.launch {
            _sharedStateFlowString.emit(string)
        }
    }

    fun clearFilters() {
        viewModelScope.launch(Dispatchers.IO  + coroutineException) {
            filtersRepository.insert(
                listOf(
                    Filter(0), Filter(1), Filter(2), Filter(3)
                ))
        }
    }

    override fun onCleared() {
        Log.d("FilterViewModel", "onCleard")
        super.onCleared()
    }
}

class FactoryFilterView @Inject constructor(
    private val userPreferences: UserPreferences,
    private val filtersRepository: FiltersRepository,
    private val markerManager: MarkerManager,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilterViewModel(userPreferences, filtersRepository, markerManager) as T
    }
}