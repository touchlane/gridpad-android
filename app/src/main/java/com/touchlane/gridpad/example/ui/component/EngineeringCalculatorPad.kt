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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun EngineeringCalculatorPad(modifier: Modifier = Modifier) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = EngineeringCalculatorPadTheme.colors.content,
            background = EngineeringCalculatorPadTheme.colors.background
        )
    ) {
        GridPad(
            cells = GridPadCells.Builder(rowCount = 5, columnCount = 5)
                .rowSize(0, GridPadCellSize.Fixed(48.dp)).build(),
            modifier = modifier
        ) {
            //BG space
            item(row = 1, column = 0, rowSpan = 4, columnSpan = 3) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(size = 8.dp))
                        .background(EngineeringCalculatorPadTheme.colors.numBackground)
                )
            }
            //row 0
            item(row = 0, column = 0) {
                FunctionTheme {
                    SmallTextPadButton(text = "sin", onClick = {})
                }
            }
            item {
                FunctionTheme {
                    SmallTextPadButton(text = "cos", onClick = {})
                }
            }
            item {
                FunctionTheme {
                    SmallTextPadButton(text = "log", onClick = {})
                }
            }
            item {
                FunctionTheme {
                    SmallTextPadButton(text = "π", onClick = {})
                }
            }
            item {
                FunctionTheme {
                    SmallTextPadButton(text = "√", onClick = {})
                }
            }
            //row 1
            item(row = 1, column = 0) {
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
                    LargeTextPadButton(text = "(", onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = ")", onClick = {})
                }
            }
            //row 2
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
                    LargeTextPadButton(text = "×", onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "÷", onClick = {})
                }
            }
            //row 3
            item(row = 3, column = 0) {
                LargeTextPadButton(text = "1", onClick = {})
            }
            item {
                LargeTextPadButton(text = "2", onClick = {})
            }
            item {
                LargeTextPadButton(text = "3", onClick = {})
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "−", onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "+", onClick = {})
                }
            }
            //row 4
            item {
                RemoveTheme {
                    LargeTextPadButton(text = "C", onClick = {})
                }
            }
            item {
                LargeTextPadButton(text = ".", onClick = {})
            }
            item {
                LargeTextPadButton(text = "0", onClick = {})
            }
            item {
                RemoveTheme {
                    IconPadButton(icon = Icons.Default.Backspace, onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "=", onClick = {})
                }
            }
        }
    }
}

@Composable
private fun RemoveTheme(content: @Composable () -> Unit) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = EngineeringCalculatorPadTheme.colors.removeContent,
            background = EngineeringCalculatorPadTheme.colors.background
        )
    ) {
        content()
    }
}

@Composable
private fun ActionTheme(content: @Composable () -> Unit) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = EngineeringCalculatorPadTheme.colors.actionsContent,
            background = EngineeringCalculatorPadTheme.colors.background
        )
    ) {
        content()
    }
}

@Composable
private fun FunctionTheme(content: @Composable () -> Unit) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = EngineeringCalculatorPadTheme.colors.background,
            background = EngineeringCalculatorPadTheme.colors.functionsContent
        )
    ) {
        content()
    }
}

@Immutable
data class EngineeringCalculatorPadColors(
    val content: Color,
    val removeContent: Color,
    val actionsContent: Color,
    val functionsContent: Color,
    val background: Color,
    val numBackground: Color
) {
    companion object {
        val Default = EngineeringCalculatorPadColors(
            content = Color.Unspecified,
            removeContent = Color.Unspecified,
            actionsContent = Color.Unspecified,
            functionsContent = Color.Unspecified,
            background = Color.Unspecified,
            numBackground = Color.Unspecified
        )
    }
}

val LocalEngineeringCalculatorPadColors = staticCompositionLocalOf {
    EngineeringCalculatorPadColors.Default
}

@Composable
fun EngineeringCalculatorPadTheme(
    colors: EngineeringCalculatorPadColors = EngineeringCalculatorPadColors.Default,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalEngineeringCalculatorPadColors provides colors,
        content = content
    )
}

object EngineeringCalculatorPadTheme {
    val colors: EngineeringCalculatorPadColors
        @Composable
        get() = LocalEngineeringCalculatorPadColors.current
}

@Preview(showBackground = true, widthDp = 500, heightDp = 600)
@Preview(showBackground = true, widthDp = 500, heightDp = 400)
@Composable
fun EngineeringCalculatorPadPreview() {
    GridPadExampleTheme {
        EngineeringCalculatorPad()
    }
}