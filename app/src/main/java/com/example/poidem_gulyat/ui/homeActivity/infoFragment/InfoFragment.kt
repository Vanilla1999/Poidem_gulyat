package com.example.poidem_gulyat.ui.homeActivity.infoFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.databinding.FragmentInfoBinding
import com.example.poidem_gulyat.di.mainActivtiy.DaggerInfoFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.InfoFragmentComponent
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.ui.homeActivity.OnBackPressedFrament
import com.example.poidem_gulyat.utils.atStartOfDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class InfoFragment : Fragment(R.layout.fragment_info), CoroutineScope,
    OnBackPressedFrament {
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val binding: FragmentInfoBinding by viewBinding()

    lateinit var infoComponent: InfoFragmentComponent

    @Inject
    lateinit var factory: InfoFactory
    private val viewModelInfo by viewModels<InfoViewModel> { factory }
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        infoComponent = DaggerInfoFragmentComponent.factory()
            .create((requireActivity() as MainActivity).activityComponent)
        infoComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        flexWithMotion()
        initFlow()
    }

    private fun flexWithMotion(){
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
    }
    private fun initFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelInfo.sharedFlowFromMain.collect {
                it?.let { markerPoint ->
                    viewModelInfo.markerTouch = true
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
                    viewModelInfo.markerTouch = false
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("InfoViewModel", "onDestroy")
    }

    override fun onBack(): Boolean {
        return if (viewModelInfo.markerTouch) {
            binding.dopInfoHomeMain.dopInfo.transitionToStart()
            binding.motionBase.transitionToStart()
            true
        } else
            false
    }

}