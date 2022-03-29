package com.example.poidem_gulyat.ui.homeActivity.dashboard.listFilter


import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.camera2.CameraManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.datastore.preferences.protobuf.Empty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.databinding.*
import com.example.poidem_gulyat.utils.*
import com.example.poidem_gulyat.utils.customView.CustomTitleFilter
import com.example.poidem_gulyat.utils.customView.DrawListener

import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*

class ListFilterAdapter(
    private val onFilterCallback: (Filter) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var filterList = emptyList<Filter>()


    private val itemClick: (Filter) -> Unit =
        { filter: Filter -> onFilterCallback(filter) }

    //  @LayoutRes private val layoutRes: Int,
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SortBy -> {
                val binding = ItemRecyclerViewFiltersSortbyBinding.inflate(inflater, parent, false)
                ViewHolderSortBy(
                    binding, itemClick
                )
            }
            Type -> {
                val binding = ItemRecyclerViewFiltersTypesBinding.inflate(inflater, parent, false)
                ViewHolderType(
                    binding, itemClick
                )
            }
            Rating -> {
                val binding = ItemRecyclerViewFiltersRatingBinding.inflate(inflater, parent, false)
                ViewHolderRating(
                    binding, itemClick
                )
            }
            Availability -> {
                val binding =
                    ItemRecyclerViewFiltersAvailabilityBinding.inflate(inflater, parent, false)
                ViewHolderAvailability(
                    binding, itemClick
                )
            }
            else -> throw IllegalStateException("Incorrect view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            SortBy -> {
                (holder as ViewHolderSortBy).bindView(filterList[position])
            }
            Type -> {
                (holder as ViewHolderType).bindView(filterList[position])
            }
            Rating -> {
                (holder as ViewHolderRating).bindView(filterList[position])
            }
            Availability -> {
                (holder as ViewHolderAvailability).bindView(filterList[position])
            }
        }

    }

    // чтоб использовать разные ViewHolder.
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            SortBy -> SortBy

            Type -> Type

            Rating -> Rating

            Availability -> Availability
            else -> {
                5
            }
        }
    }


    fun update(items: List<Filter>, diff: GenericItemDiff<Filter>) {
        val diffCallback = GenericDiffUtil(filterList, items, diff)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        try {
            filterList = items
            diffResult.dispatchUpdatesTo(this)

        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    inner class ViewHolderSortBy(
        private val item: ItemRecyclerViewFiltersSortbyBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        @SuppressLint("SetTextI18n")
        fun bindView(model: Filter) {
            item.customText1.setValues(item.root.context.getString(R.string.filter_sortBy_nearBy))
            item.customText1.isActive(model.bestNearby)
            item.customText1.setlistener {
                    if (model.bestNearby == 0)
                        model.bestNearby = 1 else model.bestNearby = 0
                    itemClick(model)
                }
            item.customText2.setValues(item.root.context.getString(R.string.filter_sortBy_rating))
            item.customText2.isActive(model.sortByRating)
            item.customText2.setlistener {
                if (model.sortByRating == 0)
                    model.sortByRating = 1 else model.sortByRating = 0
                itemClick(model)
            }
        }
    }

    inner class ViewHolderType(
        private val item: ItemRecyclerViewFiltersTypesBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        @SuppressLint("SetTextI18n")
        fun bindView(model: Filter) {
            item.customText1.setValues(item.root.context.getString(R.string.filter_type_attraction))
            item.customText1.isActive(model.attraction)
            item.customText1.setlistener{

                    if (model.attraction == 0)
                        model.attraction = 1 else model.attraction = 0
                    itemClick(model)

            }
            item.customText2.setValues(item.root.context.getString(R.string.filter_type_userPoint))
            item.customText2.isActive(model.userPoint)
            item.customText2.setlistener{

                    if (model.userPoint == 0)
                        model.userPoint = 1 else model.userPoint = 0
                    itemClick(model)
                }

            item.customText3.setValues(item.root.context.getString(R.string.filter_type_photoZone))
            item.customText3.isActive(model.photoZone)
            item.customText3.setlistener {
                    if (model.photoZone == 0)
                        model.photoZone = 1 else model.photoZone = 0
                    itemClick(model)
                }

        }
    }

    inner class ViewHolderRating(
        private val item: ItemRecyclerViewFiltersRatingBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {


        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bindView(model: Filter) {
            when(model.rating){
                1 -> {item.rating2.background = item.root.context.getDrawable(R.color.gray) }
                2 -> {item.rating5.background = item.root.context.getDrawable(R.color.gray) }
                3 -> {item.rating4.background = item.root.context.getDrawable(R.color.gray) }
                4 -> {item.rating3.background = item.root.context.getDrawable(R.color.gray) }
                5 -> {item.rating1.background = item.root.context.getDrawable(R.color.gray) }
            }
            item.rating1.setOnClickListener {
                if( model.rating == 5)
                    model.rating = 0 else  model.rating = 5
                itemClick(model)
            }
            item.rating3.setOnClickListener {
                if( model.rating == 4)
                    model.rating = 0 else  model.rating = 4
                itemClick(model)
            }
            item.rating4.setOnClickListener {
                if( model.rating == 3)
                    model.rating = 0 else  model.rating = 3
                itemClick(model)
            }
            item.rating5.setOnClickListener {
                if( model.rating == 2)
                    model.rating = 0 else  model.rating = 2
                itemClick(model)
            }
            item.rating2.setOnClickListener {
                if( model.rating == 1)
                    model.rating = 0 else  model.rating = 1
                itemClick(model)
            }
        }

    }

    inner class ViewHolderAvailability(
        private val item: ItemRecyclerViewFiltersAvailabilityBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        @SuppressLint("SetTextI18n")
        fun bindView(model: Filter) {
            item.customText1.setValues(item.root.context.getString(R.string.filter_availability_Platno))
            item.customText2.setValues(item.root.context.getString(R.string.filter_availability_close))
            item.customText3.setValues(item.root.context.getString(R.string.filter_availability_free))
            item.customText3.setValues(item.root.context.getString(R.string.filter_availability_open))
        }
    }


    override fun getItemCount(): Int {
        return filterList.size
    }


}
