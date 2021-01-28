package com.rogok.weather.data.db.entity.response.current

import com.google.gson.annotations.SerializedName

data class Sys (
    @SerializedName("country")
    val tzId: String
)