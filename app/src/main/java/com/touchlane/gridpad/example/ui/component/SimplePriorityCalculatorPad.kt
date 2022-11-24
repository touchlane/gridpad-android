package com.touchlane.gridpad.example.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCellSize
import com.touchlane.gridpad.GridPadCells
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun SimplePriorityCalculatorPad(modifier: Modifier = Modifier) {
    GridPad(
        cells = GridPadCells.Builder(rowCount = 5, columnCount = 5)
            .rowSize(0, GridPadCellSize.Fixed(64.dp)).build(),
        modifier = modifier
    ) {
        //row 0
        item {
            SmallTextPadButton(text = "C", onClick = {})
        }
        item(columnSpan = 2) {
            SmallTextPadButton(text = "(", onClick = {})
        }
        item(columnSpan = 2) {
            SmallTextPadButton(text = ")", onClick = {})
        }
        //row 1
        item {
            TextPadButton(text = "7", onClick = {})
        }
        item {
            TextPadButton(text = "8", onClick = {})
        }
        item {
            TextPadButton(text = "9", onClick = {})
        }
        item {
            TextPadButton(text = "×", onClick = {})
        }
        item {
            TextPadButton(text = "÷", onClick = {})
        }
        //row 2
        item {
            TextPadButton(text = "4", onClick = {})
        }
        item {
            TextPadButton(text = "5", onClick = {})
        }
        item {
            TextPadButton(text = "6", onClick = {})
        }
        item(rowSpan = 2) {
            TextPadButton(text = "−", onClick = {})
        }
        item(rowSpan = 2) {
            TextPadButton(text = "+", onClick = {})
        }
        //row 3
        item(row = 3, column = 0) {
            TextPadButton(text = "1", onClick = {})
        }
        item {
            TextPadButton(text = "2", onClick = {})
        }
        item {
            TextPadButton(text = "3", onClick = {})
        }
        //row 4
        item(row = 4, column = 0) {
            TextPadButton(text = "0", onClick = {})
        }
        item {
            TextPadButton(text = ".", onClick = {})
        }
        item {
            IconPadButton(icon = Icons.Default.Backspace, onClick = {})
        }
        item(columnSpan = 2) {
            TextPadButton(text = "=", onClick = {})
        }
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 600)
@Preview(showBackground = true, widthDp = 500, heightDp = 400)
@Composable
fun SimplePriorityCalculatorPadPreview() {
    GridPadExampleTheme {
        SimplePriorityCalculatorPad()
    }
}