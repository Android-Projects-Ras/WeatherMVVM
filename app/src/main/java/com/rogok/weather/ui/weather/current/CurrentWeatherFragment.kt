package com.rogok.weather.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rogok.weather.R
import com.rogok.weather.data.network.WeatherNetworkDataSource
import com.rogok.weather.data.network.provider.LocationProvider
import com.rogok.weather.internal.getOpenWeatherIconUrl
import com.rogok.weather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.math.roundToInt

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val weatherNetworkDataSource: WeatherNetworkDataSource by instance()
    private val locationProvider: LocationProvider by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

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

        /*edit_text_custom_location?.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_GO -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        //locationProvider.getCustomLocationName()?.let {
                        val usersLocationWeather = edit_text_custom_location?.text?.toString()
                        if (usersLocationWeather != null) {
                            weatherNetworkDataSource.fetchCurrentWeather(
                                usersLocationWeather,
                                Locale.getDefault().language
                            )
                        }
                        //}

                    }
                    true
                }
                else -> false
            }
        }*/
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
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
            val icon = getOpenWeatherIconUrl(it.weather[0].icon)
            Glide.with(this@CurrentWeatherFragment)
                .load(icon)
                .into(imageView_condition_icon)
        })
    }

    private fun updateLocation(location: String?) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location ?: ""
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            getString(R.string.current_action_bar_subtitle)
    }

    private fun updateTemperatures(temperature: Int, feelsLike: Double) {
        textView_temperature.text = "$temperature °C"
        (getString(R.string.feels_like_temp) + " " + feelsLike + " °C").also {
            textView_feels_like_temperature.text = it
        }
    }

    private fun updateDescription(description: String) {
        textView_description.text = description
    }

    private fun updateHumidity(humidity: Int) {
        (getString(R.string.humidity) + " " + humidity + " %").also { textView_humidity.text = it }
    }

    private fun updateWindSpeed(windSpeed: Int) {
        (getString(R.string.wind_speed) + " " + windSpeed + getString(R.string.meters_per_sec)).also {
            textView_wind_speed.text = it
        }
    }

}