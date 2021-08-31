package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.data.RidingLessonDayDto
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRidingLessonsService @Inject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
) {

    suspend fun execute(): Result<ApiCall.Error, List<RidingLessonDayDto>> =
        ridingLessonRepository.getRidingLessonDays()
            .map {
                ridingLessonRepository.saveCache(it.ridingLessonDayDtos)
            }
}