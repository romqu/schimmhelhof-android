package de.romqu.schimmelhof_android.presentation.ridinglessonlist.logout

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.romqu.schimmelhof_android.databinding.FragmentShowRidingLessonsBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ShowRidingLessonsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class LogoutLayout
@AssistedInject constructor(
    @Assisted
    private val viewModel: ShowRidingLessonsViewModel,
    @Assisted
    private val binding: FragmentShowRidingLessonsBinding,
) {

    init {
        binding.logoutTextView.setOnClickListener {
            viewModel.logoutLessonRunner.onLogoutClick()
        }
    }
}

@AssistedFactory
interface LogoutLayoutFactory {
    fun create(
        viewModel: ShowRidingLessonsViewModel,
        binding: FragmentShowRidingLessonsBinding,
    ): LogoutLayout
}
