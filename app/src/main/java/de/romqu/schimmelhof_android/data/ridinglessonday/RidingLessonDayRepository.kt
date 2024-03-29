package de.romqu.schimmelhof_android.data.ridinglessonday

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import de.romqu.schimmelhof_android.data.*
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.shared.ApiCallDelegate
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhofandroid.sql.Get
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntityQueries
import de.romqu.schimmelhofandroid.sql.RidingLessonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RidingLessonDayRepository @Inject constructor(
    private val api: RidingLessonApi,
    private val apiCallDelegate: ApiCallDelegate,
    private val lessonDayDao: RidingLessonDayEntityQueries,
) : ApiCall by apiCallDelegate {

    suspend fun getRidingLessonDaysNet(): Result<ApiCall.Error, GetRidingLessonDaysOutDto> =
        executeBodyCall { api.get() }
    // fake()

    fun get(): Flow<List<DayWithLessonsEntity>> = lessonDayDao.get()
        .asFlow()
        .mapToList()
        .map { rows ->
            rows.groupBy(Get::id)
                .map { entry ->

                    val firstRow = entry.value.first()
                    val lessonDay = RidingLessonDayEntity(
                        firstRow.id,
                        WeekdayEntity.valueOf(firstRow.weekday.name),
                        firstRow.date
                    )

                    val lessons = entry.value.map { row ->
                        with(row) {
                            RidingLessonEntity(
                                id_,
                                remoteId,
                                weekday_,
                                title,
                                fromTime,
                                toTime,
                                date_,
                                teacher,
                                place,
                                state,
                                action,
                                ridingLessonDayId
                            )
                        }
                    }

                    DayWithLessonsEntity(
                        lessonDay,
                        lessons
                    )
                }
        }

    fun save(list: List<RidingLessonDayEntity>): List<RidingLessonDayEntity> =
        list.map(::save)

    fun save(entity: RidingLessonDayEntity): RidingLessonDayEntity {
        lessonDayDao.save(entity)
        val insertedId = lessonDayDao.getLastInsertedId().executeAsOne()
        return entity.copy(id = insertedId)
    }

    fun delete() {
        lessonDayDao.delete()
    }

    private fun fake(): Result.Success<GetRidingLessonDaysOutDto> {
        return Result.Success(
            GetRidingLessonDaysOutDto(
                ridingLessonDayDtos = (0..1).map {
                    RidingLessonDayDto(
                        date = LocalDateDto(2020, 11, 2),
                        ridingLessons =
                        ('A'..'Z').map {
                            RidingLessonDto(
                                title = if (it == 'A') getRandomString(70) else it.toString(),
                                from = LocalTimeDto(13, 40),
                                to = LocalTimeDto(14, 40),
                                date = LocalDateDto(2020, 11, 2),
                                teacher = "TEACHER",

                                )
                        }
                    )
                }
            )
        )
    }

    private fun getRandomString(length: Int = 10): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

class DayWithLessonsEntity(
    val day: RidingLessonDayEntity,
    val lessons: List<RidingLessonEntity>,
)