package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem

data class RidingLessonParentItem(
    val childs: List<RidingLessonChildItem>
)