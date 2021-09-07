package de.romqu.schimmelhof_android.presentation.ridinglessonlist.day

import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import java.time.LocalDate

data class RidingLessonDayItem(
    val date: LocalDate,
    val weekdayName: String,
    val lessons: List<RidingLessonItem>,
)