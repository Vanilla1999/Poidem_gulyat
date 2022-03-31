package com.example.poidem_gulyat.ui.homeActivity.dashboard.listFilter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.databinding.FragmentDashboardBinding
import com.example.poidem_gulyat.databinding.FragmentListFilterBinding
import com.example.poidem_gulyat.di.mainActivtiy.DaggerFilterFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.DaggerListFilterFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.FilterFragmentComponent
import com.example.poidem_gulyat.di.mainActivtiy.ListFilterFragmentComponent
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.ui.homeActivity.OnBackPressedFrament
import com.example.poidem_gulyat.ui.homeActivity.dashboard.FactoryFilterView
import com.example.poidem_gulyat.ui.homeActivity.dashboard.FilterAdapter
import com.example.poidem_gulyat.ui.homeActivity.dashboard.FilterFragmentDirections
import com.example.poidem_gulyat.ui.homeActivity.dashboard.FilterViewModel
import com.example.poidem_gulyat.utils.GenericItemDiff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext



class ListFilterFragment : Fragment(R.layout.fragment_list_filter), CoroutineScope,
    OnBackPressedFrament {
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private lateinit var navController: NavController
    private val binding: FragmentListFilterBinding by viewBinding()

    lateinit var listFilterComponent: ListFilterFragmentComponent
    private lateinit var adapter: ListFilterAdapter
private lateinit  var mContext:Context
    @Inject
    lateinit var factory: FactoryListFilterView
    private val viewModelFilter by viewModels<ListFilterViewModel> { factory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listFilterComponent =DaggerListFilterFragmentComponent.factory()
            .create((requireActivity() as MainActivity).activityComponent)
        listFilterComponent.inject(this)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        navController = findNavController()
        initListeners()
        initView()
    }

    fun initView(){
        binding.customText1.setPaintBlackFull()
        binding.customText2.setPaintBlackStroke()
        binding.customText1.setlistener{
            navController.popBackStack()
        }
        binding.customText2.setValues(mContext.getString(R.string.filter_list_delete))
        binding.customText2.setlistener {viewModelFilter.changeFilter(Filter())}
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ListFilterFragment","onDestroy")
    }
    private fun initAdapter(){
        adapter = ListFilterAdapter {
            viewModelFilter.changeFilter(it)
        }
        binding.recyclerView.adapter = adapter
        viewModelFilter.getAllFilters()

        //adapter.update(),diffUtil)

    }
    private fun initListeners (){
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelFilter.sharedStateFlowDataBase.collect {
                when(it){
                    is ResponseDataBase.SuccessNotList->{
                        adapter.setFilter(it.value)
                    }
                    is ResponseDataBase.Failure ->{
                        Toast.makeText(context,
                            getString(R.string.common_error) + " " + it.errorBody,
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    is ResponseDataBase.Empty ->{
                        adapter.clear()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModelFilter.stateFlowSizeMarkers.collect { binding.customText1.setValues(mContext.getString(R.string.filter_listview_result_size,it)) } }
    }


    override fun onBack(): Boolean {
       return false
    }
}