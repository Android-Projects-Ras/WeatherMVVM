package com.rogok.weather.internal

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rogok.weather.data.db.entity.Weather
import java.util.*

class Converters {
    private val gson = Gson()

    //list to string
    @TypeConverter
    fun listToJson(list: List<Weather>): String {
        return gson.toJson(list)
    }

    //strings to list
    @TypeConverter
    fun JsonToList(value: String?): List<Weather> {
        if (value == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(value, listType)
    }
}