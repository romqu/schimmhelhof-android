package de.romqu.schimmelhof_android.data.ridinglesson

import de.romqu.schimmelhof_android.data.GetRidingLessonDaysOutDto
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.user.ApiCallDelegate
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

}