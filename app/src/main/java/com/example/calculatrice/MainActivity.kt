package com.example.calculatrice

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    private lateinit var expression: TextView
    private lateinit var result: TextView
    private lateinit var clean: Button
    private lateinit var backSpace: Button
    private lateinit var percent: Button
    private lateinit var divide: Button
    private lateinit var multiply: Button
    private lateinit var add: Button
    private lateinit var subtract: Button
    private lateinit var equal: Button
    private lateinit var dot: Button
    private lateinit var zero: Button
    private lateinit var doubleZero: Button
    private lateinit var one: Button
    private lateinit var two: Button
    private lateinit var three: Button
    private lateinit var four: Button
    private lateinit var five: Button
    private lateinit var six: Button
    private lateinit var seven: Button
    private lateinit var eight: Button
    private lateinit var nine: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurer la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Calculatrice"

        // Initialisation des vues
        expression = findViewById(R.id.expression)
        result = findViewById(R.id.result)
        clean = findViewById(R.id.clean)
        backSpace = findViewById(R.id.backSpace)
        percent = findViewById(R.id.percent)
        divide = findViewById(R.id.divide)
        multiply = findViewById(R.id.multiply)
        add = findViewById(R.id.add)
        subtract = findViewById(R.id.subtract)
        equal = findViewById(R.id.equal)
        dot = findViewById(R.id.dot)
        zero = findViewById(R.id.zero)
        doubleZero = findViewById(R.id.doubleZero)
        one = findViewById(R.id.one)
        two = findViewById(R.id.two)
        three = findViewById(R.id.three)
        four = findViewById(R.id.four)
        five = findViewById(R.id.five)
        six = findViewById(R.id.six)
        seven = findViewById(R.id.seven)
        eight = findViewById(R.id.eight)
        nine = findViewById(R.id.nine)

        // Ajouter des écouteurs de clic pour les boutons
        clean.setOnClickListener {
            expression.text = ""
            result.text = ""
        }

        backSpace.setOnClickListener {
            val currentText = expression.text.toString()
            if (currentText.isNotEmpty()) {
                expression.text = currentText.substring(0, currentText.length - 1)
            }
        }

        percent.setOnClickListener {
            appendToExpression("%")
        }

        divide.setOnClickListener {
            appendToExpression("/")
        }

        multiply.setOnClickListener {
            appendToExpression("*")
        }

        add.setOnClickListener {
            appendToExpression("+")
        }

        subtract.setOnClickListener {
            appendToExpression("-")
        }

        equal.setOnClickListener {
            calculateResult()
        }

        dot.setOnClickListener {
            appendToExpression(".")
        }

        zero.setOnClickListener {
            appendToExpression("0")
        }

        doubleZero.setOnClickListener {
            appendToExpression("00")
        }

        one.setOnClickListener {
            appendToExpression("1")
        }

        two.setOnClickListener {
            appendToExpression("2")
        }

        three.setOnClickListener {
            appendToExpression("3")
        }

        four.setOnClickListener {
            appendToExpression("4")
        }

        five.setOnClickListener {
            appendToExpression("5")
        }

        six.setOnClickListener {
            appendToExpression("6")
        }

        seven.setOnClickListener {
            appendToExpression("7")
        }

        eight.setOnClickListener {
            appendToExpression("8")
        }

        nine.setOnClickListener {
            appendToExpression("9")
        }
    }

    private fun appendToExpression(value: String) {
        expression.append(value)
    }

    private fun calculateResult() {
        val expressionText = expression.text.toString()
        try {
            val resultValue = evaluateExpression(expressionText)
            result.text = resultValue.toString()
        } catch (e: Exception) {
            result.text = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        return eval(expression)
    }

    private fun eval(expression: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm()
                    else if (eat('-'.toInt())) x -= parseTerm()
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor()
                    else if (eat('/'.toInt())) x /= parseFactor()
                    else if (eat('%'.toInt())) x = x * parseFactor() / 100.0 // Gestion du pourcentage
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor()
                if (eat('-'.toInt())) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('('.toInt())) {
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) {
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = expression.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                return x
            }
        }.parse()
    }
}