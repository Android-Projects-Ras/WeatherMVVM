package com.rogok.weather.data.network.provider

import com.rogok.weather.data.db.entity.CurrentWeatherResponse

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: CurrentWeatherResponse): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "New Delhi"
    }
}