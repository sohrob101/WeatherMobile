package com.example.weathermobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage


class ForecastDetailsFragment : Fragment() {


    private val args: ForecastDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as MainActivity).supportActionBar?.title = "Forecast Details"
        val view = ComposeView(requireContext())
        view.apply{
            setContent{
                Column() {
                    AsyncImage(model = "https://openweathermap.org/img/wn/${args.dayForecast.weather[0].icon}@2x.png", contentDescription = null, modifier = Modifier.size(88.dp))
                    MessageCard(String.format("Day Temperature: %.0f°",args.dayForecast.temp.day))
                    MessageCard(String.format("Minimum Temperature: %.0f°",args.dayForecast.temp.min))
                    MessageCard(String.format("Maximum Temperature: %.0f°",args.dayForecast.temp.max))
                    MessageCard(String.format("Humidity: %d",args.dayForecast.humidity))
                    MessageCard(String.format("Pressure: %.0f",args.dayForecast.pressure))
                    MessageCard(String.format("Wind Speed: %.0f",args.dayForecast.speed))
                    MessageCard(String.format("Weather Description: %s",args.dayForecast.weather[0].description))

                }


            }
        }
        return view
    }


    @Composable
    fun MessageCard(name: String) {
        Text(text = name, fontSize = 30.sp)
    }


/*
    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun PreviewMessage(){
        Column() {
            MessageCard(String.format("Day Temperature:  %.0f°",50.50f))
            MessageCard(String.format("Min Temp: %.0f°",50.32f))
        }

    }
 */


}