package com.example.poidem_gulyat.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.poidem_gulyat.App
import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.data.dto.LoginResponse
import com.example.poidem_gulyat.data.dto.UserLocation
import kotlinx.coroutines.*
import com.example.poidem_gulyat.data.source.gps.GpsListener
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.location.UserLocationRepository
import com.example.poidem_gulyat.di.locationService.DaggerLocationServiceComponent
import com.example.poidem_gulyat.di.locationService.LocationServiceComponent
import com.example.poidem_gulyat.di.mainActivtiy.DaggerHomeFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.HomeFragmentComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class LocationService : Service(), CoroutineScope, GpsListener {
    private val binder: IBinder = LocationServiceBinder()
    var locationServiceListener: LocationServiceListener? = null

    private val _locationFlow: MutableStateFlow<ResponseSplash<Any?>> = MutableStateFlow(
        ResponseSplash.Loading)
    val locationFlow: StateFlow<ResponseSplash<Any?>> = _locationFlow.asStateFlow()
    lateinit var locationServiceComponent: LocationServiceComponent
    @Inject
    lateinit var gpsRepository: GpsRepository

    @Inject
    lateinit var userLocationRepository: UserLocationRepository

    private val minUpdateTimeSeconds = 40L
    private val minDistanceMeters = 1F
    private val timeToTryGetLocation = 1 * 60 * 1000L

    /**
     * Status code
     * 0 - значение по умолчанию(успешно)
     * 1 - выключен модуль GPS
     * 2 - получена старая координата
     * 3 - устройство не получило свежую коордиату и хранит старую последнюю координату
     * */
    private var reasonCodeOfWrongLocation: Int = 0
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    override fun onCreate() {
        locationServiceComponent = DaggerLocationServiceComponent.factory().create(App.appComponentMain)
        locationServiceComponent.inject(this)
        Log.d("onCreateLocatoin", "service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extras = intent?.extras
        val command = extras?.getInt(SERVICE_TASK) ?: START_SERVICE

        when (command) {
            START_SERVICE -> {
                Log.e("START_SERVICE", " Координата записывается")
                initLocationSearching()}
            STOP_SERVICE ->
                stopSelf()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // initLocationSearching()
        Log.e("LocationUpdate", " Координата записывается")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        locationServiceListener = null
        return super.onUnbind(intent)
    }

    inner class LocationServiceBinder : Binder() {
        fun getService(): LocationService {
            Log.e("LocationUpdate", " Координата записывается2")
            return this@LocationService
        }
    }

    override fun onDestroy() {
        stopListenLocation()
        Log.e("LocationUpdate", " Координата не записывается ")
        job.cancelChildren()
        super.onDestroy()
    }

    private fun stopListenLocation() {
        gpsRepository.setLocationListener(null)
        gpsRepository.stopListen()
    }

//    private fun stopStateView(){
//        _locationFlow.emit(ResponseSplash.Loading)
//    }
//
//    private fun startStateView(){
//        _locationFlow
//    }

    private fun initLocationSearching() {
        gpsRepository.setLocationListener(this)
        gpsRepository.startListen(minUpdateTimeSeconds, minDistanceMeters)
        //gpsRepository.requestCurrentLocation()
    }


    private fun saveUserCoordinates(location: Location) {
        async(Dispatchers.IO) {
            userLocationRepository.insert(
                UserLocation(
                    Date().time / 1000,
                    location.latitude,
                    location.longitude,
                    location.provider,
                    location.accuracy
                )
            )
        }.invokeOnCompletion {

        }
    }

    companion object {
        const val TAG = "LocationService"
        private const val THREE_MINUTES_FOR_CHECK = 3 * 60
        private const val THREE_MINUTES = 3 * 60 * 1000L
        private const val SERVICE_TASK = "SERVICE_TASK"
        private const val START_SERVICE = 100
        private const val STOP_SERVICE = 101
        private const val STOP_CHECK = 101


        @JvmStatic
        fun customBindService(context: Context, connection: ServiceConnection) {
            val intent = Intent(context, LocationService::class.java)
            Log.e("customBindService", " customBindService ")
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        @JvmStatic
        fun customUnbindService(context: Context, connection: ServiceConnection) {
            Log.e("customUnbindService", " customUnbindService ")
            context.unbindService(connection)
        }


        @JvmStatic
        fun startLocation(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            intent.putExtra(SERVICE_TASK, START_SERVICE)
            Log.e("startLocation", " startLocation ")
            context.startService(intent)
        }

        @JvmStatic
        fun stopService(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            Log.e("stopService", " stopService ")
            context.stopService(intent)
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

    //нужна проверка всех трех координат.
    // Грубо говоря, если они пипец как отличаются, то жопа
    override fun onLocationUpdate(location: Location) {
        launch(Dispatchers.IO) {
            if (location.accuracy < 200) {
                saveUserCoordinates(location)
                _locationFlow.emit(ResponseSplash.Success(location))
                Log.d("LocationUpdate",
                    location.latitude.toString() + " Координата, хорошая")
            } else {
                Log.d("LocationUpdateWitoutChk",
                    location.latitude.toString() + " координата кривая")
            }
        }
    }


    override fun onLocationFailure(throwable: Throwable) {
        Log.d("onLocationFailure",
            "$throwable чет пошло не так")
    }

    interface LocationServiceListener {
        fun locationWasReceived(locaton : Location)
    }
}
