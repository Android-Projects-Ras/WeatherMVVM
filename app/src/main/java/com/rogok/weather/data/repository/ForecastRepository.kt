package com.rogok.weather.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.db.entity.response.future.FutureWeatherResponse

interface ForecastRepository {
    suspend fun getCurrentWeather(context: Context): LiveData<CurrentWeatherResponse>
    suspend fun getWeatherLocation(): LiveData<CurrentWeatherResponse>
    suspend fun getFutureWeather(): LiveData<FutureWeatherResponse>?

}