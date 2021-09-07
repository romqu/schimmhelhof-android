package de.romqu.schimmelhof_android.presentation.ridinglessonlist.day

import androidx.recyclerview.widget.DiffUtil

class RidingLessonDayItemDiffCallback(
    private val oldList: List<RidingLessonDayItem>,
    private val newList: List<RidingLessonDayItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].lessons.toTypedArray() contentDeepEquals
                newList[newItemPosition].lessons.toTypedArray()
                ) && oldList[oldItemPosition] == newList[newItemPosition]
    }
}