package com.rogok.weather.data.response


import com.google.gson.annotations.SerializedName

data class Sys(
    val country: String,
    val id: Double,
    val message: Double,
    val sunrise: Double,
    val sunset: Double,
    val type: Double
)