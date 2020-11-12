package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem

data class RidingLessonParentItem(
    val dayOfWeekName: String,
    val childs: List<RidingLessonChildItem>
)