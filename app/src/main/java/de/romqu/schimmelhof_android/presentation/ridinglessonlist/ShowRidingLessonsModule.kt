package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named

const val ON_ITEM_CLICK = "ON_ITEM_CLICK"

@Module
@InstallIn(ActivityRetainedComponent::class)
class ShowRidingLessonsModule {

    @Provides
    @ActivityRetainedScoped
    @Named(ON_ITEM_CLICK)
    fun provideOnItemClickChannel(): MutableSharedFlow<Int> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
}