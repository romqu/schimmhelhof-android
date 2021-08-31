package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
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

    private val currentPosition = MutableStateFlow(0)

    val ridingLessonParentItems = MutableStateFlow(emptyList<RidingLessonParentItem>())

    private val currentParent: Flow<RidingLessonParentItem> =
        ridingLessonParentItems.combine(currentPosition) { list, position ->
            list[position]
        }

    private val currentChildList: Flow<List<RidingLessonChildItem>> =
        currentPosition.combine(ridingLessonParentItems) { position, list ->
            list[position].childs
        }

    private val parentAndChildList =
        ridingLessonParentItems.zip(currentChildList) { parent, child ->
            Pair(parent, child)
        }

    private val bookResult =
        parentAndChildList.zip(onItemClickChannel) { parentAndChild, clickedPosition ->
            val item = parentAndChild.second[clickedPosition]
            bookLessonRunner.execute(item.id, parentAndChild.first)
        }.map { result ->
            result.doOn({ ActionResult.Book(it) }, { ActionResult.BookBad("oh no") })
        }

    sealed class ActionResult {
        class Book(val diffResult: DiffUtil.DiffResult) : ActionResult()
        class BookBad(val message: String) : ActionResult()
        class Cancel(val diffResult: DiffUtil.DiffResult) : ActionResult()
        class CancelBad(val message: String) : ActionResult()
    }

    val dispatchBookedList = bookResult
        .filterIsInstance<ActionResult.Book>()


    val updateDayName: Flow<String> =
        combine(ridingLessonParentItems.filter { it.isNotEmpty() }, currentPosition)
        { list, position ->
            val parentItem = list[position]
            "${parentItem.weekdayName} - ${parentItem.date.format(DateTimeFormatter.ofPattern("dd-MM"))}"
        }


    init {
        getLessons()
    }

    private fun getLessons() {
        viewModelScope.launch {
            getRidingLessonsService.execute().doOn({ ridingLessonDayDtos ->
                ridingLessonParentItems.value = ridingLessonDayDtos.map { dayDto ->
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
        currentPosition.tryEmit(position)
    }

}