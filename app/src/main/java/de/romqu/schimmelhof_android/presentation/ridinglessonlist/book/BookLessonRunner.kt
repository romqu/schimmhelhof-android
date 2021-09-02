package de.romqu.schimmelhof_android.presentation.ridinglessonlist.book

import dagger.hilt.android.scopes.ActivityRetainedScoped
import de.romqu.schimmelhof_android.domain.BookRidingLessonService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.CURRENT_CHILD_ITEMS
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ON_ITEM_CLICK
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.shared.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@ActivityRetainedScoped
class BookLessonRunner @Inject constructor(
    private val bookRidingLessonService: BookRidingLessonService,
    @Named(ON_ITEM_CLICK)
    private val onItemClickChannel: MutableSharedFlow<Int>,
    @Named(CURRENT_CHILD_ITEMS)
    private val currentChildList: Flow<@JvmSuppressWildcards List<RidingLessonChildItem>>,
) {

    val result =
        onItemClickChannel.flatMapMerge { clickedPosition ->
            flowOf(Unit).zip(currentChildList) { _, childList ->
                bookRidingLessonService.execute(childList[clickedPosition].id)
            }
        }

    val showSuccessMessage = result.filterIsInstance<Result.Success<*>>()
        .map {
            "Success"
        }

    val showErrorMessage = result.filterIsInstance<Result.Failure<*>>()
        .map { "Failure" }
}