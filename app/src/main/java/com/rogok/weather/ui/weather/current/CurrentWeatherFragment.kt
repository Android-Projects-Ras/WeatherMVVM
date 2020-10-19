package com.rogok.weather.ui.weather.current

import android.os.Bundle
import android.os.DeadObjectException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rogok.weather.R
import com.rogok.weather.internal.glide.GlideApp
import com.rogok.weather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var urlForGlide: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location->
            if (location == null) return@Observer
            updateLocation(location.name)

        })


        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateDateToToday()
            updateTemperatures(it.main.temp.roundToInt(), it.main.feelsLike)
            updateDescription(it.weather[0].description)
            updateHumidity(it.main.humidity.roundToInt())
            updateWindSpeed(it.wind.speed.roundToInt())
            getOpenWeatherIcon(it.weather[0].icon)
            GlideApp.with(this@CurrentWeatherFragment)
                .load(urlForGlide)
                .into(imageView_condition_icon)
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Int, feelsLike: Double) {
        textView_temperature.text = "$temperature °C"
        textView_feels_like_temperature.text = "Feels like: $feelsLike °C"
    }

    private fun updateDescription(description: String) {
        textView_description.text = description
    }

    private fun updateHumidity(humidity: Int) {
        textView_humidity.text = "Humidity: $humidity %"
    }

    private fun updateWindSpeed(windSpeed: Int) {
        textView_wind_speed.text = "Wind speed: $windSpeed mps"
    }

    private fun getOpenWeatherIcon(icon: String) {
        when (icon) {
            "01d" -> urlForGlide = "http://openweathermap.org/img/wn/01d@2x.png"
            "01n" -> urlForGlide = "http://openweathermap.org/img/wn/01n@2x.png"
            "02d" -> urlForGlide = "http://openweathermap.org/img/wn/02d@2x.png"
            "02n" -> urlForGlide = "http://openweathermap.org/img/wn/02n@2x.png"
            "03d" -> urlForGlide = "http://openweathermap.org/img/wn/03d@2x.png"
            "03n" -> urlForGlide = "http://openweathermap.org/img/wn/03n@2x.png"
            "04d" -> urlForGlide = "http://openweathermap.org/img/wn/04d@2x.png"
            "04n" -> urlForGlide = "http://openweathermap.org/img/wn/04n@2x.png"
            "09d" -> urlForGlide = "http://openweathermap.org/img/wn/09d@2x.png"
            "09n" -> urlForGlide = "http://openweathermap.org/img/wn/09n@2x.png"
            "10d" -> urlForGlide = "http://openweathermap.org/img/wn/10d@2x.png"
            "10n" -> urlForGlide = "http://openweathermap.org/img/wn/10n@2x.png"
            "11d" -> urlForGlide = "http://openweathermap.org/img/wn/11d@2x.png"
            "11n" -> urlForGlide = "http://openweathermap.org/img/wn/11n@2x.png"
            "13d" -> urlForGlide = "http://openweathermap.org/img/wn/13d@2x.png"
            "13n" -> urlForGlide = "http://openweathermap.org/img/wn/13n@2x.png"
            "50d" -> urlForGlide = "http://openweathermap.org/img/wn/50d@2x.png"
            "50n" -> urlForGlide = "http://openweathermap.org/img/wn/50n@2x.png"
        }
    }


}