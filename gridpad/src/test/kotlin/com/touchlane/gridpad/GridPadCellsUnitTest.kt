package com.touchlane.gridpad

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
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
    fun `Check constructors of GridPadCells`() {
        val left = GridPadCells.Builder(2, 2).build()
        assertEquals(left, GridPadCells(rows = 2, columns = 2))
        assertEquals(
            left, GridPadCells(
                rowSizes = GridPadCellSize.weight(2),
                columnSizes = GridPadCellSize.weight(2)
            )
        )
    }

    @Test
    fun `Check fields initialization`() {
        val cells = GridPadCells.Builder(2, 4)
            .columnSize(0, GridPadCellSize.Weight(1.75f))
            .columnSize(1, GridPadCellSize.Weight(1.5f))
            .columnSize(3, GridPadCellSize.Fixed(24.dp))
            .rowSize(0, GridPadCellSize.Fixed(14.dp))
            .rowSize(1, GridPadCellSize.Fixed(16.dp)).build()
        assertEquals(TotalSize(weight = 0f, fixed = 30.dp), cells.rowsTotalSize)
        assertEquals(TotalSize(weight = 4.25f, fixed = 24.dp), cells.columnsTotalSize)
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
}