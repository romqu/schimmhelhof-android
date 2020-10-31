package de.romqu.schimmelhof_android

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class CoScopeModule {

    @Provides
    @Singleton
    fun provideAppCoScope(retrofit: Retrofit): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}