package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.lesson.RidingLessonItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named

const val ON_ITEM_CLICK = "ON_ITEM_CLICK"
const val CURRENT_POSITION = "CURRENT_POSITION"

@Module
@InstallIn(ActivityRetainedComponent::class)
object ShowRidingLessonsModule {

    @Provides
    @ActivityRetainedScoped
    @Named(ON_ITEM_CLICK)
    fun provideOnItemClickChannel(): MutableSharedFlow<RidingLessonItem> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)

    @Provides
    @ActivityRetainedScoped
    @Named(CURRENT_POSITION)
    fun currentPosition(): MutableStateFlow<Int> = MutableStateFlow(0)
}