package com.example.weathermobile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weathermobile.databinding.CurrentConditionsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.current_conditions_fragment)
{

    private lateinit var binding: CurrentConditionsFragmentBinding
    private val args: CurrentConditionsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: CurrentConditionsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Current Conditions"

        binding = CurrentConditionsFragmentBinding.bind(view)

        binding.forestBtn.setOnClickListener {
            viewModel.forecastBtnClicked()
        }

        viewModel.onViewCreated(args.currentConditions)

        viewModel.navigateToForecast.observe(viewLifecycleOwner){
            it?.let { coordinates -> navigateToForecast(coordinates) }
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            bindView(it)
        }

    }


    private fun bindView(state: CurrentConditionsViewModel.State) {
        binding.temperature.text = context?.getString(
            R.string.current_temp,
            state.currentConditions?.main?.temp
        )
        binding.cityName.text = state.currentConditions?.name
        binding.feelsLike.text = context?.getString(
            R.string.feels_like,
            state.currentConditions?.main?.feelsLike
        )
        binding.info.text = context?.getString(
            R.string.info,
            state.currentConditions?.main?.tempMin,
            state.currentConditions?.main?.tempMax,
            state.currentConditions?.main?.humidity,
            state.currentConditions?.main?.pressure
        )

        val icon = state.currentConditions?.weather?.firstOrNull()?.icon
        icon?.let {
            Glide
                .with(binding.imageView)
                .load("https://openweathermap.org/img/wn/$it@2x.png")
                .into(binding.imageView)
        }
    }



    private fun navigateToForecast(coordinates: Coordinates){
        val action = CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(coordinates)
        findNavController().navigate(action)
    }






}