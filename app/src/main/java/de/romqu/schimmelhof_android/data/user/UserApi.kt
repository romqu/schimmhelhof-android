package de.romqu.schimmelhof_android.data.user

import de.romqu.schimmelhof_android.data.LoginDtoIn
import de.romqu.schimmelhof_android.data.LoginDtoOut
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserApi {

    @POST("users/login")
    suspend fun login(@Body loginDtoIn: LoginDtoIn): Response<LoginDtoOut>

    @DELETE("/api/v1/sessions/")
    suspend fun logout(): Response<Unit>


}