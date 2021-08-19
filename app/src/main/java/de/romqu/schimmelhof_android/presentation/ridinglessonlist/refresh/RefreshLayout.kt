package de.romqu.schimmelhof_android.presentation.ridinglessonlist.refresh

import dagger.hilt.android.scopes.FragmentScoped
import de.romqu.schimmelhof_android.databinding.FragmentShowRidingLessonsBinding
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentListAdapter
import javax.inject.Inject

@FragmentScoped
class RefreshLayout @Inject constructor(
    private val refreshRunner: RefreshRunner,
) {

    lateinit var binding: FragmentShowRidingLessonsBinding

    fun setup(
        binding: FragmentShowRidingLessonsBinding,
        lessonsAdapter: RidingLessonParentListAdapter,
    ) {
        this.binding = binding
    }
}