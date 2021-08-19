package de.romqu.schimmelhof_android.data.ridinglesson

import de.romqu.schimmelhof_android.data.GetRidingLessonDaysOutDto
import retrofit2.Response
import retrofit2.http.GET

interface RidingLessonApiDataSource {

    @GET("/api/v1/ridinglessonsdays")
    suspend fun getRidingLessonDays(): Response<GetRidingLessonDaysOutDto>
}