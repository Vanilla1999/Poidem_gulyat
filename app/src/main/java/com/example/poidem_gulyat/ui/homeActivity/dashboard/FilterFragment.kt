package com.example.poidem_gulyat.ui.homeActivity.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.databinding.FilterContainerBinding
import com.example.poidem_gulyat.databinding.FilterSerchFiltersBinding
import com.example.poidem_gulyat.databinding.FragmentDashboardBinding
import com.example.poidem_gulyat.databinding.FragmentHomeBinding
import com.example.poidem_gulyat.di.mainActivtiy.DaggerFilterFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.DaggerHomeFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.FilterFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.HomeFragmentComponent
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.ui.homeActivity.OnBackPressedFrament
import com.example.poidem_gulyat.ui.homeActivity.home.FactoryHomeView
import com.example.poidem_gulyat.ui.homeActivity.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FilterFragment : Fragment(R.layout.fragment_dashboard), CoroutineScope, OnBackPressedFrament {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val binding: FragmentDashboardBinding by viewBinding()

    lateinit var filterComponent: FilterFragmentComponent
    @Inject
    lateinit var factory: FactoryFilterView
    private val viewModelFilter by viewModels<FilterViewModel> { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterComponent = DaggerFilterFragmentComponent.factory()
            .create((requireActivity() as MainActivity).activityComponent)
        filterComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding.motion Base.transitionToEnd()
        binding.filterContainer.filterContainer.isInteractionEnabled = false
        binding.filterContainer.filterContainer.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                binding.filterContainer.filterContainer.isInteractionEnabled = false
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
        binding.filterContainer.filterMain.filterLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    binding.filterContainer.filterContainer.isInteractionEnabled = false
                   // binding.filterContainer.filterMain.filterLayout.isInteractionEnabled = false
                    Log.d("kek", "ACTION_UP")
                }
                MotionEvent.ACTION_DOWN -> {
                    binding.filterContainer.filterContainer.isInteractionEnabled = true
                   // binding.filterContainer.filterMain.filterLayout.isInteractionEnabled = true
                    binding.filterContainer.filterContainer.dispatchTouchEvent(event)
                    Log.d("kek", "ACTION_DOWN")
                }
                MotionEvent.ACTION_HOVER_MOVE -> {
                    binding.filterContainer.filterContainer.isInteractionEnabled = false
                   // binding.filterContainer.filterMain.filterLayout.isInteractionEnabled = false
                    binding.filterContainer.filterContainer.dispatchTouchEvent(event)
                    Log.d("kek", "ACTION_HOVER_MOVE")
                }
            }
            true
        }
        // это надо сохранять во viewModel//
        var id = 0
        binding.filterContainer.filterMain.nameFilterTextView.setOnTouchListener { _, event ->
            binding.filterContainer.filterContainer.isInteractionEnabled = true
            when(event.action){
                MotionEvent.ACTION_UP ->{
                    Log.d("nameFilterTextView", "ACTION_UP")
                    if(id == 0 ) {
                        binding.filterContainer.filterMain.filterLayout.transitionToState(R.id.end2)
                        id=1
                    }else{
                        binding.filterContainer.filterContainer.isInteractionEnabled = true
                        binding.filterContainer.filterMain.filterLayout.transitionToState(R.id.start_filter)
                        id=0
                    }
                }
            }
            binding.filterContainer.filterContainer.isInteractionEnabled = false
            true
        }
        binding.filterContainer.filterMain.filtersTextView.setOnTouchListener { _, event ->
            binding.filterContainer.filterContainer.isInteractionEnabled = true
            when(event.action){
                MotionEvent.ACTION_UP -> {
                    Log.d("filtersTextView", "ACTION_UP")
                    if(id == 0 ) {
                        binding.filterContainer.filterMain.filterLayout.transitionToState(R.id.end_filter)
                        id=1
                    }else{
                        binding.filterContainer.filterMain.filterLayout.transitionToState(R.id.start_filter)
                        id=0
                    }
                }
                }
            binding.filterContainer.filterContainer.isInteractionEnabled = false
            true
        }


    }

    override fun onResume() {
        super.onResume()
        binding.filterMotionBase.transitionToEnd()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onBack(): Boolean {
        return false
    }

}