package de.romqu.schimmelhof_android.presentation.ridinglessonlist.book

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ShowRidingLessonsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect


@ExperimentalCoroutinesApi
class BookLessonLayout
@AssistedInject constructor(
    @Assisted private val scope: LifecycleCoroutineScope,
    @Assisted private val context: Context,
    @Assisted private val viewModel: ShowRidingLessonsViewModel,
) {

    init {
        scope.launchWhenCreated {
            viewModel.bookLessonRunner.showSuccessMessage.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        scope.launchWhenCreated {
            viewModel.bookLessonRunner.showErrorMessage.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@AssistedFactory
interface BookLessonLayoutFactory {
    fun create(
        scope: LifecycleCoroutineScope,
        context: Context,
        viewModel: ShowRidingLessonsViewModel,
    ): BookLessonLayout
}
