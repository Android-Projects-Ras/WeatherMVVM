package com.rogok.weather.data.repository

import androidx.lifecycle.LiveData
import com.rogok.weather.data.db.CurrentWeatherDao
import com.rogok.weather.data.db.entity.CurrentWeatherResponse
import com.rogok.weather.data.network.WeatherNetworkDataSource
import com.rogok.weather.data.network.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {
    init {
        //Observing LiveData with forecast&writing to Room Database
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<CurrentWeatherResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getLocation()
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = currentWeatherDao.getLocation().value

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    //Getting data from API
    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    //Writing data to Room Database
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather)
        }
    }
}