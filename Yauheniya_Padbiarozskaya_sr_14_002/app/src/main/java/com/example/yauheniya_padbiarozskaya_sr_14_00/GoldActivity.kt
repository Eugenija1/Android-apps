package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import org.json.JSONArray
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.*
import kotlin.collections.ArrayList


class GoldActivity : AppCompatActivity() {
    internal lateinit var goldRate: TextView
    internal lateinit var goldDesc: TextView
    internal lateinit var chart: LineChart
    private lateinit var spinner: ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)

        spinner = findViewById(R.id.progressBar2)
        spinner.setVisibility(View.GONE);

        goldDesc = findViewById(R.id.descrGold)!!
        goldRate = findViewById(R.id.goldRate)
        chart = findViewById(R.id.chartGold)
        RequestQueue.prepare(this)
        makeRequest()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeRequest() {
        val queue = RequestQueue.queue
        val requestUrl = "http://api.nbp.pl/api/cenyzlota/last/30"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            requestUrl,
            null,
            Response.Listener {
                    response -> loadData(response)
            },
            Response.ErrorListener {
                Log.d("CREATION","error")
                spinner.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Check your internet connection", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonArrayRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(response: JSONArray?) {
        response?.let {
            val curRate = response.getJSONObject(response.length() - 1)
                .getDouble("cena")

            val entries = ArrayList<Entry>()

            for (i in 0 until response.length()) {
                val currentObject = response.getJSONObject(i)
                val date = currentObject.getString("data")
                val price = currentObject.getDouble("cena")
                entries.add(Entry(
                    LocalDate.parse(date).getLong(ChronoField.EPOCH_DAY).toFloat() * 86400 * 1000,
                    price.toFloat()))
            }

            goldRate.text = getString(R.string.goldRate, String.format(Locale.CANADA,"%.2f", curRate))
            val chartData = LineData(LineDataSet(entries, "Rates"))

            chart.description.isEnabled = false
            chart.xAxis.valueFormatter = ChartTools()
            chart.data = chartData
            chart.invalidate()
        }
    }

}
