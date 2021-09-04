package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import de.romqu.schimmelhof_android.data.RidingLessonDayDto
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Named

const val CURRENT_LESSON_DAYS = "CURRENT_LESSON_DAYS"
const val ON_ITEM_CLICK = "ON_ITEM_CLICK"
const val CURRENT_POSITION = "CURRENT_POSITION"
const val CURRENT_PARENT_ITEMS = "CURRENT_PARENT_ITEMS"
const val CURRENT_PARENT = "CURRENT_PARENT"
const val CURRENT_CHILD_ITEMS = "CURRENT_CHILD_ITEMS"
const val OBSERVE_ITEMS = "OBSERVE_ITEMS"

@Module
@InstallIn(ActivityRetainedComponent::class)
object ShowRidingLessonsRetainedModule {


    @Provides
    @ActivityRetainedScoped
    @Named(CURRENT_LESSON_DAYS)
    fun provideCurrentItems(
        ridingLessonDayRepository: RidingLessonDayRepository,
    ) = ridingLessonDayRepository.get()

    @Provides
    @ActivityRetainedScoped
    @Named(ON_ITEM_CLICK)
    fun provideOnItemClickChannel(): MutableSharedFlow<Int> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)

    @Provides
    @ActivityRetainedScoped
    @Named(CURRENT_POSITION)
    fun currentPosition(): MutableStateFlow<Int> = MutableStateFlow(0)

    @Provides
    @ActivityRetainedScoped
    @Named(CURRENT_PARENT_ITEMS)
    fun ridingLessonParentItems(): MutableStateFlow<List<@JvmSuppressWildcards RidingLessonParentItem>> =
        MutableStateFlow(emptyList())

    @ExperimentalCoroutinesApi
    @Provides
    @ActivityRetainedScoped
    @Named(OBSERVE_ITEMS)
    fun observeItems(
        @Named(CURRENT_PARENT_ITEMS) items: MutableStateFlow<List<RidingLessonParentItem>>,
        dayRepository: RidingLessonDayRepository,
    ): Flow<List<@JvmSuppressWildcards RidingLessonParentItem>> =
        merge(items, dayRepository.observe().map(::toItems))

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

    @Provides
    @ActivityRetainedScoped
    @Named(CURRENT_PARENT)
    fun currentParent(
        @Named(CURRENT_PARENT_ITEMS) items: MutableStateFlow<List<RidingLessonParentItem>>,
        @Named(CURRENT_POSITION) currentPosition: MutableStateFlow<Int>,
    ): Flow<@JvmSuppressWildcards RidingLessonParentItem> =
        items.combine(currentPosition) { list, position ->
            list[position]
        }

    @Provides
    @ActivityRetainedScoped
    @Named(CURRENT_CHILD_ITEMS)
    fun currentChildList(
        @Named(CURRENT_POSITION) currentPosition: MutableStateFlow<Int>,
        @Named(CURRENT_PARENT_ITEMS) ridingLessonParentItems: MutableStateFlow<List<RidingLessonParentItem>>,
    ): Flow<List<@JvmSuppressWildcards RidingLessonChildItem>> =
        currentPosition.combine(ridingLessonParentItems) { position, list ->
            list[position].childs
        }
}