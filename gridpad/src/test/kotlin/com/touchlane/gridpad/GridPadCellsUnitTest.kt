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

package com.touchlane.gridpad

import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Tests for GridPadCells
 */
class GridPadCellsUnitTest {
    @Test
    fun `Check equals() and hashCode() for the same GridPadCells`() {
        val left = GridPadCells.Builder(2, 4)
            .columnSize(1, GridPadCellSize.Weight(2f))
            .rowSize(0, GridPadCellSize.Fixed(24.dp)).build()
        val right = GridPadCells.Builder(2, 4)
            .columnSize(1, GridPadCellSize.Weight(2f))
            .rowSize(0, GridPadCellSize.Fixed(24.dp)).build()
        assertEquals(left.hashCode(), right.hashCode())
        assertEquals(left, right)
    }

    @Test
    fun `Check internal fields`() {
        val cells = GridPadCells.Builder(2, 4)
            .columnSize(0, GridPadCellSize.Fixed(12.dp))
            .columnSize(1, GridPadCellSize.Weight(2f))
            .columnSize(2, GridPadCellSize.Fixed(10.dp))
            .rowSize(0, GridPadCellSize.Weight(3f))
            .rowSize(1, GridPadCellSize.Fixed(24.dp)).build()
        assertEquals(2, cells.rowCount)
        assertEquals(4, cells.columnCount)
        assertEquals(24.dp, cells.rowsTotalSize.fixed)
        assertEquals(3f, cells.rowsTotalSize.weight)
        assertEquals(22.dp, cells.columnsTotalSize.fixed)
        assertEquals(3f, cells.rowsTotalSize.weight)
        assertEquals(
            cells.rowSizes,
            listOf(GridPadCellSize.Weight(3f), GridPadCellSize.Fixed(24.dp)).toImmutableList()
        )
        assertEquals(
            cells.columnSizes,
            listOf(
                GridPadCellSize.Fixed(12.dp),
                GridPadCellSize.Weight(2f),
                GridPadCellSize.Fixed(10.dp),
                GridPadCellSize.Weight(1f)
            ).toImmutableList()
        )
    }

    @Test
    fun `Check constructors of GridPadCells`() {
        val left = GridPadCells.Builder(2, 2).build()
        assertEquals(left, GridPadCells(rowCount = 2, columnCount = 2))
        assertEquals(
            left, GridPadCells(
                rowSizes = GridPadCellSize.weight(2),
                columnSizes = GridPadCellSize.weight(2)
            )
        )
    }

    @Test
    fun `Check GridPadCells_Builder methods`() {
        assertEquals(
            GridPadCells.Builder(2, 2)
                .rowSize(0, GridPadCellSize.Weight(3f))
                .rowSize(1, GridPadCellSize.Weight(3f))
                .columnSize(0, GridPadCellSize.Weight(2f))
                .columnSize(1, GridPadCellSize.Weight(2f))
                .build(),
            GridPadCells.Builder(2, 2)
                .rowsSize(GridPadCellSize.Weight(3f))
                .columnsSize(GridPadCellSize.Weight(2f))
                .build()
        )
    }

    @Test
    fun `Check extensions`() {
        assertEquals(
            listOf(GridPadCellSize.Fixed(1.dp), GridPadCellSize.Fixed(1.dp)),
            GridPadCellSize.fixed(2, 1.dp)
        )
        assertEquals(
            listOf(GridPadCellSize.Fixed(1.dp), GridPadCellSize.Fixed(2.dp)),
            GridPadCellSize.fixed(arrayOf(1.dp, 2.dp))
        )
        assertEquals(
            listOf(GridPadCellSize.Weight(0.5f), GridPadCellSize.Weight(0.5f)),
            GridPadCellSize.weight(2, 0.5f)
        )
        assertEquals(
            listOf(GridPadCellSize.Weight(0.5f), GridPadCellSize.Weight(1.5f)),
            GridPadCellSize.weight(0.5f, 1.5f)
        )
    }

    @Test
    fun `Check total size calculation`() {
        assertEquals(
            TotalSize(weight = 3f, fixed = 22.dp),
            listOf(
                GridPadCellSize.Fixed(12.dp),
                GridPadCellSize.Weight(2f),
                GridPadCellSize.Fixed(10.dp),
                GridPadCellSize.Weight(1f)
            ).calculateTotalSize()
        )
    }

    @Test
    fun `Check errors`() {
        assertThrows(IllegalStateException::class.java) {
            GridPadCellSize.Fixed(-1.dp)
        }
        assertThrows(IllegalStateException::class.java) {
            GridPadCellSize.Weight(0f)
        }
    }
}
