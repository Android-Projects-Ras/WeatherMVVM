package com.rogok.weather.internal

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rogok.weather.data.db.entity.response.current.Weather
import com.rogok.weather.data.db.entity.response.future.Daily
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
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
    fun jsonToList(value: String?): List<Weather> {
        if (value == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun listDailyToJson(list: List<Daily>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun jsonDailyToList(value: String?): List<Daily> {
        if (value == null)
            return Collections.emptyList()

        val listType = object : TypeToken<List<Daily>>() {}.type
        return gson.fromJson(value, listType)
    }

}