package de.romqu.schimmelhof_android.data.user

import de.romqu.schimmelhof_android.data.LoginDtoIn
import de.romqu.schimmelhof_android.data.LoginDtoOut
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import okhttp3.Headers
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiDataSource: UserApiDataSource,
    private val apiCallDelegate: ApiCallDelegate
) : ApiCall by apiCallDelegate {

    suspend fun login(loginDtoIn: LoginDtoIn): Result<ApiCall.Error, Headers> =
        executeCall { userApiDataSource.login(loginDtoIn) }.map { it.headers }
}



