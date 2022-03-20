package com.example.poidem_gulyat.ui.homeActivity

import android.database.sqlite.SQLiteException
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.poidem_gulyat.data.ErrorApp
import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.markers.MarkerManager
import com.example.poidem_gulyat.data.source.local.UserPreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class MainViewModel(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
    private val markerManager: MarkerManager,
) : ViewModel() {
    private val _sharedStateFlowError = MutableSharedFlow<ErrorApp<Any?>>(replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedStateFlowError = _sharedStateFlowError.asSharedFlow()
    private val coroutineException = CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            _sharedStateFlowError.emit(ErrorApp.FailureUnknown(throwable.toString()))
            Log.d("MainViewModel", throwable.toString())
        }
    } + CoroutineName("CatsCoroutine")
    private val _responseDataBaseStateFlow: MutableStateFlow<ResponseDataBase<Any?>> =
        MutableStateFlow(ResponseDataBase.Non)
    val responseDataBaseStateFlow: StateFlow<ResponseDataBase<Any?>> =
        _responseDataBaseStateFlow.asStateFlow()

    private val locationFlowToMain: StateFlow<ResponseHome> =
        markerManager.locationFlow.asStateFlow()

    init {
        Log.d("MainViewModel", "Attraction")
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            try {
                mockDatabase()
            } catch (throwable: Exception) {
                _sharedStateFlowError.emit(ErrorApp.FailureDataBase(throwable.toString()))
            }
            locationFlowToMain.collect {
                println("initFlowLocationHome      : I'm working in thread ${Thread.currentThread().name}")
                when (it) {
                    ResponseHome.Attraction -> {
                        Log.d("MainViewModel", "Attraction")
                        getAttraction()
                    }
                    ResponseHome.PhotoZone -> {
                        getPhotoZone()
                    }
                    ResponseHome.UserPoint -> {
                        getUserPoint()
                    }
                    ResponseHome.Loading -> {
                        clearDataFromMap()
                    }
                }
            }
        }
    }

    private suspend fun mockDatabase() {
        markerManager.attractionRepository.insertList(
            listOf(
                Attraction(name = "Екатерина собор",
                    latitude = 45.02052,
                    longitude = 38.97454,
                    img = null,
                    description = "Свято-Екатерининский кафедральный собор ",
                    startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L,
                    rating = 5.0f),
                Attraction(name = "Войсковой собор святого Благоверного Князя Александра Невского",
                    latitude = 45.01436,
                    longitude = 38.96696,
                    img = null,
                    description = "Войсковой собор святого Благоверного Князя Александра Невского",
                    startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L,
                    rating = 5.0f),
                Attraction(name = "Стадион “Краснодар”",
                    latitude = 45.04442,
                    longitude = 39.0293,
                    img = null,
                    description = "Стадион “Краснодар” ",
                    startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L,
                    rating = 5.0f)
            ))
        markerManager.photoZoneRepository.insertList(
            listOf(
                PhotoZone(name = "EasyPhoto.Studio", latitude = 45.03215, longitude = 39.02482,
                    img = null,
                    description = "EasyPhoto.Studio ",    startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L,
                    rating = 5.0f),
                PhotoZone(name = "Alice", latitude = 45.06229, longitude = 38.99264, img = null,
                    description = "Alice", startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L, rating = 5.0f),
                PhotoZone(name = "Белый осел",
                    latitude = 45.06326,
                    longitude = 38.99113,
                    img = null,
                    description = "Белый осел ",
                    startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L,
                    rating = 5.0f)
            )
        )
        markerManager.userPointRepository.insertList(
            listOf(
                UserPoint(name = "Аллея на Московской", latitude = 45.06797,
                    longitude = 39.01165, img = null,
                    description = "Аллея на Московской ",    startWork = 8 * 60 * 60 * 1000L,
                    endWork = 18 * 60 * 60 * 1000L, rating = 5.0f)
            ))
    }

    private fun getAttraction() {
        viewModelScope.launch(Dispatchers.IO) {
            println("getAttraction      : I'm working in thread ${Thread.currentThread().name}")
            markerManager.attractionRepository.getAllAttractions().collect {
                println("getAttraction      : I'm working in thread ${Thread.currentThread().name}")
                _responseDataBaseStateFlow.emit(it)
            }
        }
    }

    fun clickOnMarker(marker: Any?) {
        viewModelScope.launch(Dispatchers.IO) {
            markerManager.markerFlow.emit(marker)
        }
    }


    private fun getUserPoint() {
        viewModelScope.launch(Dispatchers.IO) {
            println("getUserPoint      : I'm working in thread ${Thread.currentThread().name}")
            markerManager.userPointRepository.getAllUserPoints().collect {
                println("getUserPoint      : I'm working in thread ${Thread.currentThread().name}")
                _responseDataBaseStateFlow.emit(it)
            }
        }
    }

    private fun getPhotoZone() {
        viewModelScope.launch(Dispatchers.IO) {
            println("getPhotoZone      : I'm working in thread ${Thread.currentThread().name}")
            markerManager.photoZoneRepository.getAllPhotoZones().collect {
                println("getPhotoZone      : I'm working in thread ${Thread.currentThread().name}")
                _responseDataBaseStateFlow.emit(it)
            }
        }
    }

    private fun clearDataFromMap() {
        viewModelScope.launch(Dispatchers.IO) {
            println("clearDataFromMap      : I'm working in thread ${Thread.currentThread().name}")
            _responseDataBaseStateFlow.emit(ResponseDataBase.Clear)
            println("clearDataFromMap      : I'm working in thread ${Thread.currentThread().name}")
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