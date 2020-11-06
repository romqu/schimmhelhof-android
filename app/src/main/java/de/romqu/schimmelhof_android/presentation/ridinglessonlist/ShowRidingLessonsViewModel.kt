package de.romqu.schimmelhof_android.presentation.ridinglessonlist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.child.RidingLessonChildItem
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.parent.RidingLessonParentItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ShowRidingLessonsViewModel @ViewModelInject constructor(
    private val ridingLessonRepository: RidingLessonRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val ridingLessonItems = MutableStateFlow(emptyList<RidingLessonParentItem>())
    val ridingLessonDayName = MutableStateFlow("")


    init {
        viewModelScope.launch {
            ridingLessonRepository.getRidingLessonDays().doOn({ dto ->

                ridingLessonDayName.value = dto.ridingLessonDayDtos.first().weekday.name

                ridingLessonItems.value = dto.ridingLessonDayDtos.map { dayDto ->
                    RidingLessonParentItem(
                        dayDto.ridingLessons.map { RidingLessonChildItem((it.title)) }
                    )
                }
            }, {

            })
        }
    }

    fun onNextPage(position: Int){

    }

}