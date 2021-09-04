package de.romqu.schimmelhof_android.data.shared

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.romqu.schimmelhof_android.Database
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonDayEntityQueries
import de.romqu.schimmelhofandroid.sql.RidingLessonEntity
import de.romqu.schimmelhofandroid.sql.RidingLessonEntityQueries
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        val driver: SqlDriver = AndroidSqliteDriver(Database.Schema,
            context,
            "schimmelhof.db",
            callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.query("PRAGMA journal_mode=WAL;")
                    db.query("PRAGMA foreign_keys = ON;")
                }
            })

        return Database(
            driver,
            ridingLessonEntityAdapter = RidingLessonEntity.Adapter(
                stateAdapter = EnumColumnAdapter(),
                actionAdapter = EnumColumnAdapter(),
                weekdayAdapter = EnumColumnAdapter(),
                dateAdapter = LocalDateColumnAdapter(),
                fromTimeAdapter = LocalTimeColumnAdapter(),
                toTimeAdapter = LocalTimeColumnAdapter(),
            ),
            ridingLessonDayEntityAdapter = RidingLessonDayEntity.Adapter(
                weekdayAdapter = EnumColumnAdapter(),
                dateAdapter = LocalDateColumnAdapter(),
            )
        )
    }

    @Singleton
    @Provides
    fun provideRidingLessonDayDao(database: Database): RidingLessonDayEntityQueries =
        database.ridingLessonDayEntityQueries

    @Singleton
    @Provides
    fun provideRidingLessonDao(database: Database): RidingLessonEntityQueries =
        database.ridingLessonEntityQueries

}

class LocalDateColumnAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate =
        LocalDate.parse(databaseValue)


    override fun encode(value: LocalDate): String =
        value.toString()
}

class LocalTimeColumnAdapter : ColumnAdapter<LocalTime, String> {
    override fun decode(databaseValue: String): LocalTime =
        LocalTime.parse(databaseValue)


    override fun encode(value: LocalTime): String =
        value.toString()
}

