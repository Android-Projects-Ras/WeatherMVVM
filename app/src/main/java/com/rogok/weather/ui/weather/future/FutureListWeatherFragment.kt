package com.rogok.weather.ui.weather.future

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rogok.weather.R
import com.rogok.weather.data.db.entity.response.future.Daily
import com.rogok.weather.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@RequiresApi(Build.VERSION_CODES.O)
class FutureListWeatherFragment : ScopedFragment(), KodeinAware {

    private lateinit var viewModel: FutureListWeatherViewModel
    override val kodein by closestKodein()
    private val viewModelFactory: FutureListWeatherViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FutureListWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.futureWeather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        futureWeather?.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateDateToNextWeek()
            initRecyclerView(it.daily.toFutureWeatherItems(requireContext()))

        })

        weatherLocation.observe(viewLifecycleOwner, Observer { location->
            if (location == null) return@Observer
            updateLocation(location.name)
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.future_next_week)
    }

    private fun List<Daily>.toFutureWeatherItems(context: Context): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it, context)
        }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            Toast.makeText(this@FutureListWeatherFragment.context, "Clicked!", Toast.LENGTH_SHORT).show()
        }
    }

}