package com.example.weathermobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastActivity : AppCompatActivity() {

   private lateinit var recyclerView: RecyclerView

    private val adapterData = listOf<Data>(
        Data(1645377925),
        Data(1645464325),
        Data(1645550725),
        Data(1645637125),
        Data(1645723525),
        Data(1645809925),
        Data(1645896325),
        Data(1645982725),
        Data(1646069125),
        Data(1646155525),
        Data(1646241925),
        Data(1646328325),
        Data(1646414725),
        Data(1646501125),
        Data(1646587525),
        Data(1646673925),


        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recyclerV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(adapterData)


        }
}