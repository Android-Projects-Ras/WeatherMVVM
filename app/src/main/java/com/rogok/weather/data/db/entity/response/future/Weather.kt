package com.rogok.weather.data.db.entity.response.future


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)