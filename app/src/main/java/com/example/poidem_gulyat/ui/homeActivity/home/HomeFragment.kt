package com.example.poidem_gulyat.ui.homeActivity.home

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.App
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.customView.geo.CustomMe
import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.databinding.FragmentHomeBinding
import com.example.poidem_gulyat.di.mainActivtiy.DaggerHomeFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.HomeFragmentComponent
import com.example.poidem_gulyat.services.LocationService
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(R.layout.fragment_home), ServiceConnection, CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private var firstOpen = true
    private var context: MainActivity? = null
    private var locationService: LocationService? = null
    lateinit var homeComponent: HomeFragmentComponent
    private lateinit var mapController: IMapController
    private var locationUpdatesJob: Job? = null

    @Inject
    lateinit var factory: FactoryHomeView
    private val viewModelHome by viewModels<HomeViewModel> { factory }

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as MainActivity
        homeComponent = DaggerHomeFragmentComponent.factory()
            .create((requireActivity() as MainActivity).activityComponent)
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
//        binding.map.setTileSource(TileSourceFactory.MAPNIK)
//        // человечек на карте
//        val locationOverlay = CustomMe(binding.map);
//        binding.map.overlays.add(locationOverlay)
//        // повороты
//        val rotationGestureOverlay = RotationGestureOverlay(context, binding.map);
//        rotationGestureOverlay.isEnabled
//        binding.map.setMultiTouchControls(true);
//        binding.map.overlays.add(rotationGestureOverlay);
        // инициализация карты типа координата, тут нужен будет сервис координат. +анимация загрузки прикольная
        binding.attractionButton.setOnClickListener {
            viewModelHome.attractionButtonClick()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelHome.buttonStateFlow.collect {
                when (it) {
                    ResponseHome.Attraction -> {
                        binding.userPointButton.hide()
                        binding.photoZoneButton.hide()
                    }
                    else -> {}
                }
            }
        }
        binding.kek.setOnTouchListener { _, _ ->
            Log.d("HomeFragment", "onTouch")
            false
        }
    }

    private fun initFlow() {
        locationUpdatesJob = lifecycleScope.launch {
            viewModelHome.locationFlow!!.collect {
                when (it) {
                    is ResponseSplash.Success -> {
                        val location = (it.value as Location)
                        if (firstOpen) {
//                            mapController.setZoom(15.0)
//                            mapController.animateTo(GeoPoint(location.latitude, location.longitude))
                            firstOpen = false
                        }
                        Log.d("kek", (it.value as Location).latitude.toString())
                        //(binding.map.overlays[0] as CustomMe).setLocation(location)
                        //binding.map.visibility = View.VISIBLE
                    }
                    is ResponseSplash.Failure -> Log.d("kek", " что то случилось")
                    else -> {}
                }
            }
        }
    }

    override fun onResume() {

        super.onResume();
        //   binding.map.onResume();
        Log.d("onResume", "onResume onResume")
        // LocationService.customBindService(context as Context, this)
        // mapController = binding.map.controller
        firstOpen = true
    }

    override fun onPause() {
        super.onPause();
        Log.d("onPause", "onPause onPause")
        //  binding.map.onPause();
        // LocationService.customUnbindService(context as Context, this)
        locationService?.locationServiceListener = null
        locationService = null
        //viewModelHome.locationFlow = null
        // getInstance().save(context, PreferenceManager.getDefaultSharedPreferences(context))
        //  locationUpdatesJob!!.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineContext.cancelChildren()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d("onServiceConnected", "service подключен")
        service as LocationService.LocationServiceBinder
        locationService = service.getService()
        viewModelHome.locationFlow = locationService?.locationFlow
        //  initFlow()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.d("onServiceDisconnected", "service onServiceDisconnected")
        locationService = null
        viewModelHome.locationFlow = null
    }

}