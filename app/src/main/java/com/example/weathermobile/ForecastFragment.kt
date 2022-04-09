package com.example.weathermobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathermobile.databinding.CurrentConditionsFragmentBinding
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
        binding.recyclerV.adapter = state.forecast?.list?.let {MyAdapter(it) }
    }



 /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForecastFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this){ forecast ->
            bindData(forecast)
        }
        viewModel.loadData()
    }
*/

    private fun bindData(forecast: Forecast){


        binding.recyclerV.apply {
            binding.recyclerV.adapter = MyAdapter(forecast.list)
        }

    }


}