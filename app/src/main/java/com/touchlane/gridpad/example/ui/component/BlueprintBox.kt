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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.example.ui.theme.AndreaBlue
import com.touchlane.gridpad.example.ui.theme.HeatWave

@Composable
fun BlueprintBox() {
    val stroke = Stroke(
        width = with(LocalDensity.current) { 1.dp.toPx() },
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = Color.White, style = stroke)
        }
    }
}

@Composable
fun ContentBlueprintBox(text: String = "") {
    val stroke = Stroke(
        width = with(LocalDensity.current) { 2.dp.toPx() },
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = HeatWave, style = stroke)
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = AndreaBlue,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(widthDp = 86, heightDp = 86)
@Composable
fun BlueprintBoxPreview() {
    BlueprintBox()
}

@Preview(widthDp = 86, heightDp = 86)
@Composable
fun ContentBlueprintBoxPreview() {
    ContentBlueprintBox("[0;0]")
}