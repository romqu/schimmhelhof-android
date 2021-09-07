package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.Database
import de.romqu.schimmelhof_android.data.GetRidingLessonDaysOutDto
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonActionEntity
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonStateEntity
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.data.ridinglessonday.WeekdayEntity
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonEntity
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadInitialLessonDaysService @Inject constructor(
    private val lessonRepository: RidingLessonRepository,
    private val lessonDayRepository: RidingLessonDayRepository,
    private val database: Database,
) {

    suspend fun execute(): Result<ApiCall.Error, Unit> =
        lessonDayRepository.getRidingLessonDaysNet()
            .processLessonDays()


    private fun Result<ApiCall.Error, GetRidingLessonDaysOutDto>.processLessonDays()
            : Result<ApiCall.Error, Unit> = map { getDayDto ->
        database.transaction {
            lessonDayRepository.delete()
            getDayDto.ridingLessonDayDtos.map { dayDto ->
                val date = LocalDate.of(
                    dayDto.date!!.year,
                    dayDto.date.month,
                    dayDto.date.day
                )
                val dayEntity = RidingLessonDayEntity(
                    id = 0,
                    weekday = WeekdayEntity.valueOf(dayDto.weekday.name),
                    date = date,
                )

                val savedDayEntity = lessonDayRepository.save(dayEntity)

                val lessons = dayDto.ridingLessons.map { lessonDto ->

                    val from = lessonDto.from!!
                    val to = lessonDto.to!!

                    RidingLessonEntity(
                        id = 0,
                        remoteId = lessonDto.lessonId,
                        weekday = WeekdayEntity.valueOf(lessonDto.weekday.name),
                        title = lessonDto.title,
                        fromTime = LocalTime.of(from.hours, from.minutes),
                        toTime = LocalTime.of(to.hours, to.minutes),
                        date = date,
                        teacher = lessonDto.teacher,
                        place = lessonDto.place,
                        state = RidingLessonStateEntity.valueOf(lessonDto.state.name),
                        action = RidingLessonActionEntity.valueOf(lessonDto.action.name),
                        ridingLessonDayId = savedDayEntity.id
                    )
                }

                lessonRepository.save(lessons)
            }
        }
    }

}