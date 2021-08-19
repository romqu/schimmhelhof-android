package de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent

import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import java.time.LocalDate

data class RidingLessonParentItem(
    val date: LocalDate,
    val weekdayName: String,
    val childs: List<RidingLessonChildItem>,
)