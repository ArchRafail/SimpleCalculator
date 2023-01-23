package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private var textResult : String = ""
    private lateinit var textScreen : TextView
    private var number : Float = 0.0f
    private var operation : Char = ' '

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textScreen = findViewById(R.id.screen_result)

        val button0 : TextView = findViewById(R.id.button_0)
        val button1 : TextView = findViewById(R.id.button_1)
        val button2 : TextView = findViewById(R.id.button_2)
        val button3 : TextView = findViewById(R.id.button_3)
        val button4 : TextView = findViewById(R.id.button_4)
        val button5 : TextView = findViewById(R.id.button_5)
        val button6 : TextView = findViewById(R.id.button_6)
        val button7 : TextView = findViewById(R.id.button_7)
        val button8 : TextView = findViewById(R.id.button_8)
        val button9 : TextView = findViewById(R.id.button_9)

        button0.setOnClickListener(clickListener)
        button1.setOnClickListener(clickListener)
        button2.setOnClickListener(clickListener)
        button3.setOnClickListener(clickListener)
        button4.setOnClickListener(clickListener)
        button5.setOnClickListener(clickListener)
        button6.setOnClickListener(clickListener)
        button7.setOnClickListener(clickListener)
        button8.setOnClickListener(clickListener)
        button9.setOnClickListener(clickListener)

        val buttonC : TextView = findViewById(R.id.button_c)
        buttonC.setOnClickListener {
            textResult = ""
            textScreen.text = textResult
            operation = ' '
            number = 0.0f
        }

        val buttonPoint : TextView = findViewById(R.id.button_point)
        buttonPoint.setOnClickListener {
            if (textResult.isEmpty()) {
                textResult = "0."
            } else if (!textResult.contains(".")) {
                textResult += "."
            }
            if(operation != ' ' && !textResult.substring(textResult.indexOf(operation)).contains(".")) {
                textResult += if (textResult.substring(textResult.indexOf(operation)+1).isEmpty()) {
                    "0."
                } else {
                    "."
                }
            }
            textScreen.text = textResult
        }

        val buttonAddition : TextView = findViewById(R.id.button_addition)
        buttonAddition.setOnClickListener {
            val operation = '+'
            if (this.operation == ' ') {
                simpleConcatOperation(operation)
            } else {
                concatOperation(operation)
            }
        }

        val buttonSubtraction : TextView = findViewById(R.id.button_subtraction)
        buttonSubtraction.setOnClickListener {
            val operation = '-'
            if (this.operation == ' ') {
                simpleConcatOperation(operation)
            } else {
                concatOperation(operation)
            }
        }

        val buttonMultiply : TextView = findViewById(R.id.button_multiply)
        buttonMultiply.setOnClickListener {
            val operation = 'x'
            if (this.operation == ' ') {
                simpleConcatOperation(operation)
            } else {
                concatOperation(operation)
            }
        }

        val buttonDivide : TextView = findViewById(R.id.button_divide)
        buttonDivide.setOnClickListener {
            val operation = '/'
            if (this.operation == ' ') {
                simpleConcatOperation(operation)
            } else {
                concatOperation(operation)
            }
        }

        val buttonPercent : TextView = findViewById(R.id.button_percent)
        buttonPercent.setOnClickListener {
            val operation = '%'
            if (this.operation == ' ') {
                simpleConcatOperation(operation)
            } else {
                concatOperation(operation)
            }
        }

        val buttonResult : TextView = findViewById(R.id.button_result)
        buttonResult.setOnClickListener(resultClickListener)

        val buttonSign : TextView = findViewById(R.id.button_sign)
        buttonSign.setOnClickListener {
            if (textResult.isNotEmpty()) {
                val string: String = textResult
                textResult = if (string[0] == '-')  string.substring(1) else "-$string"
            }
            textScreen.text = textResult
        }
    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->

        val number = (view as TextView).text
        textResult += number
        textScreen.text = textResult
    }

    private val resultClickListener: View.OnClickListener = View.OnClickListener {

        var result = 0.0f
        if (textResult[0] != number.toString()[0])
            number *= -1
        if (textResult.substring(textResult.indexOf(operation)).isNotEmpty()) {
            val tempNumber = textResult.substring(textResult.indexOf(operation)+1).toFloat()
            result = when (this.operation) {
                '+' -> number +  tempNumber
                '-' -> number - tempNumber
                'x' -> number * tempNumber
                '/' -> number / tempNumber
                '%' -> number * tempNumber / 100
                else -> textResult.toFloat()
            }
        }
        textResult = if (result.isInfinite() || result.isNaN()) {
            R.string.error_info.toString()
        } else if (result % 1.0 == 0.0) {
            String.format("%.0f", result)
        } else {
            roundOffDecimal(result).toString().replace(',', '.')
        }
        number = result
        textScreen.text = textResult
        operation = ' '
    }

    private fun checkNumber() {
        if (textResult.isNotEmpty()) {
            number = textResult.toFloat()
        }
    }

    private fun simpleConcatOperation(operation: Char) {
        checkNumber()
        this.operation = operation
        textResult += operation
        textScreen.text = textResult
    }

    private fun concatOperation(operation: Char) {
        val regex= "[+x/%-]"
        val pattern: Pattern = Pattern.compile(regex)
        val match: Matcher = pattern.matcher(textResult)
        while (match.find()) {
            if (textResult.lastIndex != match.start()) {
                findViewById<TextView>(R.id.button_result).performClick()
            } else if (textResult.lastIndex == match.start()) {
                textResult = textResult.substring(0, textResult.length-1)
            }
        }
        this.operation = operation
        textResult += this.operation
        textScreen.text = textResult
    }

    private fun roundOffDecimal(number: Float): Float {
        val df = DecimalFormat("#.#####")
        val dfs = DecimalFormatSymbols()
        dfs.decimalSeparator = '.'
        df.decimalFormatSymbols = dfs
        df.isDecimalSeparatorAlwaysShown = false
        return df.format(number).toFloat()
    }
}