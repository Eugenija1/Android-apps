package com.example.yauheniya_padbiarozskaya_sr_14_00

data class Currency(var currencyName: String, var curValue: Double, var curFlag: Int = 0, var table:String){
    val name = currencyName
    val value = curValue
    val flag = curFlag
    var rise = 0
    var tbl = table
}