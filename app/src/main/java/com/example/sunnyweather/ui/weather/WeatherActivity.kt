package com.example.sunnyweather.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity:AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        if(viewModel.locationLng.isEmpty())
            viewModel.locationLng = intent.getStringExtra("location_lng")?:""
        if(viewModel.locationLat.isEmpty())
            viewModel.locationLat = intent.getStringExtra("location_lat")?:""
        if(viewModel.placeName.isEmpty())
            viewModel.placeName = intent.getStringExtra("placeName")?:""
        viewModel.weatherLiveData.observe(this, { result ->
            val weather = result.getOrNull()
            if(weather != null)
                showWeatherInfo(weather)
            else {
                Toast.makeText(this,"无法获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    @SuppressLint("WrongViewCast")
    private fun showWeatherInfo(weather: Weather) {
        val placeName = this.findViewById<TextView>(R.id.placeName)
        val currentTemp = this.findViewById<TextView>(R.id.currentTemp)
        val currentSky = this.findViewById<TextView>(R.id.currentSky)
        val currentAQI = this.findViewById<TextView>(R.id.currentAQI)
        val nowLayout = this.findViewById<RelativeLayout>(R.id.nowLayout)
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充now.xml布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        val forecastLayout = this.findViewById<MaterialCardView>(R.id.forecastLayout)
        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for(i in 0 until days)
        {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)
            val dateInfo = this.findViewById(R.id.dateInfo) as TextView
            val skyIcon = this.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = this.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = this.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        val coldRiskText = findViewById<TextView>(R.id.coldRiskText)
        val dressingText = findViewById<TextView>(R.id.dressingText)
        val ultravioletText = findViewById<TextView>(R.id.ultravioletText)
        val carWashingText = findViewById<TextView>(R.id.carWashingText)
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        val weatherLayout = findViewById<ScrollView>(R.id.weatherLayout)
        weatherLayout.visibility = View.VISIBLE
    }
}