package com.example.poidem_gulyat.ui.homeActivity.infoFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.data.repository.markers.MarkerManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class InfoViewModel(
   private val markerManager: MarkerManager,
) : ViewModel() {

    private val _sharedFlowFromMain = MutableSharedFlow<MarkerPoint>(replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedFlowFromMain = markerManager.markerFlow.asSharedFlow()
    var markerTouch :Boolean = false
    override fun onCleared() {
        Log.d("InfoViewModel", "onCleard")
        super.onCleared()
    }
}

class InfoFactory @Inject constructor(
    private val markerManager: MarkerManager,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InfoViewModel( markerManager) as T
    }
}