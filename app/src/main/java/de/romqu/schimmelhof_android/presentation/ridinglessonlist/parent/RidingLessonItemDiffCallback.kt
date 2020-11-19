package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import androidx.recyclerview.widget.DiffUtil

class RidingLessonItemDiffCallback(
    private val oldList: List<RidingLessonParentItem>,
    private val newList: List<RidingLessonParentItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}