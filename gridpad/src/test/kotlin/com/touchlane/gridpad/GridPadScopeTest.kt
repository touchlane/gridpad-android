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

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
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
    fun `Check sequential addition of elements`() {

        /*composeTestRule.onNode(hasText("Small Button"))
            .assertExists()
            .assertHeightIsEqualTo(28.dp)*/

        val cells = GridPadCells.Builder(rowCount = 3, columnCount = 3).build()
        val scope = GridPadScopeImpl(cells)
        composeTestRule.setContent {
            GridPad(cells = cells) {
                //warm up
                item { }
            }
            scope.item { }
            scope.item { }
            scope.item { }
            //next row
            scope.item { }
            //line jumping checking
            assertEquals(1, scope.data[3].row)
            assertEquals(0, scope.data[3].column)
            scope.item { }
            scope.item { }
            //next row
            scope.item { }
            //line jumping checking
            assertEquals(2, scope.data[6].row)
            assertEquals(0, scope.data[6].column)
            scope.item { }
            scope.item { }
            //end of free cells
            assertEquals(9, scope.data.size)
            //next add should be skipped
            scope.item { }
            assertEquals(9, scope.data.size)
        }
    }

    @Test
    fun `Check revert addition of elements`() {
        val cells = GridPadCells.Builder(rowCount = 3, columnCount = 3).build()
        val scope = GridPadScopeImpl(cells)
        composeTestRule.setContent {
            GridPad(cells = cells) {
                //warm up
                item { }
            }
            scope.item(row = 1, column = 2) { }
            scope.item(row = 1, column = 1) { }
            scope.item { }
            assertEquals(3, scope.data.size)
            assertEquals(1, scope.data[2].row)
            assertEquals(2, scope.data[2].column)
            //last one should have same position as the first one
            assertEquals(1, scope.data[0].row)
            assertEquals(2, scope.data[0].column)
        }
    }

    @Test
    fun `Check skipping out of grid elements`() {
        val cells = GridPadCells.Builder(rowCount = 3, columnCount = 3).build()
        val scope = GridPadScopeImpl(cells)
        composeTestRule.setContent {
            GridPad(cells = cells) {
                //warm up
                item { }
            }
            //invalid position
            scope.item(row = 0, column = 3) { }
            assertEquals(0, scope.data.size)
            scope.item(row = -1, column = 1) { }
            assertEquals(0, scope.data.size)
            //span outside grid
            scope.item(row = 0, column = 0, rowSpan = 4, columnSpan = 1) { }
            assertEquals(0, scope.data.size)
        }
    }
}