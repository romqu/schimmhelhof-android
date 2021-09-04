package de.romqu.schimmelhof_android.data.ridinglessonday

import de.romqu.schimmelhof_android.data.*
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.shared.ApiCallDelegate
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntityQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RidingLessonDayRepository @Inject constructor(
    private val api: RidingLessonApi,
    private val apiCallDelegate: ApiCallDelegate,
    private val lessonDayDao: RidingLessonDayEntityQueries,
) : ApiCall by apiCallDelegate {

    private val lessonsCache = mutableListOf<RidingLessonDayDto>()

    private val lessonCacheChannel = MutableSharedFlow<List<RidingLessonDayDto>>(
        extraBufferCapacity = Int.MAX_VALUE
    )


    suspend fun getRidingLessonDaysNet(): Result<ApiCall.Error, GetRidingLessonDaysOutDto> =
        /*executeBodyCall { api.getRidingLessonDays() }
            .doOn({ Result.Success(it) }, { createFakeData() })*/
        fake()

    fun save(list: List<RidingLessonDayEntity>): List<RidingLessonDayEntity> =
        list.map { entity ->
            lessonDayDao.save(entity)
            val insertedId = lessonDayDao.getLastInsertedId().executeAsOne()
            entity.copy(id = insertedId)
        }


    fun saveCache(list: List<RidingLessonDayDto>): List<RidingLessonDayDto> {
        lessonsCache.clear()
        lessonsCache.addAll(list)
        return list
    }

    fun updateCache(list: List<RidingLessonDayDto>) {
        lessonCacheChannel.tryEmit(list)
    }

    fun delete() {
        lessonDayDao.delete()
    }

    fun getCache(): List<RidingLessonDayDto> = lessonsCache.toList()

    fun observe(): Flow<List<RidingLessonDayDto>> = lessonCacheChannel.asSharedFlow()

    private fun fake(): Result.Success<GetRidingLessonDaysOutDto> {
        return Result.Success(GetRidingLessonDaysOutDto(
            ridingLessonDayDtos = listOf(RidingLessonDayDto(
                date = LocalDateDto(2020, 11, 2),
                ridingLessons =
                (0..30).map {
                    RidingLessonDto(
                        title = getRandomString(),
                        from = LocalTimeDto(it, 40),
                        to = LocalTimeDto(it + 1, 40),
                        date = LocalDateDto(2020, 11, 2),
                        teacher = "TEACHER",
                    )
                }
            ), RidingLessonDayDto(
                date = LocalDateDto(2020, 11, 2),
                ridingLessons =
                (0..30).map {
                    RidingLessonDto(
                        title = getRandomString(),
                        from = LocalTimeDto(it, 40),
                        to = LocalTimeDto(it + 1, 40),
                        date = LocalDateDto(2020, 11, 2),
                        teacher = "TEACHER",
                    )
                }
            ))
        ))
    }

    fun getRandomString(length: Int = 10): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }


    suspend fun book(id: String): Result<ApiCall.Error, BookRidingLessonInDto> {
        return Result.Success(BookRidingLessonInDto("", id))
        // return executeBodyCall { api.bookLesson(id) }
    }

    suspend fun cancel(id: String): Result<ApiCall.Error, CancelRidingLessonInDto> {
        return Result.Success(CancelRidingLessonInDto("", id))
        //return executeBodyCall { api.cancelLesson(id) }
    }
}