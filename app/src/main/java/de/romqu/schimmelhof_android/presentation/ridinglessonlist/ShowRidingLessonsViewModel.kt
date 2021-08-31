package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.domain.GetRidingLessonsService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.book.BookLessonRunner
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltViewModel
class ShowRidingLessonsViewModel @Inject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
    private val savedStateHandle: SavedStateHandle,
    private val getRidingLessonsService: GetRidingLessonsService,
    private val bookLessonRunner: BookLessonRunner,
    @Named(ON_ITEM_CLICK) private val onItemClickChannel: MutableSharedFlow<Int>,
) : ViewModel() {

    private val positionState = MutableStateFlow(0)

    val ridingLessonItems = MutableStateFlow(emptyList<RidingLessonParentItem>())

    private val currentChildList = positionState.combine(ridingLessonItems) { position, list ->
        list[position].childs
    }

    private val book = currentChildList.zip(onItemClickChannel) { a, b ->
        a
    }

    val updateDayName =
        combine(ridingLessonItems.filter { it.isNotEmpty() }, positionState)
        { list, position ->
            val parentItem = list[position]
            "${parentItem.weekdayName} - ${parentItem.date.format(DateTimeFormatter.ofPattern("dd-MM"))}"
        }


    init {
        getLessons()
        viewModelScope.launch {
            book.collect {
                it
            }
        }
    }

    private fun getLessons() {
        viewModelScope.launch {
            getRidingLessonsService.execute().doOn({ ridingLessonDayDtos ->
                ridingLessonItems.value = ridingLessonDayDtos.map { dayDto ->
                    val date = LocalDate.of(dayDto.date!!.year, dayDto.date.month, dayDto.date.day)
                    RidingLessonParentItem(
                        date,
                        dayDto.weekday.name,
                        dayDto.ridingLessons.map {
                            RidingLessonChildItem(it.title, it.lessonId, it.state)
                        }
                    )
                }
            }, {})
        }

    }

    fun onNextPage(position: Int) {
        positionState.tryEmit(position)
    }

}