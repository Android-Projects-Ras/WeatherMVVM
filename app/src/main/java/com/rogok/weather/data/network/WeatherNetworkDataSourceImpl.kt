package com.rogok.weather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.db.entity.response.future.Daily
import com.rogok.weather.data.db.entity.response.future.FutureWeatherResponse
import com.rogok.weather.internal.NoConnectivityException

//Getting data from openWeatherApiService & persisting in MutableLiveData
class WeatherNetworkDataSourceImpl(
    private val openWeatherApiService: OpenWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = openWeatherApiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(
        latitude: Double?,
        longitude: Double?,
        languageCode: String
    ) {
        try {
            val fetchedFutureWeather = openWeatherApiService
                .getFutureWeather(latitude, longitude, languageCode)
                .await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}