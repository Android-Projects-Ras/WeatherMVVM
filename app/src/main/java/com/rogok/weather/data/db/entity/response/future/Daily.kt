package com.rogok.weather.data.db.entity.response.future


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Daily(
    val clouds: Double,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("dt")
    val weatherDate: Long,
    @Embedded(prefix = "feelsLike_")
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    val humidity: Double,
    val pop: Double,
    val pressure: Double,
    val sunrise: Double,
    val sunset: Double,
    @Embedded(prefix = "temp_")
    val temp: Temp,
    @Embedded
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)