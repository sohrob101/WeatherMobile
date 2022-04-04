package com.example.weathermobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.weathermobile.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.forestBtn.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)

        }

        Log.d("TAG","onCreate")
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this){ currentConditions ->
            bindData(currentConditions)
        }
        viewModel.loadData()
    }


    private fun bindData(currentConditions: CurrentConditions){

        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(R.string.current_temp, currentConditions.main.temp.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.imageView)
        binding.feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        info.text = getString(R.string.info, currentConditions.main.tempMin.toInt(), currentConditions.main.tempMax.toInt(), currentConditions.main.humidity.toInt(), currentConditions.main.pressure.toInt())

    }
}



