package com.rogok.weather.data.db.entity.response.future


import com.google.gson.annotations.SerializedName

data class FeelsLike(
    @SerializedName("day")
    val feelsLikeDay: Double,
    @SerializedName("eve")
    val feelsLikeEve: Double,
    @SerializedName("morn")
    val feelsLikeMorn: Double,
    @SerializedName("night")
    val feelsLikeNight: Double
)