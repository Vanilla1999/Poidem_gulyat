package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.data.repository.filter.FiltersRepository
import com.example.poidem_gulyat.utils.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.*

data class MarkerManager constructor(
    val markerRepository: MarkerRepository,
    val filtersRepository: FiltersRepository,
) {
    val locationFlow: MutableStateFlow<ResponseHome> = MutableStateFlow(ResponseHome.Non)
    val markerFlow: MutableSharedFlow<MarkerPoint?> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 1, BufferOverflow.DROP_OLDEST)
    val markerPointFromFilters: MutableSharedFlow<List<MarkerPoint>> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 0, BufferOverflow.DROP_OLDEST)
    val markerFlowToMain: MutableSharedFlow<MarkerPoint?> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 0, BufferOverflow.DROP_OLDEST)

    fun getAllMarkers(): Flow<ResponseDataBase<MarkerPoint>> {
        return filtersRepository.getFilters().transform {
            when (it) {
                is ResponseDataBase.SuccessNotList -> {
                    markerRepository.getAllMarksers()
                        .collect { markers: ResponseDataBase<MarkerPoint> ->
                            emit(markers)
                        }
                }
                is ResponseDataBase.Empty -> {
                    markerRepository.getAllMarksers()
                        .collect { markers: ResponseDataBase<MarkerPoint> ->
                            emit(markers)
                        }
                }
                else -> {}
            }
        }
    }

    fun getAllMarkersWithFilter(): Flow<ResponseDataBase<MarkerPoint>> {
        return flow {
            markerRepository.getAllMarksers().collect { markers: ResponseDataBase<MarkerPoint> ->
                when (markers) {
                    is ResponseDataBase.Success -> {
                        filtersRepository.getFilters().collect { filter: ResponseDataBase<Filter> ->
                            when (filter) {
                                is ResponseDataBase.SuccessNotList -> {
                                    emit(ResponseDataBase.Success(filterMarkerList(markers.value,filter.value)))
                                }
                                is ResponseDataBase.Failure -> {
                                    emit(filter)
                                }
                                is ResponseDataBase.Empty ->{ emit(markers)}
                            }
                        }
                    }
                    is ResponseDataBase.Empty -> {
                        emit(markers)
                    }
                    is ResponseDataBase.Failure->{ emit(markers)}
                    else -> {}
                }
            }
        }
    }

    private fun filterMarkerList(markers:List<MarkerPoint>,filter: Filter):List<MarkerPoint>{
        var markserList =  markers
        if (filter.typeMarker != empty)
            markserList= markserList.filter { it.type == filter.typeMarker }
        if (filter.rating != empty)
            markserList=  markserList.filter { it.rating == filter.rating.toFloat() }
        if (filter.price != empty && filter.price != 1)
            markserList=  markserList.filter { it.price > empty }
        when (filter.open) {
            open ->{
                val currentDate = Date(System.currentTimeMillis())
                markserList=  markserList.filter { marker: MarkerPoint -> if(marker.endWork != null && marker.startWork !=null )
                    (atStartOfDay(currentDate).time + marker.endWork) < (atStartOfDay(currentDate).time + currentDate.time) &&
                            (atStartOfDay(currentDate).time + marker.startWork) > (atStartOfDay(currentDate).time + currentDate.time)else true
                }
            }
            closed->{
                val currentDate = Date(System.currentTimeMillis())
                markserList=  markserList.filter { marker: MarkerPoint -> if(marker.endWork != null && marker.startWork !=null )
                    (atStartOfDay(currentDate).time + marker.endWork) > (atStartOfDay(currentDate).time + currentDate.time) &&
                            (atStartOfDay(currentDate).time + marker.startWork) < (atStartOfDay(currentDate).time + currentDate.time)else true
                }
            }
        }
        when(filter.typeSort){
            sortByRating ->{
                markserList= markserList.sortedBy { it.rating }
            }
            bestNearBy ->{
                markserList= markserList.sortedBy { it.rating }
            }
        }
        return markserList
    }

    fun getFilters(): Flow<ResponseDataBase<Filter>> {
        return filtersRepository.getFilters()
    }


}
