package com.example.weathermobile

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ForecastActivity : AppCompatActivity() {


    //private val adapterTemp = listOf<ForecastTemp>(
      //  ForecastTemp(72.0F,65.0F,80.0F)
    //)

    private val apiKey = "b55cf5a5fd3ecf277058fb06bb0ce80b"
    private lateinit var api: Api
    private lateinit var conditionIcon: ImageView
    private lateinit var date: TextView
    private lateinit var temp: TextView
    private lateinit var sunRise: TextView
    private lateinit var sunSet: TextView
    private lateinit var recyclerView: RecyclerView


    private lateinit var adapterData: List<DayForecast>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recyclerV)


        date = findViewById(R.id.date)
        temp = findViewById(R.id.temp)
        sunRise = findViewById(R.id.sunrise)
        sunSet = findViewById(R.id.sunset)
        conditionIcon = findViewById(R.id.imageView5)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/forecast/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

    }

    override fun onResume() {
        super.onResume()
        val call : Call<Forecast> = api.getForecast("55439")
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>

            ) {
                val forecast = response.body()

                forecast?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun bindData(forecast: Forecast){



        val dateOne = forecast.list[0].date
        val sunsetOne = forecast.list[0].sunset
        val sunriseOne = forecast.list[0].sunrise
        val tempOne = forecast.list[0].temp
        val pressureOne = forecast.list[0].pressure
        val humidityOne = forecast.list[0].humidity





        //adapterData = listOf<DayForecast>()

        //DayForecast(dateOne, sunriseOne, sunsetOne, tempOne, pressureOne, humidityOne)


        //DayForecast(1645377925,1645377925,1645378921,adapterTemp.elementAt(0),1024F,70),

        recyclerView.apply {
            recyclerView.adapter = MyAdapter(forecast.list)
            recyclerView.layoutManager = LinearLayoutManager(this@ForecastActivity)
        }

    }


}