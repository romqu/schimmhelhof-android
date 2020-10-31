package de.romqu.schimmelhof_android.data.shared

import de.romqu.schimmelhof_android.shared.Result
import okhttp3.Headers
import retrofit2.Response

interface ApiCall {
    suspend fun <T> executeCall(call: suspend () -> retrofit2.Response<T>): Result<Error, Response<T>>
    suspend fun <T> executeBodyCall(call: suspend () -> retrofit2.Response<T>): Result<Error, T>

    class Response<T>(
        val data: T,
        val headers: Headers
    )

    sealed class Error(val message: String) {
        class NotSuccessful(message: String, statusCode: Int) : Error(message)
        class BodyIsNull() : Error("")
        class IO(message: String) : Error(message)
    }
}