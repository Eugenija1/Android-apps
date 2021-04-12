package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue


object RequestQueue {
    lateinit var queue: RequestQueue

    fun prepare(context: Context){
        queue = newRequestQueue(context)
    }
}