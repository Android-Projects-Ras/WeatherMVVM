package com.rogok.weather

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.rogok.weather.data.db.ForecastDatabase
import com.rogok.weather.data.network.*
import com.rogok.weather.data.network.provider.LocationProvider
import com.rogok.weather.data.network.provider.LocationProviderImpl
import com.rogok.weather.data.repository.ForecastRepository
import com.rogok.weather.data.repository.ForecastRepositoryImpl
import com.rogok.weather.ui.weather.current.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { OpenWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl() }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}