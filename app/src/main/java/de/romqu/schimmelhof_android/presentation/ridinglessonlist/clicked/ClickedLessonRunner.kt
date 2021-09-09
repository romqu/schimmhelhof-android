package de.romqu.schimmelhof_android.presentation.ridinglessonlist.clicked

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.domain.ProcessSelectedRidingLessonService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ON_ITEM_CLICK
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import de.romqu.schimmelhof_android.shared.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Named

@ExperimentalCoroutinesApi
class ClickedLessonRunner @AssistedInject constructor(
    private val processSelectedRidingLessonService: ProcessSelectedRidingLessonService,
    @Named(ON_ITEM_CLICK)
    private val onItemClickChannel: MutableSharedFlow<RidingLessonItem>,
    @Assisted
    private val scope: CoroutineScope,
) {

    val result: SharedFlow<Result<ApiCall.Error, Unit>> =
        onItemClickChannel.map { item ->
            processSelectedRidingLessonService.execute(item.id, item.remoteId, item.action)
        }.shareIn(scope, SharingStarted.Lazily)

    val showSuccessMessage: Flow<String> = result.filterIsInstance<Result.Success<*>>()
        .map {
            "Hat geklappt"
        }

    val showErrorMessage: Flow<String> = result.filterIsInstance<Result.Failure<*>>()
        .map { "Nicht geklappt" }
}

@AssistedFactory
interface ClickedLessonRunnerFactory {
    fun create(scope: CoroutineScope): ClickedLessonRunner
}