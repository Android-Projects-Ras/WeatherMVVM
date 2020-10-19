package com.rogok.weather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.rogok.weather.data.repository.ForecastRepository
import com.rogok.weather.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

}