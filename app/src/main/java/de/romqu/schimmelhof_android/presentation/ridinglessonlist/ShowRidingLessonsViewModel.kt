package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonActionEntity
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.data.user.UserRepository
import de.romqu.schimmelhof_android.domain.LoadInitialLessonDaysService
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.clicked.ClickedLessonRunnerFactory
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.day.RidingLessonDayItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.logout.LogoutRunnerFactory
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
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle,
    private val initialService: LoadInitialLessonDaysService,
    @Named(CURRENT_POSITION) private val currentPosition: MutableStateFlow<Int>,
    clickedLessonRunnerFactory: ClickedLessonRunnerFactory,
    logoutRunnerFactory: LogoutRunnerFactory,
) : ViewModel() {

    private val onListDispatched = MutableSharedFlow<Unit>()
    val bookLessonRunner by lazy {
        clickedLessonRunnerFactory.create(viewModelScope)
    }

    val logoutRunner by lazy {
        logoutRunnerFactory.create(viewModelScope)
    }

    private val unmodifiedItems = ridingLessonDayRepository.get()
        .map { entityList ->
            entityList.map { entity ->
                RidingLessonDayItem(
                    entity.day.date,
                    entity.day.weekday.german,
                    entity.lessons
                        .filter {
                            it.action == RidingLessonActionEntity.BOOK
                                    || it.action == RidingLessonActionEntity.CANCEL_BOOKING
                        }
                        .map { lesson ->
                            RidingLessonItem(
                                title = lesson.title,
                                time = "${lesson.fromTime} - ${lesson.toTime}",
                                teacher = lesson.teacher,
                                state = lesson.state,
                                id = lesson.id,
                                remoteId = lesson.remoteId,
                                isEnabled = lesson.action == RidingLessonActionEntity.BOOK
                                        || lesson.action == RidingLessonActionEntity.CANCEL_BOOKING,
                                action = lesson.action,
                            )
                        }
                )
            }
        }
        .shareIn(
            viewModelScope, SharingStarted.Lazily
        )

    val setInitialItems = unmodifiedItems.take(1)

    private val lastScrollPosition = unmodifiedItems.flatMapMerge {
        currentPosition
    }

    val dispatchListUpdates = unmodifiedItems
/*        .filter { it.isNotEmpty() }
        .scan(DispatchListUpdate()) { previous, next ->
            val diffResult =
                DiffUtil.calculateDiff(RidingLessonDayItemDiffCallback(previous.list, next))
            DispatchListUpdate(next, diffResult)
        }.filter { it.list.isNotEmpty() }*/

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

    fun onLogoutClick() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}