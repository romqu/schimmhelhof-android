package de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson

import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonActionEntity
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonStateEntity

data class RidingLessonItem(
    val title: String,
    val time: String,
    val teacher: String,
    val state: RidingLessonStateEntity,
    val id: Long,
    val remoteId: String,
    val action: RidingLessonActionEntity,
    val isEnabled: Boolean,
)