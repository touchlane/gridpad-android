package com.touchlane.gridpad.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.example.ui.component.PinPad
import com.touchlane.gridpad.example.ui.component.SimpleCalculatorPad
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GridPadExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn {
                        item {
                            Card(
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.2f)
                                    .padding(16.dp)
                            ) {
                                PinPad(modifier = Modifier.padding(8.dp))
                            }
                        }
                        item {
                            Card(
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.9f)
                                    .padding(16.dp)
                            ) {
                                SimpleCalculatorPad(modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GridPadExampleTheme {
        Greeting("Android")
    }
}