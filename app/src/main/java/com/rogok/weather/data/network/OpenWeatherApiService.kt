package com.rogok.weather.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rogok.weather.data.db.entity.response.current.CurrentWeatherResponse
import com.rogok.weather.data.db.entity.response.future.Daily
import com.rogok.weather.data.db.entity.response.future.FutureWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"


//https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}&lang=en&units=metric

interface OpenWeatherApiService {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en",
        @Query("units") units: String = "metric"
    ): Deferred<CurrentWeatherResponse>

//https://api.openweathermap.org/data/2.5/onecall?lat=47.8388&lon=35.1396&exclude=hourly&lang=ru&units=metric&appid=00d9ac59b4bdc924164e509194b86c43

    @GET("onecall")
    fun getFutureWeather(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("exclude") exclude: String = "hourly",
        @Query("lang") languageCode: String = "en",
        @Query("units") units: String = "metric"
    ): Deferred<FutureWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApiService::class.java)
        }
    }
}
