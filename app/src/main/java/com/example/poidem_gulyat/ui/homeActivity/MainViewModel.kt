package com.example.poidem_gulyat.ui.homeActivity

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.data.ResponseDataBase
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

class MainViewModel(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
    private val markerManager: MarkerManager,
) : ViewModel() {

    private val _responseDataBaseStateFlow:MutableStateFlow<ResponseDataBase> = MutableStateFlow(ResponseDataBase.Looding)
    val responseDataBaseStateFlow:StateFlow<ResponseDataBase> = _responseDataBaseStateFlow

    init {
        Log.d("MainViewModel", "Attraction")
        viewModelScope.launch {
            markerManager.locationFlow.collect {
                when (it) {
                    ResponseHome.Attraction -> {
                        Log.d("MainViewModel", "Attraction")
                        getAttraction()
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun getAttraction() {
        markerManager.attractionRepository.getAllAttractions().collect {
            _responseDataBaseStateFlow.emit(it)
//            when (it) {
//                is ResponseDataBase.Success<*> -> {
//                    it.value
//                }
//                is ResponseDataBase.Failure -> {
//
//                }
//                is ResponseDataBase.Empty -> {
//
//                }
//                else -> {}
//            }
        }
    }

    //  val _buttonStateFlow = markerManager.locationFlow
    //val  buttonStateFlow:StateFlow<ResponseHome>
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

    override fun onCleared() {
        super.onCleared()

        Log.d("onCleared", "onCleared onCleared")
        locationFlow = null
    }
}


class FactoryMainView @Inject constructor(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
    private val markerManager: MarkerManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(userPreferences, gpsRepository, markerManager) as T
    }
}