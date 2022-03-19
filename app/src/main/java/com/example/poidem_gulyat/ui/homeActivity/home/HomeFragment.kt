package com.example.poidem_gulyat.ui.homeActivity.home

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
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
        binding.userPointButton.setOnClickListener {
            viewModelHome.userPointButtonClick()
        }
        binding.photoZoneButton.setOnClickListener {
            viewModelHome.photoZoneButtonClick()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelHome.buttonStateFlow.collect {
                when (it) {
                    ResponseHome.Attraction -> {
                        binding.userPointButton.hide()
                        binding.photoZoneButton.hide()
                    }
                    ResponseHome.UserPoint ->{
                        binding.photoZoneButton.hide()
                        binding.attractionButton.hide()
                    }
                    ResponseHome.PhotoZone ->{
                        binding.userPointButton.hide()
                        binding.attractionButton.hide()
                    }
                    ResponseHome.Loading->{
                        Log.d("kek", " показываем все")
                        withContext(Dispatchers.Main) {
                            binding.userPointButton.show()
                            binding.photoZoneButton.show()
                            binding.attractionButton.show()
                        }
                    }
                }
            }
        }
       // binding.motionBase.transitionToEnd()
        binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = false
        binding.dopInfoHomeMain.dopInfo.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = false
                Log.d("kek", "onTransitionCompleted")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.d("kek", "onTransitionTrigger")
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.d("kek", "onTransitionStarted")
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                Log.d("kek", "onTransitionChange")
            }
        })
        binding.dopInfoHomeMain.dopInfoHome.dopInfoHome.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_UP ->{
                    binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = false
                    Log.d("kek", "ACTION_UP")
                }
                MotionEvent.ACTION_DOWN ->{
                    binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = true
                    binding.dopInfoHomeMain.dopInfo.dispatchTouchEvent(event)
                    Log.d("kek", "ACTION_DOWN")
                }
                MotionEvent.ACTION_HOVER_MOVE ->{
                    binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = true
                    binding.dopInfoHomeMain.dopInfo.dispatchTouchEvent(event)
                    Log.d("kek", "ACTION_HOVER_MOVE")
                }
            }
            true
        }
        binding.fragmentHome.setOnTouchListener { _, _ ->
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