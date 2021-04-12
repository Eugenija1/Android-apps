package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.*

class ChartTools: ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(value.toLong())
        Log.d("CREATION", formatter.format(date))
        return formatter.format(date)
    }
}