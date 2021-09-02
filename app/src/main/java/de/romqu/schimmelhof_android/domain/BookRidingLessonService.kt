package de.romqu.schimmelhof_android.domain

import de.romqu.schimmelhof_android.data.RidingLessonDto
import de.romqu.schimmelhof_android.data.ridinglesson.RidingLessonRepository
import de.romqu.schimmelhof_android.shared.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRidingLessonService @Inject constructor(
    private val repository: RidingLessonRepository,
) {
    suspend fun execute(id: String) =
        repository.book(id)
            .map {
                val cachedList = repository.getCache()
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

                repository.updateCache(newList)
            }
}