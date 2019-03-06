package com.ahasanidea.weatherforecast.ui.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ahasanidea.weatherforecast.R
import com.ahasanidea.weatherforecast.data.network.PostWebApiService
import com.ahasanidea.weatherforecast.data.network.response.Post
import com.ahasanidea.weatherforecast.data.network.response.WeatherApiService
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)
        // TODO: Use the ViewModel
        //val apiService=WeatherApiService()
        val postWebApiService=PostWebApiService()
        GlobalScope.launch(Dispatchers.Main){
            //val response=apiService.getCurrentWeather("Dhaka").await()
            val response=postWebApiService.savePost("title").await()
            textView.text=response.toString()
        }
    }

}
