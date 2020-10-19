package com.rogok.weather.data.repository

import androidx.lifecycle.LiveData
import com.rogok.weather.data.db.entity.CurrentWeatherResponse

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse>
    suspend fun getWeatherLocation(): LiveData<CurrentWeatherResponse>
}