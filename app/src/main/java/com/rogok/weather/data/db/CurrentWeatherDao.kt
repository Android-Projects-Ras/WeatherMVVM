package com.rogok.weather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rogok.weather.data.db.entity.CURRENT_WEATHER_ID
import com.rogok.weather.data.db.entity.CurrentWeatherResponse

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeather: CurrentWeatherResponse)

    @Query("SELECT * FROM current_weather")
    fun getWeather(): LiveData<CurrentWeatherResponse>

    @Query("SELECT * FROM current_weather")
    fun getLocation(): LiveData<CurrentWeatherResponse>
}

//WHERE ID = $CURRENT_WEATHER_ID