package com.rogok.weather.data.db.entity.response.current

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")//, indices = [(Index(value = ["id"], unique = true))])
data class CurrentWeatherResponse(

    @Embedded(prefix = "main_")
    val main: Main,
    @SerializedName("dt")
    val localTimeEpoch: Long,
    val name: String,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    @Embedded(prefix = "coord_")
    val coord: Coord

) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}