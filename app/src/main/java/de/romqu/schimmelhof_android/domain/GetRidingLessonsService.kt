package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.data.RidingLessonDayDto
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRidingLessonsService @Inject constructor(
    private val ridingLessonDayRepository: RidingLessonDayRepository,
) {

    suspend fun execute(): Result<ApiCall.Error, List<RidingLessonDayDto>> =
        ridingLessonDayRepository.getRidingLessonDaysNet()
            .map {
                ridingLessonDayRepository.saveCache(it.ridingLessonDayDtos)
            }
}