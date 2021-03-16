// Yauheniya Padbiarozskaya
// których puntów brakuje:
// 4), 7), 9)
package com.example.yauheniya_padbiarozskaya_sr_14_00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    internal lateinit var clearButton: Button
    internal lateinit var equalsButton: Button
    internal lateinit var floatNumButton: Button
    internal lateinit var display: TextView

    internal lateinit var digits: Array<Button>
    internal lateinit var operators: Array<Button>

    private var noEvaluations = true
    private var lastDot = false
    private var hasOneDot = false
    private var lastDigit = true
    private var lastOperation = false
    var displayBuilder = StringBuilder()
    var lastNum = StringBuilder()

    private var lastNumber = ""

    private fun setDisplay(i: String) {
        displayBuilder.append(i)
        display.text = displayBuilder.toString()
    }

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearButton = findViewById(R.id.buttonClear)
        floatNumButton = findViewById(R.id.buttonDot)
        equalsButton = findViewById(R.id.buttonEquals)
        display = findViewById(R.id.textOnDisplay)
        display.isSelected = true;

        val digitsId = arrayOf(R.id.button0, R.id.button1, R.id.button2,
                R.id.button3, R.id.button4, R.id.button5,
                R.id.button6, R.id.button7, R.id.button8, R.id.button9)
        val operatorsId = arrayOf(R.id.buttonDiv, R.id.buttonMult, R.id.buttonMinus,
                R.id.buttonPlus, R.id.ButtonSqrt, R.id.buttonPercent)

        digits = (digitsId.map { id -> findViewById<Button>(id) }).toTypedArray()
        operators = (operatorsId.map { id -> findViewById<Button>(id) }).toTypedArray()

        clearButton.setOnClickListener { clearDisplay() }
        floatNumButton.setOnClickListener { floatNumbers() }
        equalsButton.setOnClickListener { evaluate() }
        digits.forEach { button -> button.setOnClickListener { i -> digitPressed(i as Button) } }
        operators.forEach { button ->
            button.setOnClickListener { i -> operatorPressed(i as Button) }
        }
    }

    private val calculator= Calculator()

    @ExperimentalStdlibApi
    private fun operatorPressed(button: Button) {
        if (lastDigit){
            lastNumber = lastNum.toString()
            calculator.addNumber(lastNumber)
            lastNum.clear()
        }
        if (lastOperation)
            displayBuilder.deleteAt(displayBuilder.length -1 )
        setDisplay(button.text as String)
        calculator.addOperator(button.text as String)
        lastOperation = true
        lastDot = false
        lastDigit = false
        hasOneDot = false
    }

    @ExperimentalStdlibApi
    private fun digitPressed(button: Button) {
        lastDot = false
        lastDigit = true
        lastOperation = false
        if (canTypeZero(button)){
            lastNum.append(button.text as String)
            setDisplay(button.text as String)
        }

    }

    @ExperimentalStdlibApi
    private fun canTypeZero(button: Button) : Boolean {
        if (displayBuilder.length == 1){
            if (displayBuilder.last().toString() == "0") {
                return if (button.text == "0")
                    false
                else{
                    displayBuilder.deleteAt(displayBuilder.length-1)
                    true
                }
            }
        } else if (displayBuilder.length > 1){
            if (displayBuilder.last().toString() == "0" &&
                    isOperator(displayBuilder.get(displayBuilder.length-2).toString()) &&
                    button.text.toString() == "0")
                return false
        } else {
            if (display.text == "0" && button.text == "0")
                return false
        }
        return true
    }

    private fun isOperator(sign: String): Boolean {
        val operatorSigns = (operators.map { id -> id.text }).toTypedArray()
        for (d in operatorSigns) {
            if (sign == d)
                return true
        }
        return false
    }

    @ExperimentalStdlibApi
    private fun evaluate() {
        if (lastDigit || displayBuilder.last().toString() == "%"){
            calculator.addNumber(lastNum.toString())
            lastNum.clear()
            val result = calculator.evaluate()
            display.text = result.toString()
            calculator.addNumber(result.toString())
        }
        displayBuilder.clear()
        setDisplay(display.text as String)
        lastDigit = false
        lastDot = false
        hasOneDot = false
    }

    private fun floatNumbers() {
        if (display.text == "0" && !lastDot && !hasOneDot){
            lastNum.append("0.")
            setDisplay("0.")
            lastDot = true
            hasOneDot = true
        }
        else if (lastDigit && !lastDot && !hasOneDot){
            lastNum.append(".")
            setDisplay(".")
            lastDot = true
            hasOneDot = true
        }
    }

    private fun clearDisplay() {
        lastDigit = false
        lastDot = false
        hasOneDot = false
        lastOperation = false
        displayBuilder.clear()
        display.text = "0"
        calculator.clearRes()
    }
}
