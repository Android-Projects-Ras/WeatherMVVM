package com.rogok.weather.data.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
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
    val sys: Sys

) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID

    val zonedDateTime: ZonedDateTime
    @RequiresApi(Build.VERSION_CODES.O)
    get() {
            val instant = Instant.ofEpochSecond(localTimeEpoch)
            val zoneId = ZoneId.of(sys.tzId)
            return ZonedDateTime.ofInstant(instant, zoneId)
    }




}