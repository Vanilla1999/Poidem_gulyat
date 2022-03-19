package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.ResponseSplash
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class MarkerManager constructor(
    val attractionRepository: AttractionRepository,
    val photoZoneRepository: PhotoZoneRepository,
    val userPointRepository: UserPointRepository,
) {
    val locationFlow: MutableStateFlow<ResponseHome> = MutableStateFlow(ResponseHome.Non)
}
