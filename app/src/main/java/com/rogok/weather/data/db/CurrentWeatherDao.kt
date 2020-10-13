package com.rogok.weather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rogok.weather.data.db.entity.CURRENT_WEATHER_ID
import com.rogok.weather.data.db.entity.CurrentWeatherResponseEntity

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeather: CurrentWeatherResponseEntity)

    @Query("SELECT * FROM current_weather WHERE ID = $CURRENT_WEATHER_ID")
    fun getWeather(): LiveData<CurrentWeatherResponseEntity>
}