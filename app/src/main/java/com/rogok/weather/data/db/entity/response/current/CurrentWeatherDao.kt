package com.rogok.weather.data.db.entity.response.current

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherResponse: CurrentWeatherResponse)// return id?

    @Query("SELECT * FROM current_weather") // WHERE ID = id
    fun getWeather(): LiveData<CurrentWeatherResponse>

    @Query("SELECT * FROM current_weather")
    fun getLocation(): LiveData<CurrentWeatherResponse>

    @Query("SELECT * FROM current_weather")
    fun getLocationNonLive(): CurrentWeatherResponse?

    @Query("DELETE FROM current_weather")
    fun deleteCurrentWeather()
}

//WHERE ID = $CURRENT_WEATHER_ID
//WHERE ID=687700