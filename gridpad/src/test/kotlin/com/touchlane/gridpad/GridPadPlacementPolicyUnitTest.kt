/*
 * MIT License
 *
 * Copyright (c) 2023 Touchlane LLC tech@touchlane.com
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

package com.touchlane.gridpad

import com.touchlane.gridpad.GridPadPlacementPolicy.HorizontalDirection.END_START
import com.touchlane.gridpad.GridPadPlacementPolicy.HorizontalDirection.START_END
import com.touchlane.gridpad.GridPadPlacementPolicy.VerticalDirection.BOTTOM_TOP
import com.touchlane.gridpad.GridPadPlacementPolicy.VerticalDirection.TOP_BOTTOM
import com.touchlane.gridpad.GridPadSpanAnchor.Horizontal.END
import com.touchlane.gridpad.GridPadSpanAnchor.Horizontal.START
import com.touchlane.gridpad.GridPadSpanAnchor.Vertical.BOTTOM
import com.touchlane.gridpad.GridPadSpanAnchor.Vertical.TOP
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for GridPadPlacementPolicy
 */
class GridPadPlacementPolicyUnitTest {

    @Test
    fun `Test anchor default initialization`() {
        val policy = GridPadPlacementPolicy()
        assertEquals(GridPadSpanAnchor(horizontal = START, vertical = TOP), policy.anchor)
    }

    @Test
    fun `Test anchor top start`() {
        val policy =
            GridPadPlacementPolicy(horizontalDirection = START_END, verticalDirection = TOP_BOTTOM)
        assertEquals(GridPadSpanAnchor(horizontal = START, vertical = TOP), policy.anchor)
    }

    @Test
    fun `Test anchor top end`() {
        val policy =
            GridPadPlacementPolicy(horizontalDirection = END_START, verticalDirection = TOP_BOTTOM)
        assertEquals(GridPadSpanAnchor(horizontal = END, vertical = TOP), policy.anchor)
    }

    @Test
    fun `Test anchor bottom end`() {
        val policy =
            GridPadPlacementPolicy(horizontalDirection = END_START, verticalDirection = BOTTOM_TOP)
        assertEquals(GridPadSpanAnchor(horizontal = END, vertical = BOTTOM), policy.anchor)
    }

    @Test
    fun `Test anchor bottom start`() {
        val policy =
            GridPadPlacementPolicy(horizontalDirection = START_END, verticalDirection = BOTTOM_TOP)
        assertEquals(GridPadSpanAnchor(horizontal = START, vertical = BOTTOM), policy.anchor)
    }
}
