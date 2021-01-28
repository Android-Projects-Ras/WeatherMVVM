package com.rogok.weather.ui.weather.future

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.repository.ForecastRepository

class FutureListWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListWeatherViewModel(forecastRepository) as T
    }
}