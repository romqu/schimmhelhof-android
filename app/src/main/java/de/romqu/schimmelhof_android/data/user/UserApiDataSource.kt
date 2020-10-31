package de.romqu.schimmelhof_android.data.user

import de.romqu.schimmelhof_android.data.LoginDtoIn
import de.romqu.schimmelhof_android.data.LoginDtoOut
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiDataSource {

    @POST("users/login")
    suspend fun login(@Body loginDtoIn: LoginDtoIn): Response<LoginDtoOut>
}