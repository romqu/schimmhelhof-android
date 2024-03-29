package de.romqu.schimmelhof_android.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import de.romqu.schimmelhof_android.data.security.Crypto
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ApiDataStoreSerializer(private val crypto: Crypto) :
    Serializer<ApiAuthData> {

    override suspend fun readFrom(input: InputStream): ApiAuthData {
        return if (input.available() != 0) {
            try {
                ApiAuthData.ADAPTER.decode(crypto.decrypt(input))
            } catch (exception: IOException) {
                throw CorruptionException("Cannot read proto", exception)
            }
        } else {
            ApiAuthData("")
        }
    }

    override suspend fun writeTo(t: ApiAuthData, output: OutputStream) {
        crypto.encrypt(ApiAuthData.ADAPTER.encode(t), output)
    }

    override val defaultValue: ApiAuthData
        get() = ApiAuthData()
}

