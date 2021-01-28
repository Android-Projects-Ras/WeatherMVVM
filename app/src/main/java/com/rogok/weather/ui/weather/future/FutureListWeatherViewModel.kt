package com.rogok.weather.ui.weather.future

import androidx.lifecycle.ViewModel
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.repository.ForecastRepository
import com.rogok.weather.internal.lazyDeferred

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val futureWeather by lazyDeferred {
        forecastRepository.getFutureWeather()
    }


    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}