package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemEmptyListBinding
import de.romqu.schimmelhof_android.databinding.ItemParentRidinglessonBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildListViewHolder
import de.romqu.schimmelhof_android.presentation.shared.recyclerview.EmptyItem
import de.romqu.schimmelhof_android.presentation.shared.recyclerview.EmptyListViewHolder

class RidingLessonParentListAdapter(
    private val items: MutableList<RidingLessonParentItem>,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
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
            else -> RidingLessonParentListViewHolder(
                ItemParentRidinglessonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is RidingLessonParentListViewHolder -> holder.bind(items[position], recycledViewPool)
            is EmptyListViewHolder -> holder.bind(EmptyItem("EMPTY"))
        }
    }


    fun updateData(list: List<RidingLessonParentItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}