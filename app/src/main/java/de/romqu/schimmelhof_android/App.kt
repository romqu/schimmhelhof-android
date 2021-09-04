package de.romqu.schimmelhof_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonEntity

@HiltAndroidApp
class App : Application() {


}

class LessonsDay(
    day: RidingLessonDayEntity,
    lessons: List<RidingLessonEntity>,
)