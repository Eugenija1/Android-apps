package com.example.yauheniya_padbiarozskaya_sr_14_00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray

class ConverterActivity : AppCompatActivity() {
    internal lateinit var valueFirstCur: TextInputEditText
    internal lateinit var valueSecondCur: TextInputEditText
    internal lateinit var convToPLNButton: Button
    internal lateinit var spinner2: Spinner
    internal lateinit var convFromPLNButton: Button
    private var currenciesList = mutableListOf<Currency>()
    private var selectedCurrency2: Currency? = null
    internal lateinit var spinner: ProgressBar
    private var noConnection : Boolean = false
    private var conversionHappened : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        spinner = findViewById(R.id.progressBar3)
        spinner.setVisibility(View.GONE);

        valueFirstCur = findViewById(R.id.cur1)
        valueSecondCur = findViewById(R.id.cur2)
        convToPLNButton = findViewById(R.id.convertToBut)
        spinner2 = findViewById(R.id.spinner2)
        convFromPLNButton = findViewById(R.id.convertFromoBut)

        convFromPLNButton.setOnClickListener{convertFromPLN()}
        convToPLNButton.setOnClickListener{convertToPLN()}

        valueFirstCur.addTextChangedListener{input1Changed()}
        valueSecondCur.addTextChangedListener{input2Changed()}

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCurrency2 = currenciesList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        RequestQueue.prepare(this)

        makeRequest("A")
        makeRequest("B")
    }

    private fun input1Changed() {
        if(!conversionHappened && !valueSecondCur.isFocused)
            valueSecondCur.text?.clear()
    }

    private fun input2Changed() {
        if(!conversionHappened && !valueFirstCur.isFocused)
            valueFirstCur.text?.clear()
    }

    private fun convertFromPLN() {
        conversionHappened = true
        val firstCurrency = valueFirstCur.text.toString()
        if (!noConnection) {
            if (firstCurrency.isEmpty()){
                Toast.makeText(this,"Field 'PLN' is empty",Toast.LENGTH_SHORT).show()
            }
            else if (!inputIsCorrect(firstCurrency)){
                Toast.makeText(this, "Input is not numeric", Toast.LENGTH_SHORT).show()
            } else {
                val val1 = firstCurrency.toFloat()
                val val2 = selectedCurrency2?.curValue
                val res2 = String.format("%.2f", (val1 / val2!!))
                valueSecondCur.setText(res2)
            }
        }
        conversionHappened = false
    }

    private fun convertToPLN() {
        conversionHappened = true
        val secCurrency = valueSecondCur.text.toString()
        if (!noConnection) {
            if (secCurrency.isEmpty()) {
                Toast.makeText(this, "Field 'foreign currency' is empty", Toast.LENGTH_SHORT).show()
            }
            else if (!inputIsCorrect(secCurrency)){
                Toast.makeText(this, "Input is not numeric", Toast.LENGTH_SHORT).show()
            }
            else {
                val val1 = secCurrency.toFloat()
                val val2 = selectedCurrency2?.curValue
                val res2 = String.format("%.2f", (val1 * val2!!))
                valueFirstCur.setText(res2)
            }
        }
        conversionHappened = false
    }

    private fun inputIsCorrect(input: String): Boolean {
        return input.matches("\\d+(\\.\\d+)?".toRegex())
    }

    private fun makeRequest(table: String) {
        val queue = RequestQueue.queue
        val requestUrl = "http://api.nbp.pl/api/exchangerates/tables/%s?format=json".format(table)
        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                requestUrl,
                null,
                Response.Listener {response->
                    loadData(response)
                    valueFirstCur.isFocusable = true
                    valueSecondCur.isFocusable = false
                },
                Response.ErrorListener {
                    Log.d("CREATION","error")
                    spinner.setVisibility(View.VISIBLE);
                    noConnection = true
                    valueFirstCur.isFocusable = false
                    valueSecondCur.isFocusable = false
                    Toast.makeText(this,"Check your internet connection", Toast.LENGTH_SHORT).show()
                }
        )
        queue.add(jsonArrayRequest)
    }


    private fun loadData(response: JSONArray) {
        val curValues = response.getJSONObject(0).getJSONArray("rates")
        val table = response.getJSONObject(response.length() - 1).getString("table")

        try {
            val curValuesCount = curValues.length()

            for (i in 0 until curValuesCount) {
                val currencyCode = curValues.getJSONObject(i).getString("code")
                val curValue = curValues.getJSONObject(i).getDouble("mid")
                val curObject = Currency(currencyCode, curValue, table=table)
                currenciesList.add(curObject)
            }
            currenciesList.sortBy { currency -> currency.currencyName }
            val currenciesCodes = currenciesList.map{currency ->  currency.currencyName}
            spinner2.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                    currenciesCodes)
            if (spinner2.adapter.count > 0)
                spinner2.setSelection(0)
        } catch (ex: Exception){
            Toast.makeText(this,"Data didn't load properly", Toast.LENGTH_SHORT).show()
        }
    }

}

