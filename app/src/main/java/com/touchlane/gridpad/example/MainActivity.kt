package com.touchlane.gridpad.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells
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
                            Greeting("Android")
                        }
                    }
                    Column() {
                        Card(Modifier.size(width = 200.dp, height = 200.dp)) {
                            GridPad(
                                cells = GridPadCells.Builder(rows = 3, columns = 3)
                                    .columnSize(0, GridPadCellSize.Fixed(22.dp))
                                    .columnSize(1, GridPadCellSize.Weight(2f))
                                    .columnSize(2, GridPadCellSize.Weight(3f))
                                    .rowSize(0, GridPadCellSize.Fixed(40.dp)).build()
                            ) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Yellow)
                                    ) {
                                        Text(text = "1 one")
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Blue)
                                    ) {
                                        Text(text = "2 two")
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Cyan)
                                    ) {
                                        Text(text = "3 three")
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Red)
                                    ) {
                                        Text(text = "4 four")
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Green)
                                    ) {
                                        Text(text = "5 five")
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.DarkGray)
                                    ) {
                                        Text(text = "6 six")
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.LightGray)
                                    ) {
                                        Text(text = "7 seven")
                                    }
                                }
                            }
                        }
                        Card(Modifier.size(width = 200.dp, height = 200.dp)) {
                            Text(
                                text = "Hello world!",
                                modifier = Modifier.fillMaxSize(),
                                style = TextStyle(background = Color.Blue)
                            )
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