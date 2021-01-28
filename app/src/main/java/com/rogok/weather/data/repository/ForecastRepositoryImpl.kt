package com.rogok.weather.data.repository

import android.content.Context
import android.content.res.Resources
import android.view.inputmethod.EditorInfo
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.LiveData
import com.rogok.weather.data.db.entity.response.current.Coord
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherDao
import com.rogok.weather.data.db.entity.response.future.FutureWeatherDao
import com.rogok.weather.data.db.entity.response.future.FutureWeatherResponse
import com.rogok.weather.data.network.WeatherNetworkDataSource
import com.rogok.weather.data.network.provider.LocationProvider
import com.rogok.weather.ui.weather.current.CurrentWeatherFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime
import java.util.*

const val THIRTY_MINUTES_IN_UNIX = 1800

class ForecastRepositoryImpl(
    private val futureWeatherDao: FutureWeatherDao,
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    private val currentWeatherFragment = CurrentWeatherFragment()


    init {
        //Observing LiveData with forecast&writing to Room Database
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever {
                persistFetchedFutureWeather(it)

            }
        }
    }

    override suspend fun getCurrentWeather(context: Context): LiveData<CurrentWeatherResponse> {
        return withContext(Dispatchers.IO) {
            initWeatherData(context)
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<CurrentWeatherResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getLocation()
        }
    }

    override suspend fun getFutureWeather(): LiveData<FutureWeatherResponse>? {
        return withContext(Dispatchers.IO) {
            initFutureWeather()
            //fetchFutureWeather()
            return@withContext futureWeatherDao.getFutureWeather()
        }
    }

    private suspend fun initFutureWeather() {
        val futureWeather = futureWeatherDao.getFutureWeather()
        val lastWeatherLocation = currentWeatherDao.getLocationNonLive()

        if (futureWeather == null)
            fetchFutureWeather()

        if (lastWeatherLocation?.coord?.lat != null
            //&& isFetchNeeded(lastWeatherLocation)
        )
            fetchFutureWeather()
        return

    }

    //If no data then fetch it
    private suspend fun initWeatherData(context: Context) {
        val lastWeatherLocation = currentWeatherDao.getLocationNonLive()

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather(context)
            return
        }

        if (isFetchNeeded(lastWeatherLocation))
            fetchCurrentWeather(context)
    }

    private fun isFetchNeeded(lastFetchTime: CurrentWeatherResponse): Boolean {
        val plusThirtyMinutes = lastFetchTime.localTimeEpoch + THIRTY_MINUTES_IN_UNIX
        return lastFetchTime.localTimeEpoch > plusThirtyMinutes
    }

    //Getting data from API
    private suspend fun fetchCurrentWeather(context: Context) {
        locationProvider.getPreferredLocationString(context)?.let {
            weatherNetworkDataSource.fetchCurrentWeather(
                it,
                Locale.getDefault().language
            )
        }
    }

    private suspend fun fetchFutureWeather() {
        val locale = Resources.getSystem().getConfiguration().locale.country
        weatherNetworkDataSource.fetchFutureWeather(
            currentWeatherDao.getLocationNonLive()?.coord?.lat,
            currentWeatherDao.getLocationNonLive()?.coord?.lon,
            "ru"

        )
    }


    //Writing data to Room Database
    private fun persistFetchedCurrentWeather(fetchedWeatherResponse: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeatherResponse)
        }
    }

    private fun persistFetchedFutureWeather(newFutureWeather: FutureWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            futureWeatherDao.insert(newFutureWeather)
            //getWeatherLocation()
        }
    }
}