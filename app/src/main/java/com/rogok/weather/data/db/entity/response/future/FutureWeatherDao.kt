package com.rogok.weather.data.db.entity.response.future

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherDays: FutureWeatherResponse?)

    @Query("SELECT * FROM future_weather")
    fun getFutureWeather() : LiveData<FutureWeatherResponse>?

    @Query("DELETE FROM future_weather")
    fun deleteFutureWeather()

    /*@Query("SELECT * FROM future_weather")
    fun getLocation(): LiveData<CurrentWeatherResponse>*/
}