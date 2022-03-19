package com.example.poidem_gulyat.ui.homeActivity.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.data.dto.LoginResponse
import com.example.poidem_gulyat.data.repository.AuthRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.markers.MarkerManager
import com.example.poidem_gulyat.data.source.local.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
    private val markerManager: MarkerManager
) : ViewModel() {


    private val _buttonsState:MutableStateFlow<ResponseHome> =  MutableStateFlow(
        ResponseHome.Loading)
    val buttonStateFlow :StateFlow<ResponseHome> = _buttonsState.asStateFlow()

    var locationFlow: StateFlow<ResponseSplash<Any?>>? = null

    suspend fun getLastLocation() = flow<ResponseSplash<Location?>> {
        gpsRepository.getLastKnownLocation().collect {
            it?.let {
                emit(ResponseSplash.Success(it))
            } ?: run {
                emit(ResponseSplash.Failure)
            }
        }
    }

    fun attractionButtonClick(){
        viewModelScope.launch {
            if (_buttonsState.value == ResponseHome.Attraction) {
                _buttonsState.emit(ResponseHome.Loading)
                markerManager.locationFlow.emit(ResponseHome.Loading)
            } else {
                _buttonsState.emit(ResponseHome.Attraction)
                markerManager.locationFlow.emit(ResponseHome.Attraction)
                Log.d("attractionButtonClick", "Attraction")
            }
        }
    }

    fun photoZoneButtonClick(){
        viewModelScope.launch {
            if (_buttonsState.value == ResponseHome.PhotoZone) {
                _buttonsState.emit(ResponseHome.Loading)
                markerManager.locationFlow.emit(ResponseHome.Loading)
            } else {
                _buttonsState.emit(ResponseHome.PhotoZone)
                markerManager.locationFlow.emit(ResponseHome.PhotoZone)
                Log.d("attractionButtonClick", "Attraction")
            }
        }
    }

     fun userPointButtonClick(){
        viewModelScope.launch {
            if (_buttonsState.value == ResponseHome.UserPoint) {
                _buttonsState.emit(ResponseHome.Loading)
                markerManager.locationFlow.emit(ResponseHome.Loading)
            } else {
                _buttonsState.emit(ResponseHome.UserPoint)
                markerManager.locationFlow.emit(ResponseHome.UserPoint)
                Log.d("attractionButtonClick", "Attraction")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.d("onCleared", "onCleared onCleared")
        locationFlow = null
    }
}


class FactoryHomeView @Inject constructor(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
    private val markerManager: MarkerManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userPreferences, gpsRepository,markerManager) as T
    }
}