// Yauheniya Padbiarozskaya
// wszystkie punkty zrobione
// wykresy pokazują nie ostatnie 30/7 kursów z tabeli ale kursy opublikowane
// w ostatnich 30/7 dniach, więc dla walut z tabeli B wykresy mogą mieć mniej
// punktów bo dane były publikowane kilka razy na miesiąc(tydzień)

package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    internal lateinit var ratesButton: Button
    internal lateinit var goldButton: Button
    internal lateinit var convertButton: Button
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ratesButton = findViewById(R.id.curRatesButton)
        ratesButton.setOnClickListener{toRatesAct()}
        goldButton = findViewById(R.id.goldRateButton)
        goldButton.setOnClickListener{toGoldAct()}
        convertButton = findViewById(R.id.currConverterButton)
        convertButton.setOnClickListener{toConvertAct()}
    }

    private fun toRatesAct() {
        val intent = Intent(context, RatesActivity::class.java)
        context.startActivity(intent)
    }

    private fun toGoldAct(){
        val intent = Intent(context, GoldActivity::class.java)
        context.startActivity(intent)
    }

    private fun toConvertAct(){
        val intent = Intent(context, ConverterActivity::class.java)
        context.startActivity(intent)
    }
}