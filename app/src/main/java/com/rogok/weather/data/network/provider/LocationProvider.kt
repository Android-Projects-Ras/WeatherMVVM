package com.rogok.weather.data.network.provider

import android.content.Context
import com.rogok.weather.data.db.entity.response.current.Coord
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse

interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: CurrentWeatherResponse): Boolean
    suspend fun getPreferredLocationString(context: Context): String?
    fun getCustomLocationName(): String?

}