package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.content.Context
import com.blongho.country_data.Country
import com.blongho.country_data.World

class FlagProvider(context: Context) {
    private var countries: List<Country>
    private val countryCodes: Map<String, Int> = mapOf("EUR" to R.drawable.eu,
        "GBP" to R.drawable.gb, "CHF" to R.drawable.ch, "HKD" to R.drawable.hk,
        "USD" to R.drawable.us, "IDR" to R.drawable.id, "WST" to R.drawable.ws, "MRU" to R.drawable.mr,
        "TMT" to R.drawable.tm, "ERN" to R.drawable.er, "MOP" to R.drawable.mo, "BYN" to R.drawable.by,
        "GHS" to R.drawable.gh, "ZWM" to R.drawable.zm, "GIP" to R.drawable.gi, "ZWL" to R.drawable.zw,
        "XPF" to R.drawable.pf, "XDR" to R.drawable.xdr, "STN" to R.drawable.st, "ZMW" to R.drawable.zm
    )

    init {
        World.init(context)
        countries = World.getAllCountries().distinctBy { it.currency.code }
    }

    fun getFlag(countryCode: String): Int {
        return if (countryCodes.containsKey(countryCode))
            countryCodes.getValue(countryCode)
        else
            countries.find { it.currency.code == countryCode }?.flagResource ?: World.getWorldFlag()
    }
}