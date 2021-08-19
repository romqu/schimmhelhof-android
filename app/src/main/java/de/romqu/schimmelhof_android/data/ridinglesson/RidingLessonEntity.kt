package de.romqu.schimmelhof_android.data.ridinglesson

import java.time.LocalDate
import java.time.LocalTime

class RidingLessonEntity(
    val weekday: WeekdayEntity,
    val title: String,
    val from: LocalTime,
    val to: LocalTime,
    val date: LocalDate,
    val teacher: String,
    val place: String,
    val lessonCmd: String,
    val lessonId: String,
    val state: RidingLessonStateEntity,
    val action: RidingLessonActionEntity,
)