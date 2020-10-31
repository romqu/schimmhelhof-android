package de.romqu.schimmelhof_android.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import de.romqu.schimmelhof_android.data.security.Crypto
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideApiAuthDataStore(
        @ApplicationContext context: Context,
        crypto: Crypto
    ): DataStore<ApiAuthData> =
        context.createDataStore(
            fileName = "api.pb",
            serializer = ApiDataStoreSerializer(crypto)
        )
}