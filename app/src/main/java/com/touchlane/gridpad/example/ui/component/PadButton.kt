package com.touchlane.gridpad.example.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun TextPadButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    PadButton(onClick = onClick, modifier = modifier) {
        Text(text = text, style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
fun IconPadButton(icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    PadButton(onClick = onClick, modifier = modifier) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun PadButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val corner: Dp by animateDpAsState(if (isPressed) 20.dp else 50.dp)
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        shape = RoundedCornerShape(corner),
        interactionSource = interactionSource
    ) {
        content()
    }
}

@ButtonPreviews
@Composable
fun TextPadButtonPreview() {
    GridPadExampleTheme {
        TextPadButton("1", {})
    }
}

@ButtonPreviews
@Composable
fun IconPadButtonPreview() {
    GridPadExampleTheme {
        IconPadButton(Icons.Default.Backspace, {})
    }
}

@Preview(
    name = "Large 128",
    group = "PadButton",
    widthDp = 128,
    heightDp = 128,
)
@Preview(
    name = "Medium 86",
    group = "PadButton",
    widthDp = 86,
    heightDp = 86,
)
@Preview(
    name = "Medium Horizontal 128x86",
    group = "PadButton",
    widthDp = 128,
    heightDp = 86,
)
@Preview(
    name = "Medium Vertical 86x128",
    group = "PadButton",
    widthDp = 86,
    heightDp = 128,
)
annotation class ButtonPreviews