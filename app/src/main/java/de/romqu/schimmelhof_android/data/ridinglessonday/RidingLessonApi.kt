package de.romqu.schimmelhof_android.data.ridinglessonday

import de.romqu.schimmelhof_android.data.BookRidingLessonInDto
import de.romqu.schimmelhof_android.data.CancelRidingLessonInDto
import de.romqu.schimmelhof_android.data.GetRidingLessonDaysOutDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RidingLessonApi {

    @GET("/api/v1/ridinglessonsdays")
    suspend fun getRidingLessonDays(): Response<GetRidingLessonDaysOutDto>

    @POST("/api/v1/book/lesson/{id}")
    suspend fun bookLesson(@Path("id") id: String): Response<BookRidingLessonInDto>

    @DELETE("/api/v1/book/lesson/{id}")
    suspend fun cancelLesson(@Path("id") id: String): Response<CancelRidingLessonInDto>
}