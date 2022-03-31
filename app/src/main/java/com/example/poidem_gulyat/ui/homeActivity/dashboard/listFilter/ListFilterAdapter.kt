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


    private var list = listOf(0,1,2,3)
    private var filter: Filter = Filter()

    fun setFilter(filter: Filter) {
        this.filter = filter
    }

    fun getFilter(): Filter = filter

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
                (holder as ViewHolderSortBy).bindView()
            }
            Type -> {
                (holder as ViewHolderType).bindView()
            }
            Rating -> {
                (holder as ViewHolderRating).bindView()
            }
            Availability -> {
                (holder as ViewHolderAvailability).bindView()
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


    fun update(items: List<Int>) {
        list = items
    }

    fun clear(){
        filter = Filter()
        notifyDataSetChanged()
    }



    inner class ViewHolderSortBy(
        private val item: ItemRecyclerViewFiltersSortbyBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        @SuppressLint("SetTextI18n")
        fun bindView() {
            item.customText1.setValues(item.root.context.getString(R.string.filter_sortBy_nearBy))
            item.customText2.setValues(item.root.context.getString(R.string.filter_sortBy_rating))
            item.customText1.isActive(0)
            item.customText2.isActive(0)
            when (filter.typeSort) {
                bestNearBy -> {
                    item.customText1.isActive(1)
                }
                sortByRating -> {
                    item.customText2.isActive(1)
                }
            }
            item.customText1.setlistener {
                item.customText2.isActive(0)
                if (filter.typeSort == bestNearBy)
                    filter.typeSort = empty else filter.typeSort = bestNearBy
                itemClick(filter)
            }
            item.customText2.setlistener {
                item.customText1.isActive(0)
                if (filter.typeSort == sortByRating)
                    filter.typeSort = empty else filter.typeSort = sortByRating
                itemClick(filter)
            }
        }
    }

    inner class ViewHolderType(
        private val item: ItemRecyclerViewFiltersTypesBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        @SuppressLint("SetTextI18n")
        fun bindView() {
            item.customText1.setValues(item.root.context.getString(R.string.filter_type_attraction))
            item.customText2.setValues(item.root.context.getString(R.string.filter_type_userPoint))
            item.customText3.setValues(item.root.context.getString(R.string.filter_type_photoZone))
            item.customText1.isActive(0)
            item.customText2.isActive(0)
            item.customText3.isActive(0)
            when (filter.typeMarker) {
                attraction -> {
                    item.customText1.isActive(1)
                }
                userPoint -> {
                    item.customText2.isActive(1)
                }
                photoZone -> {
                    item.customText3.isActive(1)
                }
            }

            item.customText1.setlistener {
                item.customText2.isActive(0)
                item.customText3.isActive(0)
                if (filter.typeMarker == attraction)
                    filter.typeMarker = empty else   filter.typeMarker = attraction
                itemClick(filter)
            }
            item.customText2.setlistener {
                item.customText1.isActive(0)
                item.customText3.isActive(0)
                if (filter.typeMarker == userPoint)
                    filter.typeMarker = empty else   filter.typeMarker = userPoint
                itemClick(filter)
            }
            item.customText3.setlistener {
                item.customText2.isActive(0)
                item.customText1.isActive(0)
                if (filter.typeMarker == photoZone)
                    filter.typeMarker = empty else   filter.typeMarker = photoZone
                itemClick(filter)
            }

        }
    }

    inner class ViewHolderRating(
        private val item: ItemRecyclerViewFiltersRatingBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {


        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bindView() {
            when (filter.rating) {
                1 -> {
                    item.rating2.background = item.root.context.getDrawable(R.color.gray)
                }
                2 -> {
                    item.rating5.background = item.root.context.getDrawable(R.color.gray)
                }
                3 -> {
                    item.rating4.background = item.root.context.getDrawable(R.color.gray)
                }
                4 -> {
                    item.rating3.background = item.root.context.getDrawable(R.color.gray)
                }
                5 -> {
                    item.rating1.background = item.root.context.getDrawable(R.color.gray)
                }
            }
            // не работает onClick
//            item.rating1.setOnClickListener {
//                if (model.rating == 5)
//                    model.rating = 0 else model.rating = 5
//                itemClick(model)
//            }
//            item.rating3.setOnClickListener {
//                if (model.rating == 4)
//                    model.rating = 0 else model.rating = 4
//                itemClick(model)
//            }
//            item.rating4.setOnClickListener {
//                if (model.rating == 3)
//                    model.rating = 0 else model.rating = 3
//                itemClick(model)
//            }
//            item.rating5.setOnClickListener {
//                if (model.rating == 2)
//                    model.rating = 0 else model.rating = 2
//                itemClick(model)
//            }
//            item.rating2.setOnClickListener {
//                if (model.rating == 1)
//                    model.rating = 0 else model.rating = 1
//                itemClick(model)
//            }
        }

    }

    inner class ViewHolderAvailability(
        private val item: ItemRecyclerViewFiltersAvailabilityBinding,
        private val itemClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        @SuppressLint("SetTextI18n")
        fun bindView() {
            item.customText1.setValues(item.root.context.getString(R.string.filter_availability_Platno))
            item.customText2.setValues(item.root.context.getString(R.string.filter_availability_close))
            item.customText3.setValues(item.root.context.getString(R.string.filter_availability_free))
            item.customText3.setValues(item.root.context.getString(R.string.filter_availability_open))
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


}
