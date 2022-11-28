package com.touchlane.gridpad.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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