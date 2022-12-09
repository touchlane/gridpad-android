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

@file:Suppress("IllegalIdentifier")

package com.touchlane.gridpad

import androidx.compose.material3.Text
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class GridPadScopeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun `Check sequential addition of elements`() = with(composeTestRule) {
        setContent {
            GridPad(cells = GridPadCells.Builder(rowCount = 3, columnCount = 3).build()) {
                //fill grid
                (0..2).forEach { row ->
                    (0..2).forEach { column ->
                        item { Text(text = "$row:$column") }
                    }
                }
                //extra row
                item { Text(text = "3:0") }
            }
        }
        (0..2).forEach { row ->
            (0..2).forEach { column ->
                onNode(hasText("$row:$column"))
                    .assertExists()
                    .assertIsDisplayed()
            }
        }
        //check ignoring
        onNode(hasText("3:0"))
            .assertDoesNotExist()

        //check positions
        val bounds22 = boundsForNodeWithText("2:2")
        val bounds00 = boundsForNodeWithText("0:0")
        assertTrue(bounds00.right < bounds22.left)
        assertTrue(bounds00.bottom < bounds22.top)
    }

    @Test
    fun `Check revert addition of elements`() = with(composeTestRule) {
        setContent {
            GridPad(cells = GridPadCells.Builder(rowCount = 3, columnCount = 3).build()) {
                item(row = 1, column = 2) { Text(text = "1:2_1") }
                item(row = 1, column = 1) { Text(text = "1:1") }
                item { Text(text = "1:2_2") }
            }
        }
        //check positions
        val bounds121 = boundsForNodeWithText("1:2_1")
        val bounds11 = boundsForNodeWithText("1:1")
        val bounds122 = boundsForNodeWithText("1:2_2")
        assertTrue(bounds11.left < bounds121.left)
        assertEquals(bounds11.bottom, bounds121.bottom)
        assertEquals(bounds121, bounds122)
    }

    @Test
    fun `Check skipping out of grid elements`() = with(composeTestRule) {
        setContent {
            GridPad(cells = GridPadCells.Builder(rowCount = 3, columnCount = 3).build()) {
                item(row = 0, column = 3) { Text(text = "0:3") }
                item(row = 3, column = 0) { Text(text = "3:0") }
                item(row = -1, column = 1) { Text(text = "-1:0") }
                item(row = 0, column = 0, rowSpan = 4, columnSpan = 1) { Text(text = "0:0") }
            }
        }
        onNode(hasText("0:3"))
            .assertDoesNotExist()
        onNode(hasText("3:0"))
            .assertDoesNotExist()
        onNode(hasText("-1:0"))
            .assertDoesNotExist()
        onNode(hasText("0:0"))
            .assertDoesNotExist()
    }
}

internal fun ComposeContentTestRule.boundsForNodeWithText(text: String): Rect {
    return onNode(hasText(text)).fetchSemanticsNode("Node with '$text' not found").boundsInRoot
}