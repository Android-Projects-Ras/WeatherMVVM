package com.rogok.weather.data.network.provider

import com.rogok.weather.data.db.entity.CurrentWeatherResponse

interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: CurrentWeatherResponse): Boolean
    suspend fun getPreferredLocationString(): String
}