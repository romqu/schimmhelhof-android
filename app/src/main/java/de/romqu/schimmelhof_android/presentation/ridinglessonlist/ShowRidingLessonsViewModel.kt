package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ShowRidingLessonsViewModel @ViewModelInject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val ridingLessonItems = MutableStateFlow(emptyList<RidingLessonParentItem>())
    private val pagePosition = MutableStateFlow(0)

    private val error = MutableSharedFlow<Error>(1).apply {
        tryEmit(Error.DEFAULT)
    }

    val showErrorMessage = error.drop(1).map { it.toString() }

    val showEmptyListMessage = ridingLessonItems.drop(1).filter { it.isEmpty() }

    val hideInitialLoading = combine(ridingLessonItems, error) { items, error ->
        (items.isNotEmpty() && error == Error.NONE) || error != Error.NONE
    }.drop(1).filter { it }.debounce(1500)

    val showList = hideInitialLoading

    val showDayName = hideInitialLoading

    val ridingLessonDayName = ridingLessonItems.drop(1).combine(pagePosition) { items, position ->
        items[position].dayOfWeekName
    }


    init {
        getRidingLessonsDays()
    }

    private fun getRidingLessonsDays() {
        viewModelScope.launch {
            ridingLessonRepository.getRidingLessonDays().doOn({ dto ->

                ridingLessonItems.value = dto.ridingLessonDayDtos.map { dayDto ->
                    RidingLessonParentItem(
                        dayDto.weekday.name,
                        dayDto.ridingLessons.map { RidingLessonChildItem((it.title)) }
                    )
                }
            }, { apiError ->
                error.emit(Error.DEFAULT)
            })
        }
    }

    fun onNextPage(position: Int) {
        pagePosition.value = position
    }

    sealed class Error {
        object NONE : Error()
        object DEFAULT : Error()
    }

}