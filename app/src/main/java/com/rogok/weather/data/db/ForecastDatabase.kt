package com.rogok.weather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rogok.weather.data.db.entity.CurrentWeatherResponse
import com.rogok.weather.internal.Converters

@Database(
    entities = [CurrentWeatherResponse::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao() : CurrentWeatherDao

    companion object {
        @Volatile private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
            ForecastDatabase::class.java, "forecast.db")
                .build()
    }

}