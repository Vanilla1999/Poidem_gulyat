package com.example.poidem_gulyat.ui.homeActivity.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.databinding.FragmentDashboardBinding
import com.example.poidem_gulyat.di.mainActivtiy.DaggerFilterFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.FilterFragmentComponent
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.ui.homeActivity.OnBackPressedFrament
import com.example.poidem_gulyat.utils.GenericItemDiff
import com.example.poidem_gulyat.utils.attraction
import com.example.poidem_gulyat.utils.textChanges
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class FilterFragment : Fragment(R.layout.fragment_dashboard), CoroutineScope, OnBackPressedFrament {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val binding: FragmentDashboardBinding by viewBinding()

    lateinit var filterComponent: FilterFragmentComponent
    private lateinit var adapter: FilterAdapter

    @Inject
    lateinit var factory: FactoryFilterView
    private val viewModelFilter by viewModels<FilterViewModel> { factory }
    private lateinit var navController:NavController
    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterComponent = DaggerFilterFragmentComponent.factory()
            .create((requireActivity() as MainActivity).activityComponent)
        filterComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding.motion Base.transitionToEnd()
       // binding.filterContainer.filterContainer.isInteractionEnabled = false
        navController= findNavController()
        initAdapter()
        initListeners()
        initCrutchMotionLayout()
    }


    private fun initCrutchMotionLayout(){
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
        // binding.filterContainer.filterContainer.isInteractionEnabled = true
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
        binding.filterContainer.filterMain.filtersTextView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    navController.navigate(R.id.listFilterFragment)
                }
            }
            true
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @SuppressLint("CheckResult")
    private fun initListeners() {
        binding.filterContainer.filterContainer2.serchView.textChanges {
            Log.d("Eddit", "Закончил")
        }.debounce(300).onEach {
            viewModelFilter.serchStateChanged(it.toString())
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        binding.filterContainer.filterContainer2.serch.setOnClickListener {
            viewModelFilter.serchStateChanged(binding.filterContainer.filterContainer2.serchView.text.toString())
        }
    }

    private fun initAdapter() {
        adapter = FilterAdapter {

        }
        adapter.setDiff(diffUtil)
        binding.filterContainer.filterContainer2.recyclerView.adapter = adapter
        binding.filterContainer.filterContainer2.recyclerView.recycledViewPool.clear()
        binding.filterContainer.filterContainer2.recyclerView.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL))
             viewModelFilter.getAllMarkers()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelFilter.sharedStateFlowDataBase.collect {
                when (it) {
                    is ResponseDataBase.Empty -> {
                        adapter.update(listOf(MarkerPoint(id = -1,name = "Войсковой собор святого Благоверного Князя Александра Невского",
                            latitude = 45.01436,
                            longitude = 38.96696,
                            img = null,
                            description = "Войсковой собор святого Благоверного Князя Александра Невского",
                            startWork = 8 * 60 * 60 * 1000L,
                            endWork = 18 * 60 * 60 * 1000L,
                            rating = 5.0f,
                            type = attraction,
                        price = 0)))
                    }
                    is ResponseDataBase.Failure -> {
                        Toast.makeText(context,
                            getString(R.string.common_error) + " " + it.errorBody,
                            Toast.LENGTH_LONG)
                            .show()
                        adapter.update(listOf(MarkerPoint(id = -1,name = "Войсковой собор святого Благоверного Князя Александра Невского",
                            latitude = 45.01436,
                            longitude = 38.96696,
                            img = null,
                            description = "Войсковой собор святого Благоверного Князя Александра Невского",
                            startWork = 8 * 60 * 60 * 1000L,
                            endWork = 18 * 60 * 60 * 1000L,
                            rating = 5.0f,
                            type = attraction,price = 1000)))
                    }
                    is ResponseDataBase.Success -> {
                        Toast.makeText(context,
                            "данные есть",
                            Toast.LENGTH_LONG)
                            .show()
                        adapter.update(it.value as List<MarkerPoint>)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelFilter.sharedStateFlowString.collect {
                adapter.filter.filter(it)
            }
        }
    }


    private val diffUtil = object : GenericItemDiff<MarkerPoint> {
        override fun isSame(
            oldItems: List<MarkerPoint>,
            newItems: List<MarkerPoint>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            val oldData = oldItems[oldItemPosition]
            val newData = newItems[newItemPosition]

            return oldData.id == newData.id
        }

        override fun isSameContent(
            oldItems: List<MarkerPoint>,
            newItems: List<MarkerPoint>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItems[oldItemPosition].adress == newItems[newItemPosition].adress &&
                    oldItems[oldItemPosition].description == newItems[newItemPosition].description &&
                    oldItems[oldItemPosition].endWork == newItems[newItemPosition].endWork &&
                    oldItems[oldItemPosition].startWork == newItems[newItemPosition].startWork &&
                    oldItems[oldItemPosition].latitude == newItems[newItemPosition].latitude &&
                    oldItems[oldItemPosition].longitude == newItems[newItemPosition].longitude &&
                    oldItems[oldItemPosition].name.equals(newItems[newItemPosition].name)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.filterContainer.filterContainer.transitionToState(R.id.end2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onBack(): Boolean {
        viewModelFilter.clearFilters()
        return false
    }

}