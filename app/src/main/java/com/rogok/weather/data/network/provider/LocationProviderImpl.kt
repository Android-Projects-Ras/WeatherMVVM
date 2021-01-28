package com.rogok.weather.data.network.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.internal.LocationPermissionNotGrantedException
import com.rogok.weather.internal.asDeferred
import com.rogok.weather.ui.weather.current.CurrentWeatherFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Deferred
import java.util.*

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : LocationProvider {

    private val appContext = context.applicationContext
    private val currentWeatherFragment = CurrentWeatherFragment()

    override suspend fun hasLocationChanged(
        lastWeatherLocation: CurrentWeatherResponse
    ): Boolean {
        //return true
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferredLocationString(context: Context): String? {
        //return "Запорожье"
        if (!isUsingDeviceLocation()) { //!true
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"

                    return getCityName(deviceLocation, context)

            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else
            return "${getCustomLocationName()}"
    }


    private fun getCityName(deviceLocation: Location, context: Context): String? {

        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = deviceLocation.latitude.let {
            geocoder.getFromLocation(
                it,
                deviceLocation.longitude,
                1
            )
        }
        return addresses?.get(0)?.locality
    }

    private suspend fun hasDeviceLocationChanged(
        lastWeatherLocation: CurrentWeatherResponse
    ): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.coord.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.coord.lon) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(
        lastWeatherLocation: CurrentWeatherResponse
    ): Boolean {
        if (isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }

    private fun isUsingDeviceLocation(): Boolean {//true
        val customLocation = getCustomLocationName()
        return customLocation == null
    }

    override fun getCustomLocationName(): String? {
        return currentWeatherFragment.activity?.edit_text_custom_location?.text?.toString() ?: ""

    }





    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}