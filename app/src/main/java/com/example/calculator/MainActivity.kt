package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var result: TextView

    lateinit var buttonEqual: Button
    lateinit var buttonAdd: Button
    lateinit var buttonSub: Button
    lateinit var buttonMul: Button
    lateinit var buttonDev: Button
    lateinit var buttonClr: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)

        buttonEqual = findViewById(R.id.equal)
        buttonAdd = findViewById(R.id.add)
        buttonSub = findViewById(R.id.sub)
        buttonMul = findViewById(R.id.mul)
        buttonDev = findViewById(R.id.dev)
        buttonClr = findViewById(R.id.clr)

        val buttons = listOf(
            findViewById<Button>(R.id.zero) to "0",
            findViewById<Button>(R.id.one) to "1",
            findViewById<Button>(R.id.two) to "2",
            findViewById<Button>(R.id.three) to "3",
            findViewById<Button>(R.id.four) to "4",
            findViewById<Button>(R.id.five) to "5",
            findViewById<Button>(R.id.six) to "6",
            findViewById<Button>(R.id.seven) to "7",
            findViewById<Button>(R.id.eight) to "8",
            findViewById<Button>(R.id.nine) to "9"
        )

        buttons.forEach { (btn, value) ->
            btn.setOnClickListener { writeNextElementToTextView(value) }
        }

        buttonClr.setOnClickListener { clearResultTextView() }
        buttonAdd.setOnClickListener { writeNextElementToTextView("+") }
        buttonSub.setOnClickListener { writeNextElementToTextView("-") }
        buttonMul.setOnClickListener { writeNextElementToTextView("*") }
        buttonDev.setOnClickListener { writeNextElementToTextView("/") }
        buttonEqual.setOnClickListener { evaluate() }
    }

    private fun writeNextElementToTextView(element: String) {
        if (result.text.length <= 12) {
            result.text = result.text.toString() + element
        }
    }

    private fun clearResultTextView() {
        result.text = ""
    }

    private fun evaluate() {
        val expression = result.text.toString()
        try {
            val finalResult = evaluateExpression(expression)
            result.text = finalResult.toString()
        } catch (e: Exception) {
            result.text = "Error"
        }
    }

    fun evaluateExpression(expression: String): Double {
        val numbers = mutableListOf<Double>()
        val operations = mutableListOf<Char>()

        var num = ""
        for (char in expression) {
            if (char.isDigit() || char == '.') {
                num += char
            } else if (char in listOf('+', '-', '*', '/')) {
                numbers.add(num.toDouble())
                operations.add(char)
                num = ""
            }
        }
        numbers.add(num.toDouble())

        while (operations.contains('*') || operations.contains('/')) {
            val i = operations.indexOfFirst { it == '*' || it == '/' }
            val op = operations[i]
            val num1 = numbers[i]
            val num2 = numbers[i + 1]

            numbers.removeAt(i)
            numbers.removeAt(i)
            operations.removeAt(i)

            if (op == '*') {
                numbers.add(i, num1 * num2)
            } else if (op == '/') {
                if (num2 == 0.0) throw ArithmeticException("Division by zero")
                numbers.add(i, num1 / num2)
            }
        }

        while (operations.isNotEmpty()) {
            val i = operations.indexOfFirst { it == '+' || it == '-' }
            val op = operations[i]
            val num1 = numbers[i]
            val num2 = numbers[i + 1]

            numbers.removeAt(i)
            numbers.removeAt(i)
            operations.removeAt(i)

            if (op == '+') {
                numbers.add(i, num1 + num2)
            } else if (op == '-') {
                numbers.add(i, num1 - num2)
            }
        }

        return numbers[0]
    }


}