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
fun PinPad(modifier: Modifier = Modifier) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = PinPadTheme.colors.content,
            background = PinPadTheme.colors.background
        )
    ) {
        GridPad(
            cells = GridPadCells.Builder(rowCount = 4, columnCount = 3).build(),
            modifier = modifier
        ) {
            //row 0
            item {
                LargeTextPadButton(text = "7", onClick = {})
            }
            item {
                LargeTextPadButton(text = "8", onClick = {})
            }
            item {
                LargeTextPadButton(text = "9", onClick = {})
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
            //row 3
            item(row = 3, column = 1) {
                LargeTextPadButton(text = "0", onClick = {})
            }
            item {
                PadButtonTheme(
                    padButtonColors = PadButtonColors(
                        content = PinPadTheme.colors.removeContent,
                        background = PinPadTheme.colors.background
                    )
                ) {
                    IconPadButton(icon = Icons.Default.Backspace, onClick = {})
                }
            }
        }
    }
}

@Immutable
data class PinPadColors(
    val content: Color,
    val removeContent: Color,
    val background: Color
) {
    companion object {
        val Default = PinPadColors(
            content = Color.Unspecified,
            removeContent = Color.Unspecified,
            background = Color.Unspecified
        )
    }
}

val LocalPinPadColors = staticCompositionLocalOf {
    PinPadColors.Default
}

@Composable
fun PinPadTheme(
    colors: PinPadColors = PinPadColors.Default,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPinPadColors provides colors,
        content = content
    )
}

object PinPadTheme {
    val colors: PinPadColors
        @Composable
        get() = LocalPinPadColors.current
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
@Composable
fun PinPadPreview() {
    GridPadExampleTheme {
        PinPad()
    }
}