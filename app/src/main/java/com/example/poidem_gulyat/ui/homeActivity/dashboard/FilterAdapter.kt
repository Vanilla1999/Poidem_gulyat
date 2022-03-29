package com.example.poidem_gulyat.ui.homeActivity.dashboard


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.protobuf.Empty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.databinding.ItemRecyclerViewBinding
import com.example.poidem_gulyat.databinding.ItemRecyclerViewForEmptyBinding
import com.example.poidem_gulyat.utils.*

import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*

class FilterAdapter(
    private val onMarkerCallback: (MarkerPoint) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    companion object {
        private const val NotEmpty = 1
        private const val Empty = 2
    }
    var markerPointList =  emptyList<MarkerPoint>()
    var filteredChecklists = emptyList<MarkerPoint>()
    private lateinit var diff: GenericItemDiff<MarkerPoint>
    private var textFilter = ""
    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val text = constraint.toStringOrEmpty()
            textFilter = text
            val list = if (text.isNotEmpty()) {
                markerPointList.filter { it.name.startsWith(text, true) }
            } else {
                markerPointList
            }
            return FilterResults().apply { values = list }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let { it ->
                val objects = it.values as? List<*> ?: return
                val list = mutableListOf<MarkerPoint>()
                for (item in objects) {
                    if (item is MarkerPoint) list.add(item)
                }
                if(list.isEmpty()){
                    list.add(MarkerPoint(id = -1,name = "Войсковой собор святого Благоверного Князя Александра Невского",
                        latitude = 45.01436,
                        longitude = 38.96696,
                        img = null,
                        description = "Войсковой собор святого Благоверного Князя Александра Невского",
                        startWork = 8 * 60 * 60 * 1000L,
                        endWork = 18 * 60 * 60 * 1000L,
                        rating = 5.0f,
                        type = attraction,price =0))
                }
                val diffCallbackList = GenericDiffUtil(filteredChecklists,list,diff)
                val diffResult = DiffUtil.calculateDiff(diffCallbackList, true)
                try {
                    filteredChecklists = list
                    diffResult.dispatchUpdatesTo(this@FilterAdapter)
                } catch (e: Exception) {
                    Log.d("TAG", e.message.toString())
                }
            }
        }
    }
    private val itemClick: (Int) -> Unit =
        { position: Int -> onMarkerCallback(filteredChecklists[position]) }

    //  @LayoutRes private val layoutRes: Int,
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NotEmpty -> {
                val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)
                ViewHolder(
                    binding, itemClick
                )
            }
            Empty -> {
                val binding = ItemRecyclerViewForEmptyBinding.inflate(inflater, parent, false)
                ViewHolderNull(
                    binding
                )
            }
            else -> throw IllegalStateException("Incorrect view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (filteredChecklists[0].id != -1)
            (holder as ViewHolder).bindView(filteredChecklists[position])else  (holder as ViewHolderNull)
    }

    // чтоб использовать разные ViewHolder.
    override fun getItemViewType(position: Int): Int {
        return if (filteredChecklists[0].id == -1)
            Empty else{
            NotEmpty
        }
    }

    fun setDiff(diff: GenericItemDiff<MarkerPoint>){
        this.diff = diff
    }

    fun update(items: List<MarkerPoint>) {
        Log.d("TAG", "Pfxtybt sdfsdfsdf ")

        val diffCallback = GenericDiffUtil(filteredChecklists, items, diff)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        try {
            filteredChecklists= items
            markerPointList = items
            diffResult.dispatchUpdatesTo(this)

        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    inner class ViewHolder(
        private val item: ItemRecyclerViewBinding,
        private val itemClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                itemClick(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(model: MarkerPoint) {
            item.textAdress.text = model.adress
            endWork(model, item)
            item.textTitle.text = model.name
            item.rating.rating = model.rating?:0.0f
        }
    }

    inner class ViewHolderNull(
        private val item: ItemRecyclerViewForEmptyBinding,
    ) : RecyclerView.ViewHolder(item.root) {
    }

    private fun endWork(model: MarkerPoint, item: ItemRecyclerViewBinding) {
        model.endWork?.let {
            val currentDate = Date(System.currentTimeMillis())
            val timeToClose =
                atStartOfDay(currentDate).time + model.endWork
            item.textTimeWork.text =
                if (timeToClose > currentDate.time) item.root.context.getString(R.string.works_pattern,
                    model.endWork / (60 * 60 * 1000L))
                else item.root.context.getString(R.string.close)
        } ?: run {
            item.textTimeWork.text =
                item.root.context.getString(R.string.time_not_specified)
        }
    }

    override fun getItemCount(): Int {
        return filteredChecklists.size
    }

    override fun getFilter(): Filter {
        return filter
    }
}
