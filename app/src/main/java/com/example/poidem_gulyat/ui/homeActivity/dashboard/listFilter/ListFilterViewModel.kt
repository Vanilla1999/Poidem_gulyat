package com.example.poidem_gulyat.ui.homeActivity.dashboard.listFilter

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
import com.example.poidem_gulyat.utils.empty
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFilterViewModel(
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

    private val _sharedStateFlowDataBase = MutableSharedFlow<ResponseDataBase<Filter>>(replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedStateFlowDataBase = _sharedStateFlowDataBase.asSharedFlow()
    private val _stateFlowSizeMarkers :MutableStateFlow<Int> = MutableStateFlow(0)
    val stateFlowSizeMarkers =_stateFlowSizeMarkers.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            mockDatabase()
        }
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
          markerManager.getAllMarkersWithFilter().collect {
              when(it){
                  is ResponseDataBase.Empty ->{ _stateFlowSizeMarkers.emit(empty)}
                  is ResponseDataBase.Success ->{_stateFlowSizeMarkers.emit(it.value.size) }
                  is ResponseDataBase.Failure ->{_stateFlowSizeMarkers.emit(empty) }
                  else -> {}
              }
          }
        }
    }

    private suspend fun mockDatabase() {
        markerManager.filtersRepository.insertOrIgnore(
            Filter()
        )
    }

    fun getAllFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            markerManager.filtersRepository.getFilters().collect {
                when (it) {
                    ResponseDataBase.Empty -> {
                        _sharedStateFlowDataBase.emit(ResponseDataBase.Empty)
                    }
                    is ResponseDataBase.SuccessNotList -> {
                        _sharedStateFlowDataBase.emit(ResponseDataBase.SuccessNotList(it.value))
                    }
                    is ResponseDataBase.Failure -> {
                        _sharedStateFlowDataBase.emit(ResponseDataBase.Failure(it.errorBody))
                    }
                }
            }
        }
    }

    fun changeFilter(filter: Filter) {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            markerManager.filtersRepository.insert(filter)
        }
    }

    fun clearFilter() {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            markerManager.filtersRepository.delete()
        }
    }

    override fun onCleared() {
        Log.d("FilterViewModel", "onCleard")
        super.onCleared()
    }
}

class FactoryListFilterView @Inject constructor(
    private val markerManager: MarkerManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListFilterViewModel(markerManager) as T
    }
}