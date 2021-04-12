package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import kotlin.collections.ArrayList

class CurDetailsActivity : AppCompatActivity() {
    internal lateinit var todayRate: TextView
    internal lateinit var yestRate: TextView
    internal lateinit var description1: TextView
    internal lateinit var description2: TextView
    internal lateinit var chart1: LineChart
    internal lateinit var chart2: LineChart
    internal lateinit var curCode: String
    internal lateinit var table: String
    private var data = mutableListOf<Entry>()
    private var data2 = mutableListOf<Entry>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cur_details)
        curCode = intent.getStringExtra("currencyCode").toString()
        table = intent.getStringExtra("table").toString()
        chart1 = findViewById(R.id.chart1)
        chart2 = findViewById(R.id.chartGold)
        todayRate = findViewById(R.id.currentRate)
        yestRate = findViewById(R.id.yesterdayRate)
        description1 = findViewById(R.id.description1)
        description1.text = getString(R.string.description1, curCode)
        description2 = findViewById(R.id.description2)
        description2.text = getString(R.string.description2, curCode)
        getHistRates(table)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHistRates(table: String){
        val queue = RequestQueue.queue
        val requestUrl = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30?format=json"
            .format(table, curCode)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            requestUrl,
            null,
            Response.Listener {
                    response -> Log.d("CREATION","Success!")
                loadHistData(response)
            },
            Response.ErrorListener {
                Log.d("CREATION","************************error******************************")
            }
        )
        queue.add(jsonObjectRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistData(response: JSONObject?){
        response?.let {
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val days30ago = LocalDate.now().minusDays( 30 )
            val days7ago = LocalDate.now().minusDays( 7 )
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            for (i in 0 until ratesCount) {
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val rate = rates.getJSONObject(i).getDouble("mid")

                var dateParsed = LocalDate.parse(date, formatter)
                if(days30ago <= dateParsed)
                    data.add(Entry(
                            LocalDate.parse(date).getLong(ChronoField.EPOCH_DAY).toFloat() * 86400 * 1000,
                            rate.toFloat()))
                if(days7ago <= dateParsed)
                    data2.add(Entry(
                            LocalDate.parse(date).getLong(ChronoField.EPOCH_DAY).toFloat() * 86400 * 1000,
                            rate.toFloat()))
            }
            val df = DecimalFormat("0.####")
            todayRate.text = getString(R.string.current_rate, df.format(data.last().y))
            yestRate.text = getString(R.string.yesterday_rate, df.format(data.reversed()[1].y))

            val chartData = LineData(LineDataSet(data.toList(), "Rates"))
            chart1.description.isEnabled = false
            chart1.xAxis.valueFormatter = ChartTools()
            chart1.data = chartData
            chart1.invalidate()

            val chartData2 = LineData(LineDataSet(data2, "Rates"))
            chart2.description.isEnabled = false
            chart2.xAxis.valueFormatter = ChartTools()
            chart2.data = chartData2
            chart2.invalidate()

        }
    }
}