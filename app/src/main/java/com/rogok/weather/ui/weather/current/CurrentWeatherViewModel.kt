package com.rogok.weather.ui.weather.current

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rogok.weather.data.repository.ForecastRepository
import com.rogok.weather.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val context: Context
) : ViewModel() {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(context)
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

}