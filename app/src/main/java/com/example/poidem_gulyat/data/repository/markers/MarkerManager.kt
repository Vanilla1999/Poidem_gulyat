package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.data.dto.MarkerPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class MarkerManager constructor(
    val markerRepository: MarkerRepository
) {
    val locationFlow: MutableStateFlow<ResponseHome> = MutableStateFlow(ResponseHome.Non)
    val markerFlow: MutableSharedFlow<MarkerPoint?> = MutableSharedFlow(replay = 1, extraBufferCapacity = 0,BufferOverflow.DROP_OLDEST)
}
