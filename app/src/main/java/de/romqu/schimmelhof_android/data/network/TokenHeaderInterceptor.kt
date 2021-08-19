package de.romqu.schimmelhof_android.data.network

import androidx.datastore.core.DataStore
import de.romqu.schimmelhof_android.data.ApiAuthData
import de.romqu.schimmelhof_android.shared.NetworkConst
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenHeaderInterceptor @Inject constructor(
    private val apiDataStore: DataStore<ApiAuthData>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = runBlocking { apiDataStore.data.firstOrNull()?.bearerToken }

        val updatedRequest = if (token != null && token.isNotBlank()) {
            request.newBuilder()
                .addHeader(NetworkConst.AUTHORIZATION_TOKEN, "Bearer $token")
                .build()
        } else request


        return chain.proceed(updatedRequest)
    }
}