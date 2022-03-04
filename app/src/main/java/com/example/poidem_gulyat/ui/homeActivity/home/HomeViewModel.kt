package com.example.poidem_gulyat.ui.homeActivity.home

import android.location.Location
import androidx.lifecycle.*
import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.data.dto.LoginResponse
import com.example.poidem_gulyat.data.repository.AuthRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.source.local.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
) : ViewModel() {

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
        locationFlow = null
    }
}


class FactoryHomeView @Inject constructor(
    private val userPreferences: UserPreferences,
    private val gpsRepository: GpsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userPreferences, gpsRepository) as T
    }
}