package de.romqu.schimmelhof_android.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonApiDataSource
import de.romqu.schimmelhof_android.data.user.UserApiDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.wire.WireConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenHeaderInterceptor: TokenHeaderInterceptor,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(tokenHeaderInterceptor)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(WireConverterFactory.create())
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiDataSource =
        retrofit.create(UserApiDataSource::class.java)

    @Provides
    @Singleton
    fun provideRidingLessonApiService(retrofit: Retrofit): RidingLessonApiDataSource =
        retrofit.create(RidingLessonApiDataSource::class.java)

    @Provides
    @Singleton
    fun provideNetworkConnectivityEmitter(): MutableSharedFlow<NetworkConnectivityState> =
        MutableSharedFlow(replay = 1)

    enum class NetworkConnectivityState {
        CONNECTED,
        DISCONNECTED,
    }

}