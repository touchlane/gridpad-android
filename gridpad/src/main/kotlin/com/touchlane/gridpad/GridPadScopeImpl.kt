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

import androidx.compose.runtime.Composable

/**
 * Implementation for [GridPadScope].
 * Checks and puts composables to display list with exact location specification.
 */
internal class GridPadScopeImpl(
    private val cells: GridPadCells,
    private val placementPolicy: GridPadPlacementPolicy
) : GridPadScope {

    /**
     * Display list with locations in a grid
     */
    internal val data: MutableList<GridPadContent> = mutableListOf()

    override fun item(
        row: Int?,
        column: Int?,
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable GridPadItemScope.() -> Unit
    ) {
        check(rowSpan > 0) {
            "`rowSpan` must be > 0"
        }
        check(columnSpan > 0) {
            "`columnSpan` must be > 0"
        }
        findPlaceForContent(
            row = row,
            column = column,
            rowSpan = rowSpan,
            columnSpan = columnSpan
        ) { top, left, right, bottom ->
            this.data.add(
                GridPadContent(
                    top = top,
                    left = left,
                    right = right,
                    bottom = bottom,
                    item = { itemContent() }
                )
            )
        }
    }

    private fun findPlaceForContent(
        row: Int?,
        column: Int?,
        rowSpan: Int,
        columnSpan: Int,
        callback: (top: Int, left: Int, right: Int, bottom: Int) -> Unit
    ): Boolean {
        //Skipping displaying items that out of grid
        if (cells.isOutsideOfGrid(row = row, column = column)) {
            return false
        }

        val lastItem = data.lastOrNull()
        var cellRow: Int
        var cellColumn: Int
        if (placementPolicy.mainAxis == GridPadPlacementPolicy.MainAxis.HORIZONTAL) {
            cellRow = row ?: findRow(lastItem)
            cellColumn = column ?: findNextColumn(lastItem)
            if (cells.isColumnOutsideOfGrid(cellColumn, columnSpan)) {
                cellColumn = firstColumn()
                cellRow = findNextRow(lastItem)
            }
        } else {
            cellColumn = column ?: findColumn(lastItem)
            cellRow = row ?: findNextRow(lastItem)
            if (cells.isRowOutsideOfGrid(cellRow, rowSpan)) {
                cellRow = firstRow()
                cellColumn = findNextColumn(lastItem)
            }
        }
        val rowOutside = cells.isRowOutsideOfGrid(cellRow, rowSpan)
        val columnOutside = cells.isColumnOutsideOfGrid(cellColumn, columnSpan)
        return if (rowOutside || columnOutside) {
            false
        } else {
            callback(cellRow, cellColumn, cellColumn + columnSpan, cellRow + rowSpan)
            true
        }
    }

    private fun firstRow(): Int {
        return when (placementPolicy.vertical) {
            GridPadPlacementPolicy.Vertical.FROM_TOP -> 0
            GridPadPlacementPolicy.Vertical.FROM_BOTTOM -> cells.rowCount - 1
        }
    }

    private fun firstColumn(): Int {
        return when (placementPolicy.horizontal) {
            GridPadPlacementPolicy.Horizontal.FROM_START -> 0
            GridPadPlacementPolicy.Horizontal.FROM_END -> cells.columnCount - 1
        }
    }

    private fun findRow(lastItem: GridPadContent?): Int {
        return lastItem?.top ?: firstRow()
    }

    private fun findNextRow(lastItem: GridPadContent?): Int {
        val lastRow = findRow(lastItem)
        val lastRowSpan = lastItem?.rowSpan ?: 0
        return when (placementPolicy.vertical) {
            GridPadPlacementPolicy.Vertical.FROM_TOP -> lastRow + lastRowSpan
            GridPadPlacementPolicy.Vertical.FROM_BOTTOM -> lastRow - lastRowSpan
        }
    }

    private fun findColumn(lastItem: GridPadContent?): Int {
        return lastItem?.left ?: firstColumn()
    }

    private fun findNextColumn(lastItem: GridPadContent?): Int {
        val lastColumn = findColumn(lastItem)
        val lastColumnSpan = lastItem?.columnSpan ?: 0
        return when (placementPolicy.horizontal) {
            GridPadPlacementPolicy.Horizontal.FROM_START -> lastColumn + lastColumnSpan
            GridPadPlacementPolicy.Horizontal.FROM_END -> lastColumn - lastColumnSpan
        }
    }
}

/**
 * Contains information about exact place in a grid and the placed composable
 */
internal class GridPadContent(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val item: @Composable GridPadItemScope.() -> Unit
) {
    val rowSpan: Int = bottom - top
    val columnSpan: Int = right - left
}

private fun <T : Comparable<T>> ClosedRange<T>.isOutOf(value: T): Boolean {
    return !contains(value)
}

private fun GridPadCells.isRowOutsideOfGrid(row: Int?): Boolean {
    return row != null && (0 until rowCount).isOutOf(row)
}

private fun GridPadCells.isRowOutsideOfGrid(row: Int, rowSpan: Int): Boolean {
    return (0 until rowCount).isOutOf(row) || (0 until rowCount).isOutOf(row + rowSpan - 1)
}

private fun GridPadCells.isColumnOutsideOfGrid(column: Int?): Boolean {
    return column != null && (0 until columnCount).isOutOf(column)
}

private fun GridPadCells.isColumnOutsideOfGrid(column: Int, columnSpan: Int): Boolean {
    return (0 until columnCount).isOutOf(column) || (0 until columnCount).isOutOf(column + columnSpan - 1)
}

private fun GridPadCells.isOutsideOfGrid(row: Int?, column: Int?): Boolean {
    return isRowOutsideOfGrid(row) || isColumnOutsideOfGrid(column)
}