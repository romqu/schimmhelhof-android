package de.romqu.schimmelhof_android.presentation.ridinglessonlist.book

import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.scopes.ViewModelScoped
import de.romqu.schimmelhof_android.data.RidingLessonDto
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonItemDiffCallback
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject

@ViewModelScoped
class BookLessonRunner @Inject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
) {

    suspend fun execute(
        bookId: String,
        currentItems: List<RidingLessonParentItem>,
    ): Result<ApiCall.Error, DiffUtil.DiffResult> {
        return ridingLessonRepository.book(bookId)
            .map {
                currentItems.map { parentItem ->
                    val newChildItemList = parentItem.childs.map { childItem ->
                        if (childItem.id == bookId) {
                            childItem.copy(state = RidingLessonDto.RidingLessonState.BOOKED)
                        } else childItem
                    }
                    parentItem.copy(childs = newChildItemList)
                }
            }.map { newItems ->
                DiffUtil.calculateDiff(RidingLessonItemDiffCallback(
                    oldList = currentItems,
                    newList = newItems)
                )
            }


    }
}