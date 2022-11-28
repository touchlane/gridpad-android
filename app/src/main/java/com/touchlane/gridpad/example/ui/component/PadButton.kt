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

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.example.ui.theme.GridPadExampleTheme

@Composable
fun LargeTextPadButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextPadButton(
        text = text,
        onClick = onClick,
        style = MaterialTheme.typography.displaySmall,
        modifier = modifier
    )
}

@Composable
fun MediumTextPadButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextPadButton(
        text = text,
        onClick = onClick,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}

@Composable
fun SmallTextPadButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextPadButton(
        text = text,
        onClick = onClick,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@Composable
fun TextPadButton(
    text: String,
    style: TextStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PadButton(onClick = onClick, modifier = modifier) {
        Text(text = text, style = style, color = PadButtonTheme.colors.content)
    }
}

@Composable
fun IconPadButton(icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    PadButton(onClick = onClick, modifier = modifier) {
        val tint = PadButtonTheme.colors.content.takeOrElse {
            LocalContentColor.current
        }
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = tint
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
    val corner: Dp by animateDpAsState(
        if (isPressed) 20.dp else 50.dp,
        tween(150, easing = LinearOutSlowInEasing)
    )
    val containerColor = PadButtonTheme.colors.background.takeOrElse {
        MaterialTheme.colorScheme.primary
    }
    val contentColor = PadButtonTheme.colors.content.takeOrElse {
        MaterialTheme.colorScheme.onPrimary
    }
    val disabledContainerColor = containerColor.takeOrElse {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    }
    val disabledContentColor = contentColor.takeOrElse {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }
    val colors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        shape = RoundedCornerShape(corner),
        colors = colors,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource
    ) {
        content()
    }
}

@Immutable
data class PadButtonColors(
    val content: Color,
    val background: Color
) {
    companion object {
        val Default = PadButtonColors(
            content = Color.Unspecified,
            background = Color.Unspecified
        )
    }
}

val LocalPadButtonColors = staticCompositionLocalOf {
    PadButtonColors.Default
}

@Composable
fun PadButtonTheme(
    padButtonColors: PadButtonColors = PadButtonColors.Default,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPadButtonColors provides padButtonColors,
        content = content
    )
}

object PadButtonTheme {
    val colors: PadButtonColors
        @Composable
        get() = LocalPadButtonColors.current
}

@ButtonPreviews
@Composable
fun LargeTextPadButtonPreview() {
    GridPadExampleTheme {
        LargeTextPadButton("1", {})
    }
}

@ButtonPreviews
@Composable
fun MediumPadButtonPreview() {
    GridPadExampleTheme {
        MediumTextPadButton("2", {})
    }
}

@ButtonPreviews
@Composable
fun SmallPadButtonPreview() {
    GridPadExampleTheme {
        SmallTextPadButton("3", {})
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