package com.touchlane.gridpad.example.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.touchlane.gridpad.GridPad
import com.touchlane.gridpad.GridPadCells
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun PinPad(modifier: Modifier = Modifier) {
    GridPad(
        cells = GridPadCells.Builder(rowCount = 4, columnCount = 3).build(),
        modifier = modifier
    ) {
        //row 0
        item {
            TextPadButton(text = "7", onClick = {})
        }
        item {
            TextPadButton(text = "8", onClick = {})
        }
        item {
            TextPadButton(text = "9", onClick = {})
        }
        //row 1
        item {
            TextPadButton(text = "4", onClick = {})
        }
        item {
            TextPadButton(text = "5", onClick = {})
        }
        item {
            TextPadButton(text = "6", onClick = {})
        }
        //row 2
        item {
            TextPadButton(text = "1", onClick = {})
        }
        item {
            TextPadButton(text = "2", onClick = {})
        }
        item {
            TextPadButton(text = "3", onClick = {})
        }
        //row 3
        item(row = 3, column = 1) {
            TextPadButton(text = "0", onClick = {})
        }
        item {
            IconPadButton(icon = Icons.Default.Backspace, onClick = {})
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
@Composable
fun PinPadPreview() {
    GridPadExampleTheme {
        PinPad()
    }
}