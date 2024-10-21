package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    MaterialTheme {
        CalculatorApp()
    }
}

@Preview
@Composable
fun CalculatorApp() {
    var displayValue by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Display(displayValue)

        Column {
            Row(Modifier.fillMaxWidth()) {
                ButtonComponent(label = "7") { displayValue = appendValue(displayValue, "7") }
                ButtonComponent(label = "8") { displayValue = appendValue(displayValue, "8") }
                ButtonComponent(label = "9") { displayValue = appendValue(displayValue, "9") }
                ButtonComponent(label = "/") { displayValue = appendValue(displayValue, "/") }
            }
            Row(Modifier.fillMaxWidth()) {
                ButtonComponent(label = "4") { displayValue = appendValue(displayValue, "4") }
                ButtonComponent(label = "5") { displayValue = appendValue(displayValue, "5") }
                ButtonComponent(label = "6") { displayValue = appendValue(displayValue, "6") }
                ButtonComponent(label = "*") { displayValue = appendValue(displayValue, "*") }
            }
            Row(Modifier.fillMaxWidth()) {
                ButtonComponent(label = "1") { displayValue = appendValue(displayValue, "1") }
                ButtonComponent(label = "2") { displayValue = appendValue(displayValue, "2") }
                ButtonComponent(label = "3") { displayValue = appendValue(displayValue, "3") }
                ButtonComponent(label = "-") { displayValue = appendValue(displayValue, "-") }
            }
            Row(Modifier.fillMaxWidth()) {
                ButtonComponent(label = "C") { displayValue = "0" }
                ButtonComponent(label = "0") { displayValue = appendValue(displayValue, "0") }
                ButtonComponent(label = "=") {
                    try {
                        displayValue = evaluateExpression(displayValue)
                    } catch (e: Exception) {
                        displayValue = "Error"
                    }
                }
                ButtonComponent(label = "+") { displayValue = appendValue(displayValue, "+") }
            }
        }
    }
}

@Composable
fun Display(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(16.dp),
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 32.sp),
        textAlign = TextAlign.End
    )
}

@Composable
fun ButtonComponent(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
    ) {
        Text(text = label)
    }
}

fun appendValue(currentValue: String, newValue: String): String {
    return if (currentValue == "0") newValue else currentValue + newValue
}


fun evaluateExpression(expression: String): String {
    val operators = mutableListOf<Char>()
    val numbers = mutableListOf<Double>()

    var currentNumber = StringBuilder()

    for (char in expression) {
        if (char.isDigit() || char == '.') {
            currentNumber.append(char)
        } else if (char == '+' || char == '-' || char == '*' || char == '/') {
            numbers.add(currentNumber.toString().toDouble())
            operators.add(char)
            currentNumber = StringBuilder() // Reset para el siguiente número
        }
    }


    if (currentNumber.isNotEmpty()) {
        numbers.add(currentNumber.toString().toDouble())
    }


    var index = 0
    while (index < operators.size) {
        if (operators[index] == '*' || operators[index] == '/') {
            val result = if (operators[index] == '*') {
                numbers[index] * numbers[index + 1]
            } else {
                numbers[index] / numbers[index + 1]
            }
            numbers[index] = result
            numbers.removeAt(index + 1)
            operators.removeAt(index)
        } else {
            index++
        }
    }

    index = 0
    while (index < operators.size) {
        val result = if (operators[index] == '+') {
            numbers[index] + numbers[index + 1]
        } else {
            numbers[index] - numbers[index + 1]
        }
        numbers[index] = result
        numbers.removeAt(index + 1)
        operators.removeAt(index)
    }

    return numbers[0].toString()
}
