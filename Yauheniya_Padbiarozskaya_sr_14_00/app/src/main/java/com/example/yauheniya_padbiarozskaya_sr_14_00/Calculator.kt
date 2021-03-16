package com.example.yauheniya_padbiarozskaya_sr_14_00

import java.math.BigDecimal

class Calculator : CalculatorInt {
    var numbers = mutableListOf<String>()
    var operators = mutableListOf<String>()

    override fun addNumber(num : String){
        numbers.add(num)
    }


    override fun addOperator(op: String){
        operators.add(op)
    }

    fun clearRes(){
        numbers.clear()
        operators.clear()
    }

    fun substr(num1 : Double, num2 : Double) : Double{
        val numBD1 = BigDecimal(num1.toString())
        val numBD2 = BigDecimal(num2.toString())
        val bigRes = numBD1.subtract(numBD2)
        return bigRes.toDouble()
    }

    @ExperimentalStdlibApi
    override fun evaluate(): Double {
        val op = operators.removeLast()
        var num2 = 0.0
        var num1 = 0.0
        if (op == "√"){
            num2 = numbers.removeLast().toDouble()
        } else if (op == "%"){
            num1 = numbers.elementAt(numbers.size - 2).toDouble()
        } else {
            num2 = numbers.removeLast().toDouble()
            num1 = numbers.removeLast().toDouble()
        }
        return when (op) {
            "+" -> num1 + num2
            "-" -> substr(num1, num2)
            "*" -> num1 * num2
            "×" -> num1 * num2
            "/" -> num1 / num2
            "÷" -> num1 / num2
            "√" -> kotlin.math.sqrt(num2)
            "%" -> num1 / 100
            else -> {
                0.0
            }
        }
    }
}
