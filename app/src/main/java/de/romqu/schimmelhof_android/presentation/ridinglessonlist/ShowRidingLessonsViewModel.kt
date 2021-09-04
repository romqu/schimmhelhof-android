package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.data.RidingLessonDayDto
import de.romqu.schimmelhof_android.domain.GetRidingLessonsService
import de.romqu.schimmelhof_android.domain.LoadInitialLessonDaysService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonItemDiffCallback
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
    private val savedStateHandle: SavedStateHandle,
    private val getRidingLessonsService: GetRidingLessonsService,
    private val initialService: LoadInitialLessonDaysService,
    @Named(CURRENT_POSITION) private val currentPosition: MutableStateFlow<Int>,
    @Named(CURRENT_PARENT_ITEMS) val ridingLessonParentItems: MutableStateFlow<List<RidingLessonParentItem>>,
    @Named(OBSERVE_ITEMS) val observeItems: Flow<@JvmSuppressWildcards List<RidingLessonParentItem>>,
) : ViewModel() {

    private val observeItemsShared = observeItems.shareIn(
        viewModelScope, SharingStarted.Lazily
    )

    val dispatchListUpdates = observeItemsShared
        .scan(DispatchListUpdate()) { previous, next ->
            val diffResult =
                DiffUtil.calculateDiff(RidingLessonItemDiffCallback(previous.list, next))
            DispatchListUpdate(next, diffResult)
        }.filter { it.list.isNotEmpty() }

    class DispatchListUpdate(
        val list: List<RidingLessonParentItem> = emptyList(),
        val diffResult: DiffUtil.DiffResult? = null,
    )


    val updateDayName: Flow<String> =
        combine(observeItems.filter { it.isNotEmpty() }, currentPosition)
        { list, position ->
            val parentItem = list[position]
            "${parentItem.weekdayName} - ${parentItem.date.format(DateTimeFormatter.ofPattern("dd-MM"))}"
        }


    init {
        viewModelScope.launch {
            initialService.execute()
        }
        getLessons()
    }

    private fun getLessons() {
        viewModelScope.launch {
            getRidingLessonsService.execute().doOn({ ridingLessonDayDtos ->
                ridingLessonParentItems.value = toItems(ridingLessonDayDtos)
            }, {})
        }

    }

    private fun toItems(ridingLessonDayDtos: List<RidingLessonDayDto>) =
        ridingLessonDayDtos.map { dayDto ->
            val date = LocalDate.of(dayDto.date!!.year, dayDto.date.month, dayDto.date.day)
            RidingLessonParentItem(
                date,
                dayDto.weekday.name,
                dayDto.ridingLessons.map {
                    RidingLessonChildItem(it.title, it.lessonId, it.state)
                }
            )
        }

    fun onNextPage(position: Int) {
        currentPosition.tryEmit(position)
    }

}