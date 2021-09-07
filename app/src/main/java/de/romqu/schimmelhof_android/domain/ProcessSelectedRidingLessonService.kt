package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonActionEntity
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonActionEntity.*
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonStateEntity
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessSelectedRidingLessonService @Inject constructor(
    private val lessonRepository: RidingLessonRepository,
) {
    suspend fun execute(
        id: Long,
        remoteId: String,
        action: RidingLessonActionEntity,
    ): Result<ApiCall.Error, Unit> =

        when (action) {
            NONE -> Result.Success(Unit)
            BOOK -> lessonRepository.book(remoteId)
                .map {
                    lessonRepository.update(
                        id,
                        RidingLessonStateEntity.BOOKED,
                        CANCEL_BOOKING
                    )
                }
            ON_WAIT_LIST -> Result.Success(Unit)
            CANCEL_BOOKING -> lessonRepository.cancel(remoteId)
                .map {
                    lessonRepository.update(
                        id,
                        RidingLessonStateEntity.BOOKED,
                        CANCEL_BOOKING
                    )
                }
            CANCEL_WAIT_LIST -> Result.Success(Unit)
        }

}