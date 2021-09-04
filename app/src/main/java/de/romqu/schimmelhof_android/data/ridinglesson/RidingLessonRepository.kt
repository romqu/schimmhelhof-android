package de.romqu.schimmelhof_android.data.ridinglesson

import de.romqu.schimmelhofandroid.sql.RidingLessonEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonEntityQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RidingLessonRepository @Inject constructor(
    private val dao: RidingLessonEntityQueries,
) {
    fun save(list: List<RidingLessonEntity>) {
        list.map { dao.save(it) }
    }
}