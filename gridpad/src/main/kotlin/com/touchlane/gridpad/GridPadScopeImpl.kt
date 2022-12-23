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
        row: Int,
        column: Int,
        rowSpan: Int,
        columnSpan: Int,
        itemContent: GridPadItemScope.() -> Unit
    ) {
        placeImplicitly(
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

    override fun item(
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
        placeImplicitly(
            row = null,
            column = null,
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

    private fun placeImplicitly(
        row: Int?,
        column: Int?,
        rowSpan: Int,
        columnSpan: Int,
        callback: (top: Int, left: Int, right: Int, bottom: Int) -> Unit
    ): Boolean {
        val anchor = placementPolicy.anchor
        //Skipping displaying items that out of grid
        if (cells.isOutsideOfGrid(row = row, column = column)) {
            onSkipped(row, column, rowSpan, columnSpan)
            return false
        }

        val lastItem = data.lastOrNull()
        var cellRow: Int
        var cellColumn: Int
        if (placementPolicy.mainAxis == GridPadPlacementPolicy.MainAxis.HORIZONTAL) {
            cellRow = row ?: findRow(lastItem)
            cellColumn = column ?: findNextColumn(lastItem)
            if (cells.isColumnOutsideOfGrid(cellColumn, columnSpan, anchor)) {
                cellColumn = cells.firstColumn(placementPolicy)
                cellRow = findNextRow(lastItem)
            }
        } else {
            cellColumn = column ?: findColumn(lastItem)
            cellRow = row ?: findNextRow(lastItem)
            if (cells.isRowOutsideOfGrid(cellRow, rowSpan, anchor)) {
                cellRow = cells.firstRow(placementPolicy)
                cellColumn = findNextColumn(lastItem)
            }
        }
        val rowOutside = cells.isRowOutsideOfGrid(cellRow, rowSpan, anchor)
        val columnOutside = cells.isColumnOutsideOfGrid(cellColumn, columnSpan, anchor)
        return if (rowOutside || columnOutside) {
            onSkipped(cellRow, cellColumn, rowSpan, columnSpan)
            false
        } else {
            callback(
                anchor.topBound(cellRow, rowSpan),
                anchor.leftBound(cellColumn, columnSpan),
                anchor.rightBound(cellColumn, columnSpan),
                anchor.bottomBound(cellRow, rowSpan)
            )
            true
        }
    }

    private fun onSkipped(row: Int?, column: Int?, rowSpan: Int, columnSpan: Int) {
        GridPadDiagnosticLogger.onItemSkipped {
            "Skipped, row: $row, column: $column, rowSpan: $rowSpan, columnSpan: $columnSpan\nGrid: [${cells.rowCount}x${cells.columnCount}]"
        }
    }

    private fun findRow(lastItem: GridPadContent?): Int {
        return if (lastItem != null) {
            when (placementPolicy.vertical) {
                GridPadPlacementPolicy.Vertical.FROM_TOP -> lastItem.top
                GridPadPlacementPolicy.Vertical.FROM_BOTTOM -> lastItem.bottom
            }
        } else {
            cells.firstRow(placementPolicy)
        }
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
        return if (lastItem != null) {
            when (placementPolicy.horizontal) {
                GridPadPlacementPolicy.Horizontal.FROM_START -> lastItem.left
                GridPadPlacementPolicy.Horizontal.FROM_END -> lastItem.right
            }
        } else {
            cells.firstColumn(placementPolicy)
        }
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
    val rowSpan: Int = bottom - top + 1
    val columnSpan: Int = right - left + 1
}

private fun <T : Comparable<T>> ClosedRange<T>.isOutOf(value: T): Boolean {
    return !contains(value)
}

private fun GridPadCells.firstRow(placementPolicy: GridPadPlacementPolicy): Int {
    return when (placementPolicy.vertical) {
        GridPadPlacementPolicy.Vertical.FROM_TOP -> 0
        GridPadPlacementPolicy.Vertical.FROM_BOTTOM -> rowCount - 1
    }
}

private fun GridPadCells.firstColumn(placementPolicy: GridPadPlacementPolicy): Int {
    return when (placementPolicy.horizontal) {
        GridPadPlacementPolicy.Horizontal.FROM_START -> 0
        GridPadPlacementPolicy.Horizontal.FROM_END -> columnCount - 1
    }
}

private fun GridPadCells.isRowOutsideOfGrid(row: Int?): Boolean {
    return row != null && (0 until rowCount).isOutOf(row)
}

private fun GridPadCells.isRowOutsideOfGrid(
    row: Int,
    rowSpan: Int,
    anchor: GridPadSpanAnchor
): Boolean {
    val top = anchor.topBound(row, rowSpan)
    val bottom = anchor.bottomBound(row, rowSpan)
    return (0 until rowCount).isOutOf(top) || (0 until rowCount).isOutOf(bottom)
}

private fun GridPadCells.isColumnOutsideOfGrid(column: Int?): Boolean {
    return column != null && (0 until columnCount).isOutOf(column)
}

private fun GridPadCells.isColumnOutsideOfGrid(
    column: Int,
    columnSpan: Int,
    anchor: GridPadSpanAnchor
): Boolean {
    val left = anchor.leftBound(column, columnSpan)
    val right = anchor.rightBound(column, columnSpan)
    return (0 until columnCount).isOutOf(left) || (0 until columnCount).isOutOf(right)
}

private fun GridPadCells.isOutsideOfGrid(row: Int?, column: Int?): Boolean {
    return isRowOutsideOfGrid(row) || isColumnOutsideOfGrid(column)
}

private fun GridPadSpanAnchor.leftBound(column: Int, span: Int): Int {
    return when (horizontal) {
        GridPadSpanAnchor.Horizontal.START -> column
        GridPadSpanAnchor.Horizontal.END -> column - span + 1
    }
}

private fun GridPadSpanAnchor.rightBound(column: Int, span: Int): Int {
    return when (this.horizontal) {
        GridPadSpanAnchor.Horizontal.START -> column + span - 1
        GridPadSpanAnchor.Horizontal.END -> column
    }
}

private fun GridPadSpanAnchor.topBound(row: Int, span: Int): Int {
    return when (this.vertical) {
        GridPadSpanAnchor.Vertical.TOP -> row
        GridPadSpanAnchor.Vertical.BOTTOM -> row - span + 1
    }
}

private fun GridPadSpanAnchor.bottomBound(row: Int, span: Int): Int {
    return when (this.vertical) {
        GridPadSpanAnchor.Vertical.TOP -> row + span - 1
        GridPadSpanAnchor.Vertical.BOTTOM -> row
    }
}