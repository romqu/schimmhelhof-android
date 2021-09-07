package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonActionEntity
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonStateEntity
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRidingLessonService @Inject constructor(
    private val lessonRepository: RidingLessonRepository,
) {
    suspend fun execute(id: Long, remoteId: String): Result<ApiCall.Error, Unit> =
        lessonRepository.book(remoteId)
            .map {
                lessonRepository.update(
                    id,
                    RidingLessonStateEntity.BOOKED,
                    RidingLessonActionEntity.CANCEL_BOOKING)
            }
}