package de.romqu.schimmelhof_android.presentation.ridinglessonlist.book

import android.util.Log
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.domain.BookRidingLessonService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ON_ITEM_CLICK
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import de.romqu.schimmelhof_android.shared.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Named

@ExperimentalCoroutinesApi
class BookLessonRunner @AssistedInject constructor(
    private val bookRidingLessonService: BookRidingLessonService,
    @Named(ON_ITEM_CLICK)
    private val onItemClickChannel: MutableSharedFlow<RidingLessonItem>,
    @Assisted
    private val scope: CoroutineScope,
) {

    val result: SharedFlow<Result<ApiCall.Error, Unit>> =
        onItemClickChannel.map { item ->
            Log.d("AAAAA", """$item $onItemClickChannel""")
            bookRidingLessonService.execute(item.id, item.remoteId)
        }.shareIn(scope, SharingStarted.Lazily)

    val showSuccessMessage: Flow<String> = result.filterIsInstance<Result.Success<*>>()
        .map {
            "Success"
        }

    val showErrorMessage: Flow<String> = result.filterIsInstance<Result.Failure<*>>()
        .map { "Failure" }
}

@AssistedFactory
interface BookLessonRunnerFactory {
    fun create(scope: CoroutineScope): BookLessonRunner
}