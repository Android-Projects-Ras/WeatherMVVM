package com.rogok.weather.data.network

import androidx.lifecycle.LiveData
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.db.entity.response.future.Daily
import com.rogok.weather.data.db.entity.response.future.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        latitude: Double?,
        longitude: Double?,
        languageCode: String
    )
}