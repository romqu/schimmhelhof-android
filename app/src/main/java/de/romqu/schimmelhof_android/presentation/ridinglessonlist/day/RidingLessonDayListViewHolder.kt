package de.romqu.schimmelhof_android.presentation.ridinglessonlist.day

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.romqu.schimmelhof_android.databinding.ItemParentRidinglessonBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonListAdapter
import kotlinx.coroutines.flow.MutableSharedFlow


class RidingLessonDayListViewHolder(
    private val binding: ItemParentRidinglessonBinding,
    private val onItemClickChannel: MutableSharedFlow<RidingLessonItem>,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dayItem: RidingLessonDayItem, recycledViewPool: RecyclerView.RecycledViewPool) {

        val linearLayoutManager = LinearLayoutManager(
            binding.ridingLessonDayChildRcv.context,
            LinearLayoutManager.VERTICAL,
            false)

        linearLayoutManager.initialPrefetchItemCount = dayItem.lessons.size

        val childAdapter =
            RidingLessonListAdapter(dayItem.lessons.toMutableList(), onItemClickChannel)

        binding.ridingLessonDayChildRcv.apply {
            layoutManager = linearLayoutManager
            adapter = childAdapter
            setRecycledViewPool(recycledViewPool)
        }
    }
}