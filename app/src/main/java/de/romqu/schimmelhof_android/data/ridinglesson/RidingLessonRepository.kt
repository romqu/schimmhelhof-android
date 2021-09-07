package de.romqu.schimmelhof_android.data.ridinglesson

import de.romqu.schimmelhof_android.data.BookRidingLessonInDto
import de.romqu.schimmelhof_android.data.ridinglessonday.RidingLessonApi
import de.romqu.schimmelhof_android.data.shared.ApiCall
import de.romqu.schimmelhof_android.shared.Result
import de.romqu.schimmelhofandroid.sql.RidingLessonEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonEntityQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RidingLessonRepository @Inject constructor(
    private val dao: RidingLessonEntityQueries,
    private val api: RidingLessonApi,
) {
    fun save(list: List<RidingLessonEntity>) {
        list.map { dao.save(it) }
    }

    fun update(id: Long, state: RidingLessonStateEntity, action: RidingLessonActionEntity) {
        dao.update(state, action, id)
    }

    suspend fun book(id: String): Result<ApiCall.Error, BookRidingLessonInDto> {
        return Result.Success(BookRidingLessonInDto("", id))
        // return executeBodyCall { api.bookLesson(id) }
    }
}