package com.example.weathermobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {


    private val apiKey = "b55cf5a5fd3ecf277058fb06bb0ce80b"
    private lateinit var api: Api
    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var conditionIcon: ImageView
    private lateinit var feelsLike: TextView
    private lateinit var tempInfo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityName = findViewById(R.id.city_name)
        currentTemp = findViewById(R.id.temperature)
        conditionIcon = findViewById(R.id.imageView)
        feelsLike = findViewById(R.id.feels_like)
        tempInfo = findViewById(R.id.info)



        forestBtn.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)

        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)



        Log.d("TAG","onCreate")
    }

    override fun onResume() {
        super.onResume()
        val call : Call<CurrentConditions> = api.getCurrentConditions("55439")
        call.enqueue(object : Callback<CurrentConditions>{
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentConditions = response.body()
                currentConditions?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun bindData(currentConditions: CurrentConditions){
        cityName.text = currentConditions.name
        currentTemp.text = getString(R.string.current_temp, currentConditions.main.temp.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(conditionIcon)
        feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        info.text = getString(R.string.info, currentConditions.main.tempMin.toInt(), currentConditions.main.tempMax.toInt(), currentConditions.main.humidity.toInt(), currentConditions.main.pressure.toInt())

    }
}



