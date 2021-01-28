package com.rogok.weather.ui.weather.future

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.rogok.weather.R
import com.rogok.weather.data.db.entity.response.future.Daily
import com.rogok.weather.internal.getOpenWeatherIconUrl
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_future_weather.*
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class FutureWeatherItem(
    private val dailyFutureWeather: Daily,
    private val context: Context
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = dailyFutureWeather.weather[0].description
            updateDate()
            updateTemperature()
            updateConditionImage()

        }
    }

    override fun getLayout() = R.layout.item_future_weather

    private fun GroupieViewHolder.updateDate() {
        val date = unixToDate(dailyFutureWeather.weatherDate)
        textView_date.text = date
    }

    private fun unixToDate(unixTime: Long?) = unixTime?.let {
        SimpleDateFormat("E, dd/MM/yyyy", Locale.getDefault()).format(Date(unixTime.toLong() * 1000))
    }

    private fun GroupieViewHolder.updateTemperature() {
        textView_temperature.text = dailyFutureWeather.temp.day.toString()
    }

    private fun GroupieViewHolder.updateConditionImage() {

            val icon = dailyFutureWeather.weather[0].icon
            val iconUrl = getOpenWeatherIconUrl(icon)
            Glide.with(context)
                .load(iconUrl)
                .into(imageView_item_condition_icon)
    }

}