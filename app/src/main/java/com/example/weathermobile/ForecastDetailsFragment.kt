package com.example.weathermobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment


class ForecastDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply{
            setContent{
                Text("Hey look a composable")
                MessageCard("Testing")
            }
        }
        return view
    }


    @Composable
    fun MessageCard(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun PreviewMessage(){
        MessageCard("Testing")
    }
}