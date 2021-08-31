package de.romqu.schimmelhof_android.presentation.ridinglessonlist.child

import de.romqu.schimmelhof_android.data.RidingLessonDto

data class RidingLessonChildItem(
    val text: String,
    val id: String,
    val state: RidingLessonDto.RidingLessonState,
)