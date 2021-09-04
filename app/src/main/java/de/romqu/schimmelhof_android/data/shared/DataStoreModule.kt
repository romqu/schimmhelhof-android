package de.romqu.schimmelhof_android.data.shared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.romqu.schimmelhof_android.data.ApiAuthData
import de.romqu.schimmelhof_android.data.ApiDataStoreSerializer
import de.romqu.schimmelhof_android.data.security.Crypto
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideApiAuthDataStore(
        @ApplicationContext context: Context,
        crypto: Crypto,
    ): DataStore<ApiAuthData> =
        DataStoreFactory.create(
            serializer = ApiDataStoreSerializer(crypto)
        ) { File(context.filesDir.absolutePath + "/api.pb") }
}