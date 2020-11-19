package de.romqu.schimmelhof_android.presentation.ridinglessonlist.refresh

import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.scopes.ActivityRetainedScoped
import de.romqu.schimmelhof_android.data.RidingLessonDayDto
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ShowRidingLessonsViewModel
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonItemDiffCallback
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import de.romqu.schimmelhof_android.shared.extension.withLatestFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class RefreshRunner @Inject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
    private val viewModel: ShowRidingLessonsViewModel,
) {

    lateinit var scope: CoroutineScope

    private val isActive = MutableStateFlow(false)

    private val ridingLessonDayDtos = MutableSharedFlow<List<RidingLessonDayDto>>()
    private val error = MutableSharedFlow<ApiCall.Error>()

    val updateList = ridingLessonDayDtos.map { dtos ->
        dtos.map { dayDto ->
            RidingLessonParentItem(
                dayDto.weekday.name,
                dayDto.ridingLessons.map { RidingLessonChildItem((it.title)) }
            )
        }
    }

    val dispatchUpdatedListDiff = updateList.withLatestFrom(viewModel.ridingLessonItemsState)
    { newList, oldList ->
        DiffUtil.calculateDiff(RidingLessonItemDiffCallback(oldList = oldList, newList = newList))
    }

    fun setup(scope: CoroutineScope) {
        this.scope = scope
    }

    fun onRefreshPull() {
        scope.launch {
            setActiveTo(true)
            ridingLessonRepository.getRidingLessonDays().doOn({
                ridingLessonDayDtos.emit(it.ridingLessonDayDtos)
            }, {
                setActiveTo(false)
                error.emit(it)
            })
        }
    }

    private fun setActiveTo(value: Boolean) {
        isActive.value = value
    }
}