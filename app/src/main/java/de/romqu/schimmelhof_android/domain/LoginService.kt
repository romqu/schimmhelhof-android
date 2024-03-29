package de.romqu.schimmelhof_android.domain

import androidx.datastore.core.DataStore
import de.romqu.schimmelhof_android.data.ApiAuthData
import de.romqu.schimmelhof_android.data.LoginDtoIn
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.user.UserRepository
import de.romqu.schimmelhof_android.shared.NetworkConst
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.flatMapError
import okhttp3.Headers
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

val AUTH_BEARER_PATTERN: Pattern =
    Pattern.compile("^Bearer *([^ ]+) *$", Pattern.CASE_INSENSITIVE)

class LoginService @Inject constructor(
    private val userRepository: UserRepository,
    private val apiDataStore: DataStore<ApiAuthData>,
) {

    suspend fun execute(username: String, plainPassword: String): Result<Error, Unit> =
        doLogin(username = username, plainPassword = plainPassword)
            .saveAuthToken()


    private suspend fun doLogin(
        username: String,
        plainPassword: String
    ): Result<ApiCall.Error, Headers> =
        userRepository.login(
            LoginDtoIn(username = username, passwordPlain = plainPassword)
        )

    private suspend fun Result<ApiCall.Error, Headers>.saveAuthToken(): Result<Error, Unit> =
        flatMapError({ headers ->

            val authMatcher = AUTH_BEARER_PATTERN.matcher(
                headers.get(NetworkConst.AUTHORIZATION_TOKEN) ?: ""
            )

            val token = if (authMatcher.matches()) {
                val token = authMatcher.group(1)

                try {
                    UUID.fromString(token)
                } catch (ex: Exception) {
                    return Result.Failure(Error.AuthTokenDoesNotExist)
                }
            } else return Result.Failure(Error.AuthTokenDoesNotExist)

            apiDataStore.updateData { apiAuthData: ApiAuthData ->
                apiAuthData.copy(bearerToken = token.toString())
            }

            Result.Success(Unit)

        }, { error: ApiCall.Error ->
            when (error) {
                is ApiCall.Error.NotSuccessful -> Error.IO
                is ApiCall.Error.BodyIsNull -> Error.IO
                is ApiCall.Error.IO -> Error.IO
            }
        })

    sealed class Error {
        object AuthTokenDoesNotExist : Error()
        object IO : Error()
    }
}