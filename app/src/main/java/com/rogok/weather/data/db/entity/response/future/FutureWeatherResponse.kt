package com.rogok.weather.data.db.entity.response.future


import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.rogok.weather.internal.Converters
@Entity(tableName = "future_weather")
//@Entity(tableName = "future_weather", indices = [Index(value = ["weatherDate"], unique = true)])
data class FutureWeatherResponse(

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Double
)