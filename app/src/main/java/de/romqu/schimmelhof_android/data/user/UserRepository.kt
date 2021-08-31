package de.romqu.schimmelhof_android.data.user

import de.romqu.schimmelhof_android.data.LoginDtoIn
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.shared.ApiCallDelegate
import de.romqu.schimmelhof_android.shared.Result
import okhttp3.Headers
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val apiCallDelegate: ApiCallDelegate,
) : ApiCall by apiCallDelegate {

    suspend fun login(loginDtoIn: LoginDtoIn): Result<ApiCall.Error, Headers> =
        /*executeCall {
            userApiDataSource.login(loginDtoIn)
        }.map { it.headers }*/
        fake()

    private fun fake() =
        Result.Success(Headers.of(mapOf("Authorization" to "Bearer 2e5bb979-2438-4da4-b64c-7947ed8124b8")))

}



