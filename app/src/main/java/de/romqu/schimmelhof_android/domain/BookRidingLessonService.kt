package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.data.RidingLessonDto
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonDayRepository
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRidingLessonService @Inject constructor(
    private val dayRepository: RidingLessonDayRepository,
) {
    suspend fun execute(id: String) =
        dayRepository.book(id)
            .map {
                val cachedList = dayRepository.getCache()
                val ridingLessonDayWithIndex = cachedList
                    .mapIndexed { index, dayDto ->
                        val lesson = dayDto.ridingLessons.find { it.lessonId == id }
                        if (lesson != null) {
                            val newLesson =
                                lesson.copy(state = RidingLessonDto.RidingLessonState.BOOKED)
                            val newLessons = dayDto.ridingLessons.subtract(listOf(lesson))
                                .union(listOf(newLesson))
                            val newLessonDay = dayDto.copy(ridingLessons = newLessons.toList())
                            Pair(newLessonDay,
                                index
                            )
                        } else {
                            null
                        }
                    }.filterNotNull().first()

                val newList = cachedList.filterIndexed { index, _ ->
                    index != ridingLessonDayWithIndex.second
                }.union(listOf(ridingLessonDayWithIndex.first)).toList()

                dayRepository.updateCache(newList)
            }
}