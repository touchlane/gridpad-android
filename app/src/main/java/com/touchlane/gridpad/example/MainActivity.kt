/*
 * MIT License
 *
 * Copyright (c) 2022 Touchlane LLC tech@touchlane.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.touchlane.gridpad.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells
import com.touchlane.gridpad.example.ui.component.*
import com.touchlane.gridpad.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GridPadExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListOfPads()
                }
            }
        }
    }
}

@Composable
fun PadCard(ratio: Float = 1f, content: @Composable () -> Unit) {
    GridPad(
        cells = GridPadCells.Builder(rowCount = 3, columnCount = 4)
            .rowSize(index = 0, size = GridPadCellSize.Weight(2f))
            .columnSize(index = 3, size = GridPadCellSize.Fixed(32.dp))
            .build()
    ) {

    }
    Card(
        Modifier
            .fillMaxWidth()
            .aspectRatio(ratio)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

@Composable
fun PinPadCard() {
    PadCard(ratio = 1.2f) {
        PinPadTheme(
            colors = PinPadColors(
                content = White,
                removeContent = HeatWave,
                background = AswadBlack
            )
        ) {
            PinPad()
        }
    }
}

@Composable
fun SimpleCalculatorPadCard() {
    PadCard(ratio = 0.9f) {
        SimpleCalculatorPadTheme(
            colors = SimpleCalculatorPadColors(
                content = AswadBlack,
                removeContent = HeatWave,
                actionsContent = AndreaBlue,
                background = White
            )
        ) {
            SimpleCalculatorPad()
        }
    }
}

@Composable
fun SimplePriorityCalculatorPadCard() {
    PadCard {
        SimplePriorityCalculatorPadTheme(
            colors = SimplePriorityCalculatorPadColors(
                content = White,
                background = AswadBlack,
                removeBackground = HeatWave,
                actionsBackground = AndreaBlue
            )
        ) {
            SimplePriorityCalculatorPad()
        }
    }
}

@Composable
fun EngineeringCalculatorPadCard() {
    PadCard(ratio = 1.1f) {
        EngineeringCalculatorPadTheme(
            colors = EngineeringCalculatorPadColors(
                content = AswadBlack,
                removeContent = HeatWave,
                actionsContent = AndreaBlue,
                functionsContent = BarneyPurple,
                background = White,
                numBackground = White.copy(alpha = 0.1f)
            )
        ) {
            EngineeringCalculatorPad()
        }
    }
}

@Composable
fun ListOfPads(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            PinPadCard()
        }
        item {
            SimpleCalculatorPadCard()
        }
        item {
            SimplePriorityCalculatorPadCard()
        }
        item {
            EngineeringCalculatorPadCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GridPadExampleTheme {
        ListOfPads()
    }
}