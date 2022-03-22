package com.example.poidem_gulyat.ui.homeActivity.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.markers.MarkerManager
import com.example.poidem_gulyat.data.source.local.UserPreferences
import com.example.poidem_gulyat.ui.homeActivity.home.HomeViewModel
import javax.inject.Inject

class FilterViewModel(
    private val userPreferences: UserPreferences,
    private val markerManager: MarkerManager
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    override fun onCleared() {
        Log.d("FilterViewModel", "onCleard")
        super.onCleared()
    }
}
class FactoryFilterView @Inject constructor(
    private val userPreferences: UserPreferences,
    private val markerManager: MarkerManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilterViewModel(userPreferences,markerManager) as T
    }
}