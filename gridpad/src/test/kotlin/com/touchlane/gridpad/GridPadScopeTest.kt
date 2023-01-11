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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.touchlane.gridpad.GridPadPlacementPolicy.HorizontalDirection.*
import com.touchlane.gridpad.GridPadPlacementPolicy.MainAxis.*
import com.touchlane.gridpad.GridPadPlacementPolicy.VerticalDirection.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GridPadScopeTest : LoggerTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

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

    @Test
    fun `Check size distribution 100x100`() = with(composeTestRule) {
        setContent {
            Box(modifier = Modifier.size(100.dp, 100.dp)) {
                GridPad(cells = GridPadCells(rowCount = 3, columnCount = 3)) {
                    item(row = 0, column = 0) { MaxSizeText(text = "0:0") }
                    item(row = 0, column = 1) { MaxSizeText(text = "0:1") }
                    item(row = 0, column = 2) { MaxSizeText(text = "0:2") }
                    item(row = 1, column = 0) { MaxSizeText(text = "1:0") }
                    item(row = 2, column = 0) { MaxSizeText(text = "2:0") }
                }
            }
        }
        val bounds00 = boundsForNodeWithText("0:0")
        assertEquals(Rect(0f, 0f, 33f, 33f), bounds00)
        val bounds01 = boundsForNodeWithText("0:1")
        assertEquals(Rect(33f, 0f, 66f, 33f), bounds01)
        val bounds02 = boundsForNodeWithText("0:2")
        assertEquals(Rect(66f, 0f, 100f, 33f), bounds02)
        val bounds10 = boundsForNodeWithText("1:0")
        assertEquals(Rect(0f, 33f, 33f, 66f), bounds10)
        val bounds20 = boundsForNodeWithText("2:0")
        assertEquals(Rect(0f, 66f, 33f, 100f), bounds20)
    }

    @Test
    fun `Check size distribution 101x10`() = with(composeTestRule) {
        setContent {
            Box(modifier = Modifier.size(101.dp, 10.dp)) {
                GridPad(cells = GridPadCells(rowCount = 1, columnCount = 3)) {
                    item(row = 0, column = 0) { MaxSizeText(text = "0:0") }
                    item(row = 0, column = 1) { MaxSizeText(text = "0:1") }
                    item(row = 0, column = 2) { MaxSizeText(text = "0:2") }
                }
            }
        }
        val bounds00 = boundsForNodeWithText("0:0")
        assertEquals(Rect(0f, 0f, 33f, 10f), bounds00)
        val bounds01 = boundsForNodeWithText("0:1")
        assertEquals(Rect(33f, 0f, 67f, 10f), bounds01)
        val bounds02 = boundsForNodeWithText("0:2")
        assertEquals(Rect(67f, 0f, 101f, 10f), bounds02)
    }

    @Test
    fun `Check size distribution 76x10`() = with(composeTestRule) {
        setContent {
            Box(modifier = Modifier.size(76.dp, 10.dp)) {
                GridPad(cells = GridPadCells(rowCount = 1, columnCount = 7)) {
                    item(row = 0, column = 0) { MaxSizeText(text = "0:0") }
                    item(row = 0, column = 1) { MaxSizeText(text = "0:1") }
                    item(row = 0, column = 2) { MaxSizeText(text = "0:2") }
                    item(row = 0, column = 3) { MaxSizeText(text = "0:3") }
                    item(row = 0, column = 4) { MaxSizeText(text = "0:4") }
                    item(row = 0, column = 5) { MaxSizeText(text = "0:5") }
                    item(row = 0, column = 6) { MaxSizeText(text = "0:6") }
                }
            }
        }
        val bounds00 = boundsForNodeWithText("0:0")
        assertEquals(Rect(0f, 0f, 10f, 10f), bounds00)
        val bounds01 = boundsForNodeWithText("0:1")
        assertEquals(Rect(10f, 0f, 21f, 10f), bounds01)
        val bounds02 = boundsForNodeWithText("0:2")
        assertEquals(Rect(21f, 0f, 32f, 10f), bounds02)
        val bounds03 = boundsForNodeWithText("0:3")
        assertEquals(Rect(32f, 0f, 43f, 10f), bounds03)
        val bounds04 = boundsForNodeWithText("0:4")
        assertEquals(Rect(43f, 0f, 54f, 10f), bounds04)
        val bounds05 = boundsForNodeWithText("0:5")
        assertEquals(Rect(54f, 0f, 65f, 10f), bounds05)
        val bounds06 = boundsForNodeWithText("0:6")
        assertEquals(Rect(65f, 0f, 76f, 10f), bounds06)
    }

    @Test
    fun `Check placement mainH horSE verTB LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = START_END,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheRightOf(cell0))
        assertTrue(cell3.isLocatedToTheBottomOf(cell0))
    }

    @Test
    fun `Check placement mainH horSE verTB RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = START_END,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheLeftOf(cell0))
        assertTrue(cell3.isLocatedToTheBottomOf(cell0))
    }

    @Test
    fun `Check placement mainH horES verTB LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = END_START,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheLeftOf(cell0))
        assertTrue(cell3.isLocatedToTheBottomOf(cell0))
    }

    @Test
    fun `Check placement mainH horES verTB RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = END_START,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheRightOf(cell0))
        assertTrue(cell3.isLocatedToTheBottomOf(cell0))
    }

    @Test
    fun `Check placement mainH horES verBT LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = END_START,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheLeftOf(cell0))
        assertTrue(cell3.isLocatedToTheTopOf(cell0))
    }

    @Test
    fun `Check placement mainH horES verBT RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = END_START,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheRightOf(cell0))
        assertTrue(cell3.isLocatedToTheTopOf(cell0))
    }

    @Test
    fun `Check placement mainH horSE verBT LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = START_END,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheRightOf(cell0))
        assertTrue(cell3.isLocatedToTheTopOf(cell0))
    }

    @Test
    fun `Check placement mainH horSE verBT RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = HORIZONTAL,
                horizontalDirection = START_END,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheLeftOf(cell0))
        assertTrue(cell3.isLocatedToTheTopOf(cell0))
    }

    @Test
    fun `Check placement mainV horSE verTB LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = START_END,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheBottomOf(cell0))
        assertTrue(cell3.isLocatedToTheRightOf(cell0))
    }

    @Test
    fun `Check placement mainV horSE verTB RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = START_END,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheBottomOf(cell0))
        assertTrue(cell3.isLocatedToTheLeftOf(cell0))
    }

    @Test
    fun `Check placement mainV horES verTB LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = END_START,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheBottomOf(cell0))
        assertTrue(cell3.isLocatedToTheLeftOf(cell0))
    }

    @Test
    fun `Check placement mainV horES verTB RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = END_START,
                verticalDirection = TOP_BOTTOM
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheBottomOf(cell0))
        assertTrue(cell3.isLocatedToTheRightOf(cell0))
    }

    @Test
    fun `Check placement mainV horES verBT LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = END_START,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheTopOf(cell0))
        assertTrue(cell3.isLocatedToTheLeftOf(cell0))
    }

    @Test
    fun `Check placement mainV horES verBT RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = END_START,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheTopOf(cell0))
        assertTrue(cell3.isLocatedToTheRightOf(cell0))
    }

    @Test
    fun `Check placement mainV horSE verBT LTR`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = START_END,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Ltr
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheTopOf(cell0))
        assertTrue(cell3.isLocatedToTheRightOf(cell0))
    }

    @Test
    fun `Check placement mainV horSE verBT RTL`() = with(composeTestRule) {
        placeItems(
            GridPadPlacementPolicy(
                mainAxis = VERTICAL,
                horizontalDirection = START_END,
                verticalDirection = BOTTOM_TOP
            ), LayoutDirection.Rtl
        )
        val cell0 = boundsForNodeWithText("0")
        val cell1 = boundsForNodeWithText("1")
        val cell3 = boundsForNodeWithText("3")
        assertTrue(cell1.isLocatedToTheTopOf(cell0))
        assertTrue(cell3.isLocatedToTheLeftOf(cell0))
    }

    private fun ComposeContentTestRule.placeItems(
        policy: GridPadPlacementPolicy,
        layoutDirection: LayoutDirection
    ) {
        setContent {
            val rowCount = 3
            val columnCount = 3
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                Box(modifier = Modifier.size(30.dp, 30.dp)) {
                    GridPad(
                        cells = GridPadCells(rowCount = rowCount, columnCount = columnCount),
                        placementPolicy = policy
                    ) {
                        repeat(rowCount * columnCount) {
                            item { MaxSizeText(text = "$it") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MaxSizeText(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxSize()
    )
}

internal fun ComposeContentTestRule.boundsForNodeWithText(text: String): Rect {
    return onNode(hasText(text)).fetchSemanticsNode("Node with '$text' not found").boundsInRoot
}

internal fun Rect.isLocatedToTheLeftOf(target: Rect): Boolean {
    return left < target.left
}

internal fun Rect.isLocatedToTheRightOf(target: Rect): Boolean {
    return right > target.right
}

internal fun Rect.isLocatedToTheTopOf(target: Rect): Boolean {
    return top < target.top
}

internal fun Rect.isLocatedToTheBottomOf(target: Rect): Boolean {
    return bottom > target.bottom
}
