package de.romqu.schimmelhof_android.domain

import androidx.datastore.core.DataStore
import de.romqu.schimmelhof_android.data.ApiAuthData
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.data.user.UserRepository
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutService @Inject constructor(
    private val userRepository: UserRepository,
    private val ridingLessonDayRepository: RidingLessonDayRepository,
    private val authDataStore: DataStore<ApiAuthData>,
) {

    suspend fun execute(): Result<ApiCall.Error, Unit> =
        userRepository.logout()
            .map { ridingLessonDayRepository.delete() }
            .map {
                authDataStore.updateData {
                    it.copy(bearerToken = "")
                }

            }.map { }
}
