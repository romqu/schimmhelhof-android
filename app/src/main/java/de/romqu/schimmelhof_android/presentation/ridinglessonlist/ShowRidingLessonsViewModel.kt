package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.domain.LoadInitialLessonDaysService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.book.BookLessonRunnerFactory
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.day.RidingLessonDayItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.day.RidingLessonDayItemDiffCallback
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltViewModel
class ShowRidingLessonsViewModel @Inject constructor(
    ridingLessonDayRepository: RidingLessonDayRepository,
    private val savedStateHandle: SavedStateHandle,
    private val initialService: LoadInitialLessonDaysService,
    @Named(CURRENT_POSITION) private val currentPosition: MutableStateFlow<Int>,
    bookLessonRunnerFactory: BookLessonRunnerFactory,
) : ViewModel() {

    private val onListDispatched = MutableSharedFlow<Unit>()
    val bookLessonRunner by lazy {
        bookLessonRunnerFactory.create(viewModelScope)
    }

    private val unmodifiedItems = ridingLessonDayRepository.get()
        .map { entityList ->
            entityList.map { entity ->
                RidingLessonDayItem(
                    entity.day.date,
                    entity.day.weekday.name,
                    entity.lessons.map { lesson ->
                        RidingLessonItem(
                            title = lesson.title,
                            state = lesson.state,
                            id = lesson.id,
                            remoteId = lesson.remoteId,
                        )
                    }
                )
            }
        }.shareIn(
            viewModelScope, SharingStarted.Lazily
        )

    val setInitialItems = unmodifiedItems.take(1)

    private val lastScrollPosition = unmodifiedItems.flatMapMerge {
        currentPosition
    }

    val scrollToPosition = onListDispatched.drop(2).flatMapConcat {
        lastScrollPosition.take(1)
    }

    val dispatchListUpdates = unmodifiedItems
        .filter { it.isNotEmpty() }
        .scan(DispatchListUpdate()) { previous, next ->
            val diffResult =
                DiffUtil.calculateDiff(RidingLessonDayItemDiffCallback(previous.list, next))
            DispatchListUpdate(next, diffResult)
        }.filter { it.list.isNotEmpty() }

    class DispatchListUpdate(
        val list: List<RidingLessonDayItem> = emptyList(),
        val diffResult: DiffUtil.DiffResult? = null,
    )

    val updateDayName: Flow<String> =
        combine(unmodifiedItems.filter { it.isNotEmpty() }, currentPosition)
        { list, position ->
            val parentItem = list[position]
            "${parentItem.weekdayName} - ${parentItem.date.format(DateTimeFormatter.ofPattern("dd-MM"))}"
        }


    init {
        initLoadLessons()
    }

    private fun initLoadLessons() {
        viewModelScope.launch {
            initialService.execute().doOn({}, {})
        }
    }

    fun onNextPage(position: Int) {
        currentPosition.tryEmit(position)
    }

    fun onListDispatched() {
        viewModelScope.launch {
            onListDispatched.emit(Unit)
        }
    }
}