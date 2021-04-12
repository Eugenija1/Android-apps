package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray


class RatesActivity : AppCompatActivity() {
    internal lateinit var currenciesListRecyclerView: RecyclerView
    internal lateinit var adapter: CurrenciesListAdapter
    internal var currenciesList = mutableListOf<Currency>()
    private lateinit var spinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)

        spinner = findViewById(R.id.progressBar2)
        spinner.setVisibility(View.GONE);


        currenciesListRecyclerView = findViewById(R.id.currencyList)
        currenciesListRecyclerView.layoutManager= LinearLayoutManager(this)
        adapter = CurrenciesListAdapter(currenciesList, this)
        currenciesListRecyclerView.adapter = adapter

        RequestQueue.prepare(this)
        makeRequest("A")
        makeRequest("B")
    }

    private fun makeRequest(table: String) {
        val queue = RequestQueue.queue
        val requestUrl = "http://api.nbp.pl/api/exchangerates/tables/%s/last/2?format=json"
            .format(table)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            requestUrl,
            null,
            Response.Listener {
                    response -> Log.d("CREATION","Success!")
                loadData(response)
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                Log.d("CREATION","error")
                spinner.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Check your internet connection", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun loadData(response: JSONArray?){
        response?.let {
            val ratesNew = response.getJSONObject(response.length() - 1).getJSONArray("rates")
            val ratesOld = response.getJSONObject(response.length() - 2).getJSONArray("rates")
            val table = response.getJSONObject(response.length() - 1).getString("table")
            val curValues = ratesNew
            val curValuesCount = curValues.length()
            val flagProvider = FlagProvider(applicationContext)

            for (i in 0 until curValuesCount) {
                val currencyCode = curValues.getJSONObject(i).getString("code")
                val curValue = curValues.getJSONObject(i).getDouble("mid")
                val curFlag = flagProvider.getFlag(currencyCode)

                val curObject = Currency(currencyCode, curValue, curFlag, table)
                adapter.dataSet.add(curObject)

                val rateOld = ratesOld.getJSONObject(i).getDouble("mid")
                curObject.rise = if (curValue > rateOld) 1 else if (curValue < rateOld) 2 else 0
            }
            adapter.dataSet.sortBy { currency -> currency.currencyName }
        }
    }
}