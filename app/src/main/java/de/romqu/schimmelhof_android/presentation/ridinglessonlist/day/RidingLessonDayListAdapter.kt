package de.romqu.schimmelhof_android.presentation.ridinglessonlist.day

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemEmptyListBinding
import de.romqu.schimmelhof_android.databinding.ItemParentRidinglessonBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import de.romqu.schimmelhof_android.presentation.shared.recyclerview.EmptyItem
import de.romqu.schimmelhof_android.presentation.shared.recyclerview.EmptyListViewHolder
import kotlinx.coroutines.flow.MutableSharedFlow

class RidingLessonDayListAdapter(
    private val items: MutableList<RidingLessonDayItem>,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val onItemClickChannel: MutableSharedFlow<RidingLessonItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // TODO: Might should use delegate pattern at some point
    companion object {
        const val EMPTY_VIEW_TYPE = 0
        const val LESSONS_VIEW_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int =
        if (items.size == 0) EMPTY_VIEW_TYPE else LESSONS_VIEW_TYPE

    override fun getItemCount(): Int = if (items.size == 0) 1 else items.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            EMPTY_VIEW_TYPE -> EmptyListViewHolder(
                ItemEmptyListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> RidingLessonDayListViewHolder(
                ItemParentRidinglessonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClickChannel
            )
        }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is RidingLessonDayListViewHolder -> holder.bind(items[position], recycledViewPool)
            is EmptyListViewHolder -> holder.bind(EmptyItem("EMPTY"))
        }
    }


    fun updateData(list: List<RidingLessonDayItem>) {
        items.clear()
        items.addAll(list)

    }

    fun notifyChange() {
        notifyDataSetChanged()
    }
}

