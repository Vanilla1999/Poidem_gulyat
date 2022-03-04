package com.example.poidem_gulyat.data.source.gps

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.example.poidem_gulyat.di.ApplicationContext
import com.example.poidem_gulyat.utils.toStringOrEmpty
import javax.inject.Inject

class GpsDataSourceImpl @Inject constructor( @ApplicationContext context: Context) : GpsDataSource, InnerLocationListener {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var gpsListener: GpsListener? = null
    private var location: Location? = null
    private var locationNetwork: Location? = null

    private val multiLocationListener =
        MultiLocationListener("wifi",this, this::requestCurrentLocation)

    private val singleLocationListener =
        SingleLocationListener(this, this::requestCurrentLocation)

    override fun onStartListen(minUpdatePeriod: Long, minUpdateDistance: Float) {
        try {
            //locationManager.removeUpdates(multiLocationListener)
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minUpdatePeriod,
                    minUpdateDistance,
                    multiLocationListener
                )
            }
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: SecurityException) {
            location = null
        }
    }

    override fun onStopListen() {
        locationManager.removeUpdates(multiLocationListener)
        locationManager.removeUpdates(singleLocationListener)
    }

    override fun requestCurrentLocation() {
        try {
            Log.d("requestCurrentLocation", "Hello from biometry")
            locationManager.removeUpdates(singleLocationListener)

            locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER, singleLocationListener, Looper.getMainLooper()
            )

        } catch (e: SecurityException) {
            singleLocationFailure(e)
            Log.e("GpsDataSourceImpl", e.message.toStringOrEmpty(), e)
        } catch (e: Exception) {
            singleLocationFailure(e)
            Log.e("GpsDataSourceImpl", e.message.toStringOrEmpty(), e)
        }
    }


    override fun getLastKnownLocation(): Location? {
        return try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        } catch (e: SecurityException) {
            null
        }
    }

    override fun setListener(listener: GpsListener?) {
        this.gpsListener = listener
    }

    override fun hasProvider(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun singleLocationUpdate(newLocation: Location) {
        locationManager.removeUpdates(singleLocationListener)
            gpsListener?.onLocationUpdate(newLocation)
    }

    override fun singleLocationFailure(throwable: Throwable) {
        gpsListener?.onLocationFailure(throwable)
    }


    override fun multiLocationUpdate(newLocation: Location,name: String) {
        newLocation.apply {
            gpsListener?.onLocationUpdate(newLocation)
        }
    }

    override fun multiLocationFailure(throwable: Throwable) {
        gpsListener?.onLocationFailure(throwable)
    }
}

private class SingleLocationListener(
    val innerLocationListener: InnerLocationListener,
    val onEnable: () -> Unit,
) : LocationListener {

    override fun onLocationChanged(newLocation: Location) =
        innerLocationListener.singleLocationUpdate(newLocation)

    override fun onProviderEnabled(provider: String) = onEnable.invoke()

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderDisabled(provider: String) {}
}

private class MultiLocationListener(
    val name:String,
    val innerLocationListener: InnerLocationListener,
    val onEnable: () -> Unit,
) : LocationListener {

    override fun onLocationChanged(newLocation: Location) =
        innerLocationListener.multiLocationUpdate(newLocation,name)

    override fun onProviderEnabled(provider: String) = onEnable.invoke()

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {}
    override fun onProviderDisabled(provider: String) {}
}

private interface InnerLocationListener {
    fun singleLocationUpdate(newLocation: Location)
    fun singleLocationFailure(throwable: Throwable)
    fun multiLocationUpdate(newLocation: Location,name: String)
    fun multiLocationFailure(throwable: Throwable)
}