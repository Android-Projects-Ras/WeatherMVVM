package com.rogok.weather.data.network

import androidx.lifecycle.LiveData
import com.rogok.weather.data.db.entity.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}