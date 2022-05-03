package com.example.weathermobile

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathermobile.databinding.ForecastFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.forecast_fragment) {



    private lateinit var binding: ForecastFragmentBinding


    @Inject
    lateinit var viewModel: ForecastViewModel
    private val args: ForecastFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Forecast"
        binding = ForecastFragmentBinding.bind(view)

        binding.recyclerV.layoutManager = LinearLayoutManager(requireContext())


        viewModel.forecast.observe(viewLifecycleOwner){
            bindView(it)
        }

        viewModel.onViewCreated(args.coordinates)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

    }

    private fun bindView(state: ForecastViewModel.State){
        val adapter = state.forecast?.list?.let {MyAdapter(it) }
        binding.recyclerV.adapter = adapter
        adapter?.setOnItemClickListener(object : MyAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val someVariableName = state.forecast.list[position]
                val action = ForecastFragmentDirections.actionForecastFragmentToForecastDetailsFragment(someVariableName)
                findNavController().navigate(action)

            }

        })
    }



/*
    private fun navigateToForecast(coordinates: Coordinates){
        //val action = CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(coordinates)
        //findNavController().navigate(action)
    }

 */





}