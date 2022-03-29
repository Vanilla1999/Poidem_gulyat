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

    private val binding: FragmentListFilterBinding by viewBinding()

    lateinit var listFilterComponent: ListFilterFragmentComponent
    private lateinit var adapter: ListFilterAdapter

    @Inject
    lateinit var factory: FactoryListFilterView
    private val viewModelFilter by viewModels<ListFilterViewModel> { factory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listFilterComponent =DaggerListFilterFragmentComponent.factory()
            .create((requireActivity() as MainActivity).activityComponent)
        listFilterComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListeners()
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
                    is ResponseDataBase.Success->{
                        adapter.update(it.value,diffUtil)
                    }
                    is ResponseDataBase.Failure ->{
                        Toast.makeText(context,
                            getString(R.string.common_error) + " " + it.errorBody,
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    is ResponseDataBase.Empty ->{

                    }
                }
            }
        }
    }
    private val diffUtil = object : GenericItemDiff<Filter> {
        override fun isSame(
            oldItems: List<Filter>,
            newItems: List<Filter>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            val oldData = oldItems[oldItemPosition]
            val newData = newItems[newItemPosition]
            return oldData.id == newData.id
        }

        override fun isSameContent(
            oldItems: List<Filter>,
            newItems: List<Filter>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItems[oldItemPosition].attraction == newItems[newItemPosition].attraction &&
                    oldItems[oldItemPosition].bestNearby == newItems[newItemPosition].bestNearby &&
                    oldItems[oldItemPosition].paid == newItems[newItemPosition].paid &&
                    oldItems[oldItemPosition].photoZone == newItems[newItemPosition].photoZone &&
                    oldItems[oldItemPosition].rating == newItems[newItemPosition].rating &&
                    oldItems[oldItemPosition].userPoint == newItems[newItemPosition].userPoint &&
                    oldItems[oldItemPosition].open == newItems[newItemPosition].open &&
                    oldItems[oldItemPosition].close == newItems[newItemPosition].close &&
                    oldItems[oldItemPosition].free == newItems[newItemPosition].free

        }
    }

    override fun onBack(): Boolean {
       return false
    }
}