package com.example.poidem_gulyat.ui.homeActivity.home

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.databinding.FragmentHomeBinding
import com.example.poidem_gulyat.di.mainActivtiy.DaggerHomeFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.HomeFragmentComponent
import com.example.poidem_gulyat.services.LocationService
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.ui.homeActivity.OnBackPressedFrament
import com.example.poidem_gulyat.utils.attraction
import com.example.poidem_gulyat.utils.photoZone
import com.example.poidem_gulyat.utils.tryCast
import com.example.poidem_gulyat.utils.userPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.osmdroid.api.IMapController
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(R.layout.fragment_home), CoroutineScope,
    OnBackPressedFrament {

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    ResponseHome.UserPoint -> {
                        binding.photoZoneButton.hide()
                        binding.attractionButton.hide()
                    }
                    ResponseHome.PhotoZone -> {
                        binding.userPointButton.hide()
                        binding.attractionButton.hide()
                    }
                    ResponseHome.Loading -> {
                        Log.d("kek", " показываем все")
                        withContext(Dispatchers.Main) {
                            binding.userPointButton.show()
                            binding.photoZoneButton.show()
                            binding.attractionButton.show()
                            binding.motionBase.transitionToStart()
                        }
                    }
                }
            }
        }
        // binding.motionBase.transitionToEnd()
        binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = false
        binding.dopInfoHomeMain.dopInfo.setTransitionListener(object :
            MotionLayout.TransitionListener {
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
                progress: Float,
            ) {
                Log.d("kek", "onTransitionChange")
            }
        })
        binding.dopInfoHomeMain.dopInfoHome.dopInfoHome.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = false
                    Log.d("kek", "ACTION_UP")
                }
                MotionEvent.ACTION_DOWN -> {
                    binding.dopInfoHomeMain.dopInfo.isInteractionEnabled = true
                    binding.dopInfoHomeMain.dopInfo.dispatchTouchEvent(event)
                    Log.d("kek", "ACTION_DOWN")
                }
                MotionEvent.ACTION_HOVER_MOVE -> {
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
        initFlow()
    }

    private fun atStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }


    private fun initFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelHome.markerSharedFlow.collect {
                it?.let { markerPoint ->
                    viewModelHome.markerTouch = true
                    binding.motionBase.transitionToEnd()
                    binding.dopInfoHomeMain.dopInfoHome.nameTextView.text = markerPoint.name
                    binding.dopInfoHomeMain.dopInfoHome.rating.rating =
                        markerPoint.rating ?: 0.0f
                    binding.dopInfoHomeMain.dopInfoHome.descriptionTextView.text =
                        markerPoint.description
                    markerPoint.endWork?.let {
                        val currentDate = Date(System.currentTimeMillis())
                        val timeToClose =
                            atStartOfDay(currentDate).time + markerPoint.endWork
                        binding.dopInfoHomeMain.dopInfoHome.workTimeTextView.text =
                            if (timeToClose > currentDate.time) getString(R.string.works_pattern,
                                markerPoint.endWork / (60 * 60 * 1000L))
                            else getString(R.string.close)
                    } ?: run {
                        binding.dopInfoHomeMain.dopInfoHome.workTimeTextView.text =
                            getString(R.string.time_not_specified)
                    }
                } ?: run {
                    viewModelHome.markerTouch = false
                }
            }
        }
    }

    override fun onResume() {

        super.onResume();
        //   binding.map.onResume();
        Log.d("onResume", "onResume onResume")
        firstOpen = true
    }

    override fun onPause() {
        super.onPause();
        Log.d("onPause", "onPause onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineContext.cancelChildren()
    }

    override fun onBack(): Boolean {
        return if (viewModelHome.markerTouch) {
            binding.dopInfoHomeMain.dopInfo.transitionToStart()
            binding.motionBase.transitionToStart()
            true
        } else
            false
    }


}