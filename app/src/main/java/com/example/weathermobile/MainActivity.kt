package com.example.weathermobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        forestBtn.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }


        Log.d("TAG","onCreate")
    }
}



