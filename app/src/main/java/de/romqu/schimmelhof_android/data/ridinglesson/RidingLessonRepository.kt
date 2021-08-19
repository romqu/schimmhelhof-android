package de.romqu.schimmelhof_android.data.ridinglesson

import de.romqu.schimmelhof_android.data.*
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.shared.ApiCallDelegate
import de.romqu.schimmelhof_android.shared.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RidingLessonRepository @Inject constructor(
    private val apiSource: RidingLessonApiDataSource,
    private val apiCallDelegate: ApiCallDelegate,
) : ApiCall by apiCallDelegate {

    suspend fun getRidingLessonDays(): Result<ApiCall.Error, GetRidingLessonDaysOutDto> =
        executeBodyCall { apiSource.getRidingLessonDays() }
            .doOn({ Result.Success(it) }, { createFakeData() })

    private fun createFakeData(): Result.Success<GetRidingLessonDaysOutDto> {
        return Result.Success(GetRidingLessonDaysOutDto(
            ridingLessonDayDtos = listOf(RidingLessonDayDto(
                date = LocalDateDto(2020, 11, 2),
                ridingLessons =
                (0..9).map {
                    RidingLessonDto(
                        title = "TITLE",
                        from = LocalTimeDto(it, 40),
                        to = LocalTimeDto(it + 1, 40),
                        date = LocalDateDto(2020, 11, 2),
                        teacher = "TEACHER",
                    )
                }
            ))
        ))
    }
}