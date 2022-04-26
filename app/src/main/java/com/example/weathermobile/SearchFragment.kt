package com.example.weathermobile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weathermobile.databinding.SearchFragmentBinding
import com.example.weathermobile.SearchViewModel.Event.NavigateToCurrentConditions
import com.google.android.gms.location.*
import com.squareup.moshi.internal.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var viewModel: SearchViewModel

    private val notificationId = 101


    private lateinit var binding: SearchFragmentBinding
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var locationProvider: FusedLocationProviderClient
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    private lateinit var lastLocation: String
    private lateinit var coordinates: Coordinates



    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Search"
        binding = SearchFragmentBinding.bind(view)
        createNotificationChannel()




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
        serviceIntent = Intent(activity?.applicationContext, NotificationTimerService::class.java)

        binding.notification.setOnClickListener {
            if(binding.notification.text == "Turn on notifications"){
                binding.notification.setText("Turn off notifications")
                resetTimer()
                startStopTimer()
                testRemoval()
                activity?.startForegroundService(serviceIntent)
            }
            else{
                binding.notification.setText("Turn on notifications")
                startStopTimer()
                resetTimer()

            }

        }


        requireActivity().registerReceiver(updateTime, IntentFilter(NotificationTimerService.TIMER_UPDATED))




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


    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(NotificationTimerService.TIME_EXTRA, 0.0)
            //if the timer hits 1:00; then the sendNotification() function will run again.
            if(time >= 30.0){
                requestLocationUpdates()
                resetNotificationTimer()
                startTimer()
            }


        }

    }


    override fun onResume() {
        super.onResume()
        requestLocationUpdates()
    }

    private fun resetTimer()
    {
        stopTimer()
        time = 0.0
    }

    private fun startStopTimer()
    {
        if(timeStarted)
            stopTimer()
        else
            startTimer()

    }

    private fun startTimer() {
        serviceIntent.putExtra(NotificationTimerService.TIME_EXTRA, time)
        //getActivity()?.startService(serviceIntent)
        timeStarted = true
    }

    private fun testRemoval(){
        serviceIntent.putExtra("test", lastLocation)
        Log.d("debug","debug")
    }

    private fun stopTimer() {
        getActivity()?.stopService(serviceIntent)
        timeStarted = false

    }

    private fun resetNotificationTimer()
    {
        stopNotificationTimer()
        time = 0.0
    }

    private fun stopNotificationTimer() {
        //getActivity()?.stopService(serviceIntent)
        timeStarted = false

    }


    private val CHANNEL_ID = "0"

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val descriptionText = "THis is a notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system

            

            val notificationManager: NotificationManager =
                getActivity()?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }




/*
    private fun sendNotification() {
        val builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.sun)
                .setContentTitle("Enable Notifications?")
                .setContentText("Would you like to enable notifications?")
                //.setStyle(
                //  NotificationCompat.BigTextStyle().bigText("HEY LOOK AT ME THIS IS " +
                //        "SUPPOSED TO BE A LOT OF TEXT LOREM IPSUM"))
                //.setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        }

        if (builder != null) {
            with(activity?.let { NotificationManagerCompat.from(it) }){
                this?.notify(notificationId, builder.build())
            }
        }

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(serviceIntent)
        }

         */

    }

 */







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
        lastLocation = locationProvider.lastLocation.toString()
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