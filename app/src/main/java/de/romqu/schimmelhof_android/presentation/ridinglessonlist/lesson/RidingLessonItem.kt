package de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson

import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonStateEntity

data class RidingLessonItem(
    val title: String,
    val state: RidingLessonStateEntity,
    val id: Long,
    val remoteId: String,
)