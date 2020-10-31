package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemParentRidinglessonBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildListAdapter


class RidingLessonParentListViewHolder(private val binding: ItemParentRidinglessonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(parentItem: RidingLessonParentItem, recycledViewPool: RecyclerView.RecycledViewPool) {

        val linearLayoutManager = LinearLayoutManager(
            binding.ridingLessonDayChildRcv.context,
            LinearLayoutManager.VERTICAL,
            false)

        linearLayoutManager.initialPrefetchItemCount = parentItem.childs.size

        val childAdapter = RidingLessonChildListAdapter(parentItem.childs.toMutableList())

        binding.ridingLessonDayChildRcv.apply {
            layoutManager = linearLayoutManager
            adapter = childAdapter
            setRecycledViewPool(recycledViewPool)
        }
    }
}