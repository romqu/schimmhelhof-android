package de.romqu.schimmelhof_android.data.ridinglesson

import de.romqu.schimmelhof_android.data.GetRidingLessonDaysOutDto
import de.romqu.schimmelhof_android.data.LoginDtoIn
import de.romqu.schimmelhof_android.data.LoginDtoOut
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RidingLessonApiDataSource {

    @GET("/api/v1/ridinglessonsdays")
    suspend fun getRidingLessonDays(): Response<GetRidingLessonDaysOutDto>
}