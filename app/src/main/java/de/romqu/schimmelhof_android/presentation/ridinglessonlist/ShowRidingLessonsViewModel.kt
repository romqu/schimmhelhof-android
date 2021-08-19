package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ShowRidingLessonsViewModel @Inject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val positionState = MutableStateFlow(0)

    val updateRidingLessonItems = MutableStateFlow(emptyList<RidingLessonParentItem>())
    val updateDayName =
        combine(updateRidingLessonItems.filter { it.isNotEmpty() }, positionState)
        { list, position ->
            val parentItem = list[position]
            "${parentItem.weekdayName} - ${parentItem.date.format(DateTimeFormatter.ofPattern("dd-MM"))}"
        }


    init {
        viewModelScope.launch {
            ridingLessonRepository.getRidingLessonDays().doOn({ dto ->
                updateRidingLessonItems.value = dto.ridingLessonDayDtos.map { dayDto ->
                    val date = LocalDate.of(dayDto.date!!.year, dayDto.date.month, dayDto.date.day)
                    RidingLessonParentItem(
                        date,
                        dayDto.weekday.name,
                        dayDto.ridingLessons.map { RidingLessonChildItem((it.title)) }
                    )
                }
            }, {})
        }
    }

    fun onNextPage(position: Int) {
        positionState.tryEmit(position)
    }

}