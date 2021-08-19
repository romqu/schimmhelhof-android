package de.romqu.schimmelhof_android.data.shared

import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import okio.IOException
import retrofit2.Response
import java.lang.reflect.Field
import javax.inject.Inject

class ApiCallDelegate @Inject constructor() : ApiCall {

    override suspend fun <T> executeBodyCall(call: suspend () -> Response<T>): Result<ApiCall.Error, T> =
        executeCall(call).map { it.data }


    override suspend fun <T> executeCall(call: suspend () -> Response<T>): Result<ApiCall.Error, ApiCall.Response<T>> =
        try {
            val response = call()
            val body = response.body()

            if (response.isSuccessful) {
                if (body != null) {
                    val field = getErrorMessageField<T>(body)
                    makeFieldAccessible(field)
                    val errorMessage = getErrorMessageFieldValue<T>(field, body)

                    if (errorMessage.isEmpty()) {
                        Result.Success(ApiCall.Response(body, response.headers()))
                    } else {
                        Result.Failure(ApiCall.Error.NotSuccessful(errorMessage, response.code()))
                    }
                } else {
                    Result.Failure(ApiCall.Error.BodyIsNull())
                }
            } else {
                Result.Failure(
                    ApiCall.Error.NotSuccessful(
                        response.errorBody().toString(),
                        response.code()
                    )
                )
            }

        } catch (ex: IOException) {
            Result.Failure(ApiCall.Error.IO(ex.toString()))
        }

    private fun makeFieldAccessible(field: Field) {
        field.isAccessible = true
    }

    private fun <T> getErrorMessageFieldValue(field: Field, body: T?): String =
        field.get(body) as String

    private fun <T> getErrorMessageField(body: T?): Field =
        body!!::class.java.getDeclaredField("errorMessage")


}