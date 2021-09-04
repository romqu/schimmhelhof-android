package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.Database
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadInitialLessonDaysService @Inject constructor(
    private val lessonRepository: RidingLessonRepository,
    private val lessonDayRepository: RidingLessonDayRepository,
    private val database: Database,
) {

    suspend fun execute() {
        lessonDayRepository.getRidingLessonDaysNet()
            .map { getDayDto ->
                getDayDto.ridingLessonDayDtos.map { dayDto ->

                }
            }
    }
}