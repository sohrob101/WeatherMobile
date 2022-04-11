package com.example.weathermobile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weathermobile.databinding.SearchFragmentBinding
import com.example.weathermobile.SearchViewModel.Event.NavigateToCurrentConditions
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var viewModel: SearchViewModel


    private lateinit var binding: SearchFragmentBinding
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var locationProvider: FusedLocationProviderClient



    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Search"
        binding = SearchFragmentBinding.bind(view)



        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    requestLocationUpdates()
                }
                else -> {

                }
            }
        }

        binding.currentLocationButton.setOnClickListener {
            requestLocation()
            requestLocationUpdates()
        }



        binding.searchButton.setOnClickListener { viewModel.searchButtonClicked() }


        binding.zipCodeEntry.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.updateSearchText(p0.toString())
            }
        })

        viewModel.onViewCreated()

        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is NavigateToCurrentConditions -> navigateToCurrentConditions(it)
                SearchViewModel.Event.SearchError -> handleSearchError()
                SearchViewModel.Event.ViewCreated -> { /* no-op */ }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { bindView(it) }
    } //End of onViewCreated Function

    override fun onResume() {
        super.onResume()
        requestLocationUpdates()
    }


    private fun requestLocation() {
        if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == true) {
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Request")
                    .setTitle("Rationale")
                    .setNeutralButton("Ok") { _, _ ->
                        locationPermissionRequest.launch(
                            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
                        )
                    }
                    .show()
            }
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun requestLocationUpdates() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProvider = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        locationProvider.lastLocation
            .addOnSuccessListener {
                requestNewLocation()
            }
    }

    fun requestNewLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 0L
        locationRequest.fastestInterval = 0L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationProvider = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach {
                    viewModel.locationButtonClicked(it.latitude, it.longitude)
                }
            }
        }
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProvider?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }




    private fun navigateToCurrentConditions(navigateToCurrentConditions: NavigateToCurrentConditions) {
        val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
            navigateToCurrentConditions.currentConditions
        )
        findNavController().navigate(action)
    }

    private fun bindView(state: SearchViewModel.State) {
        binding.searchButton.isEnabled = state.searchButtonEnabled
    }

    private fun handleSearchError() {
        SearchError()
            .show(childFragmentManager, SearchError.TAG)
    }
}