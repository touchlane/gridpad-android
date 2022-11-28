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
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun SimplePriorityCalculatorPad(modifier: Modifier = Modifier) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = SimplePriorityCalculatorPadTheme.colors.content,
            background = SimplePriorityCalculatorPadTheme.colors.background
        )
    ) {
        GridPad(
            cells = GridPadCells.Builder(rowCount = 5, columnCount = 5)
                .rowSize(0, GridPadCellSize.Fixed(64.dp)).build(), modifier = modifier
        ) {
            //row 0
            item {
                RemoveTheme {
                    MediumTextPadButton(text = "C", onClick = {})
                }
            }
            item(columnSpan = 2) {
                ActionTheme {
                    MediumTextPadButton(text = "(", onClick = {})
                }
            }
            item(columnSpan = 2) {
                ActionTheme {
                    MediumTextPadButton(text = ")", onClick = {})
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
                    LargeTextPadButton(text = "×", onClick = {})
                }
            }
            item {
                ActionTheme {
                    LargeTextPadButton(text = "÷", onClick = {})
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
            item(rowSpan = 2) {
                ActionTheme {
                    LargeTextPadButton(text = "−", onClick = {})
                }
            }
            item(rowSpan = 2) {
                ActionTheme {
                    LargeTextPadButton(text = "+", onClick = {})
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
            //row 4
            item(row = 4, column = 0) {
                LargeTextPadButton(text = "0", onClick = {})
            }
            item {
                LargeTextPadButton(text = ".", onClick = {})
            }
            item {
                RemoveTheme {
                    IconPadButton(icon = Icons.Default.Backspace, onClick = {})
                }
            }
            item(columnSpan = 2) {
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
            content = SimplePriorityCalculatorPadTheme.colors.content,
            background = SimplePriorityCalculatorPadTheme.colors.removeBackground
        )
    ) {
        content()
    }
}

@Composable
private fun ActionTheme(content: @Composable () -> Unit) {
    PadButtonTheme(
        padButtonColors = PadButtonColors(
            content = SimplePriorityCalculatorPadTheme.colors.content,
            background = SimplePriorityCalculatorPadTheme.colors.actionsBackground
        )
    ) {
        content()
    }
}

@Immutable
data class SimplePriorityCalculatorPadColors(
    val content: Color,
    val background: Color,
    val removeBackground: Color,
    val actionsBackground: Color
) {
    companion object {
        val Default = SimplePriorityCalculatorPadColors(
            content = Color.Unspecified,
            background = Color.Unspecified,
            removeBackground = Color.Unspecified,
            actionsBackground = Color.Unspecified
        )
    }
}

val LocalSimplePriorityCalculatorPadColors = staticCompositionLocalOf {
    SimplePriorityCalculatorPadColors.Default
}

@Composable
fun SimplePriorityCalculatorPadTheme(
    colors: SimplePriorityCalculatorPadColors = SimplePriorityCalculatorPadColors.Default,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSimplePriorityCalculatorPadColors provides colors, content = content
    )
}

object SimplePriorityCalculatorPadTheme {
    val colors: SimplePriorityCalculatorPadColors
        @Composable get() = LocalSimplePriorityCalculatorPadColors.current
}

@Preview(showBackground = true, widthDp = 500, heightDp = 600)
@Preview(showBackground = true, widthDp = 500, heightDp = 400)
@Composable
fun SimplePriorityCalculatorPadPreview() {
    GridPadExampleTheme {
        SimplePriorityCalculatorPad()
    }
}