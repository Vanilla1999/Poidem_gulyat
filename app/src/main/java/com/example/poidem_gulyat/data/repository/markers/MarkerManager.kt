package com.example.poidem_gulyat.data.repository.markers

import javax.inject.Inject

data class MarkerManager @Inject constructor(
     val attractionRepository: AttractionRepository,
     val photoZoneRepository: PhotoZoneRepository,
     val userPointRepository: UserPointRepository,
) {
}