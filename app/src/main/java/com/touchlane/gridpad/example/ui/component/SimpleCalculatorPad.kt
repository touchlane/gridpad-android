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

package com.touchlane.gridpad.example.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCells
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun SimpleCalculatorPad(modifier: Modifier = Modifier) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = SimpleCalculatorPadTheme.colors.content,
            background = SimpleCalculatorPadTheme.colors.background
        )
    ) {
        GridPad(
            cells = GridPadCells.Builder(rowCount = 5, columnCount = 4).build(),
            modifier = modifier
        ) {
            //row 0
            item {
                RemoveTheme {
                    LargeTextPadButton(text = "C", onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "÷", onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "×", onClick = {})
                }
            }
            item {
                RemoveTheme {
                    IconPadButton(icon = Icons.Default.Backspace, onClick = {})
                }
            }
            //row 1
            item {
                LargeTextPadButton(text = "7", onClick = {})
            }
            item {
                LargeTextPadButton(text = "8", onClick = {})
            }
            item {
                LargeTextPadButton(text = "9", onClick = {})
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "−", onClick = {})
                }
            }
            //row 1
            item {
                LargeTextPadButton(text = "4", onClick = {})
            }
            item {
                LargeTextPadButton(text = "5", onClick = {})
            }
            item {
                LargeTextPadButton(text = "6", onClick = {})
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "+", onClick = {})
                }
            }
            //row 2
            item {
                LargeTextPadButton(text = "1", onClick = {})
            }
            item {
                LargeTextPadButton(text = "2", onClick = {})
            }
            item {
                LargeTextPadButton(text = "3", onClick = {})
            }
            item(rowSpan = 2) {
                ActionTheme {
                    LargeTextPadButton(text = "=", onClick = {})
                }
            }
            //row 3
            item(row = 4, column = 0) {
                LargeTextPadButton(text = ".", onClick = {})
            }
            item(columnSpan = 2) {
                LargeTextPadButton(text = "0", onClick = {})
            }
        }
    }
}

@Composable
private fun RemoveTheme(content: @Composable () -> Unit) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = SimpleCalculatorPadTheme.colors.removeContent,
            background = SimpleCalculatorPadTheme.colors.background
        )
    ) {
        content()
    }
}

@Composable
private fun ActionTheme(content: @Composable () -> Unit) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = SimpleCalculatorPadTheme.colors.actionsContent,
            background = SimpleCalculatorPadTheme.colors.background
        )
    ) {
        content()
    }
}

@Immutable
data class SimpleCalculatorPadColors(
    val content: Color,
    val removeContent: Color,
    val actionsContent: Color,
    val background: Color
) {
    companion object {
        val Default = SimpleCalculatorPadColors(
            content = Color.Unspecified,
            removeContent = Color.Unspecified,
            actionsContent = Color.Unspecified,
            background = Color.Unspecified
        )
    }
}

val LocalSimpleCalculatorPadColors = staticCompositionLocalOf {
    SimpleCalculatorPadColors.Default
}

@Composable
fun SimpleCalculatorPadTheme(
    colors: SimpleCalculatorPadColors = SimpleCalculatorPadColors.Default,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSimpleCalculatorPadColors provides colors,
        content = content
    )
}

object SimpleCalculatorPadTheme {
    val colors: SimpleCalculatorPadColors
        @Composable
        get() = LocalSimpleCalculatorPadColors.current
}

@Preview(showBackground = true, widthDp = 400, heightDp = 500)
@Preview(showBackground = true, widthDp = 400, heightDp = 400)
@Composable
fun SimpleCalculatorPadPreview() {
    GridPadExampleTheme {
        SimpleCalculatorPad()
    }
}