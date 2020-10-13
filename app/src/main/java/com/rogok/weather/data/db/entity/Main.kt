package com.rogok.weather.data.db.entity
import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Double,
    val temp: Double
)