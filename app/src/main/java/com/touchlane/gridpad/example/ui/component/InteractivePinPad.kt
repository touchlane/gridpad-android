package com.touchlane.gridpad.example.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun InteractivePinPad(modifier: Modifier = Modifier) {
    var digits by remember { mutableStateOf("") }
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(size = 8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = digits,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        PinPad(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
        ) { action ->
            when (action) {
                'r' -> {
                    if (digits.isNotEmpty()) {
                        digits = digits.substring(0, digits.length - 1)
                    }
                }
                else -> {
                    if (digits.length < 6) {
                        digits += action
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun InteractivePinPadPreview() {
    GridPadExampleTheme {
        InteractivePinPad()
    }
}