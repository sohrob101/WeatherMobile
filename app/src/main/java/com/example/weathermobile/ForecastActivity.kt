package com.example.weathermobile

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathermobile.databinding.ActivityForecastBinding
import com.example.weathermobile.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {



    private lateinit var binding: ActivityForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this){ forecast ->
            bindData(forecast)
        }
        viewModel.loadData()
    }


    private fun bindData(forecast: Forecast){


        binding.recyclerV.apply {
            binding.recyclerV.adapter = MyAdapter(forecast.list)
            binding.recyclerV.layoutManager = LinearLayoutManager(this@ForecastActivity)
        }

    }


}