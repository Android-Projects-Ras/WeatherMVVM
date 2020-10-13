package com.rogok.weather.data.network.response

import com.rogok.weather.data.db.entity.*


data class CurrentWeatherResponse(
    val main: Main,
    val name: String,
    val weather: List<Weather>,
    val wind: Wind

)