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
        itemContent: @Composable GridPadItemScope.() -> Unit
    ) {
        checkSpan(rowSpan = rowSpan, columnSpan = columnSpan)
        placeExplicitly(
            row = row,
            column = column,
            rowSpan = rowSpan,
            columnSpan = columnSpan,
            itemContent = itemContent
        )
    }

    override fun item(
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable GridPadItemScope.() -> Unit
    ) {
        checkSpan(rowSpan = rowSpan, columnSpan = columnSpan)
        placeImplicitly(
            rowSpan = rowSpan,
            columnSpan = columnSpan,
            itemContent = itemContent
        )
    }

    private fun checkSpan(rowSpan: Int, columnSpan: Int) {
        check(rowSpan > 0) {
            "`rowSpan` must be > 0"
        }
        check(columnSpan > 0) {
            "`columnSpan` must be > 0"
        }
    }

    private fun placeImplicitly(
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable GridPadItemScope.() -> Unit
    ) {
        val anchor = placementPolicy.anchor
        val lastItem = data.lastOrNull()
        var row: Int
        var column: Int
        if (placementPolicy.mainAxis == GridPadPlacementPolicy.MainAxis.HORIZONTAL) {
            row = findCurrentRow(cells, placementPolicy, lastItem)
            column = findNextColumn(cells, placementPolicy, lastItem)
            if (cells.isColumnOutsideOfGrid(column, columnSpan, anchor)) {
                column = cells.firstColumn(placementPolicy)
                row = findNextRow(cells, placementPolicy, lastItem)
            }
        } else {
            column = findCurrentColumn(cells, placementPolicy, lastItem)
            row = findNextRow(cells, placementPolicy, lastItem)
            if (cells.isRowOutsideOfGrid(row, rowSpan, anchor)) {
                row = cells.firstRow(placementPolicy)
                column = findNextColumn(cells, placementPolicy, lastItem)
            }
        }
        placeExplicitly(
            row = row,
            column = column,
            rowSpan = rowSpan,
            columnSpan = columnSpan,
            itemContent = itemContent
        )
    }

    private fun placeExplicitly(
        row: Int,
        column: Int,
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable GridPadItemScope.() -> Unit
    ) {
        val anchor = placementPolicy.anchor
        val rowOutside = cells.isRowOutsideOfGrid(row, rowSpan, anchor)
        val columnOutside = cells.isColumnOutsideOfGrid(column, columnSpan, anchor)
        if (rowOutside || columnOutside) {
            onSkipped(row, column, rowSpan, columnSpan)
        } else {
            onPlaced(
                left = anchor.leftBound(column, columnSpan),
                top = anchor.topBound(row, rowSpan),
                right = anchor.rightBound(column, columnSpan),
                bottom = anchor.bottomBound(row, rowSpan),
                itemContent = itemContent
            )
        }
    }

    private fun onSkipped(row: Int?, column: Int?, rowSpan: Int, columnSpan: Int) {
        GridPadDiagnosticLogger.onItemSkipped {
            """
                Skipped position: [${row}x$column], span size: [${rowSpan}x$columnSpan]
                Grid size: [${cells.rowCount}x${cells.columnCount}]
            """.trimIndent()
        }
    }

    private fun onPlaced(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        itemContent: @Composable GridPadItemScope.() -> Unit
    ) {
        data.add(
            GridPadContent(
                top = top,
                left = left,
                right = right,
                bottom = bottom,
                item = { itemContent() }
            )
        )
    }

    /**
     * Implicit search for the index of current row.
     *
     * @param cells grid cells
     * @param placementPolicy implicit placement policy
     * @param lastItem last placed item
     *
     * @return current row index
     */
    private fun findCurrentRow(
        cells: GridPadCells,
        placementPolicy: GridPadPlacementPolicy,
        lastItem: GridPadContent?
    ): Int {
        return if (lastItem != null) {
            when (placementPolicy.verticalDirection) {
                GridPadPlacementPolicy.VerticalDirection.TOP_BOTTOM -> lastItem.top
                GridPadPlacementPolicy.VerticalDirection.BOTTOM_TOP -> lastItem.bottom
            }
        } else {
            cells.firstRow(placementPolicy)
        }
    }

    /**
     * Implicit search for the index of next row.
     *
     * @param cells grid cells
     * @param placementPolicy implicit placement policy
     * @param lastItem last placed item
     *
     * @return next row index
     */
    private fun findNextRow(
        cells: GridPadCells,
        placementPolicy: GridPadPlacementPolicy,
        lastItem: GridPadContent?
    ): Int {
        val lastRow = findCurrentRow(cells, placementPolicy, lastItem)
        val lastRowSpan = lastItem?.rowSpan ?: 0
        return when (placementPolicy.verticalDirection) {
            GridPadPlacementPolicy.VerticalDirection.TOP_BOTTOM -> lastRow + lastRowSpan
            GridPadPlacementPolicy.VerticalDirection.BOTTOM_TOP -> lastRow - lastRowSpan
        }
    }

    /**
     * Implicit search for the index of current column.
     *
     * @param cells grid cells
     * @param placementPolicy implicit placement policy
     * @param lastItem last placed item
     *
     * @return current column index
     */
    private fun findCurrentColumn(
        cells: GridPadCells,
        placementPolicy: GridPadPlacementPolicy,
        lastItem: GridPadContent?
    ): Int {
        return if (lastItem != null) {
            when (placementPolicy.horizontalDirection) {
                GridPadPlacementPolicy.HorizontalDirection.START_END -> lastItem.left
                GridPadPlacementPolicy.HorizontalDirection.END_START -> lastItem.right
            }
        } else {
            cells.firstColumn(placementPolicy)
        }
    }

    /**
     * Implicit search for the index of next column.
     *
     * @param cells grid cells
     * @param placementPolicy implicit placement policy
     * @param lastItem last placed item
     *
     * @return next column index
     */
    private fun findNextColumn(
        cells: GridPadCells,
        placementPolicy: GridPadPlacementPolicy,
        lastItem: GridPadContent?
    ): Int {
        val lastColumn = findCurrentColumn(cells, placementPolicy, lastItem)
        val lastColumnSpan = lastItem?.columnSpan ?: 0
        return when (placementPolicy.horizontalDirection) {
            GridPadPlacementPolicy.HorizontalDirection.START_END -> lastColumn + lastColumnSpan
            GridPadPlacementPolicy.HorizontalDirection.END_START -> lastColumn - lastColumnSpan
        }
    }
}

/**
 * Contains information about exact place in a grid and the placed composable
 *
 * @param left left column index in the grid
 * @param top top row index in the grid
 * @param right right column index in the grid
 * @param bottom bottom row index in the grid
 */
internal class GridPadContent(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val item: @Composable GridPadItemScope.() -> Unit
) {
    /**
     * How many rows the content [item] occupies.
     */
    val rowSpan: Int = bottom - top + 1

    /**
     * How many columns the content [item] occupies.
     */
    val columnSpan: Int = right - left + 1
}

private fun <T : Comparable<T>> ClosedRange<T>.isOutOf(value: T): Boolean {
    return !contains(value)
}

/**
 * Returns the first row index for a specific grid, depends on the placement policy.
 *
 * @param placementPolicy placement policy
 *
 * @return index of first row, always 0 or [GridPadCells.rowCount] - 1
 */
private fun GridPadCells.firstRow(placementPolicy: GridPadPlacementPolicy): Int {
    return when (placementPolicy.verticalDirection) {
        GridPadPlacementPolicy.VerticalDirection.TOP_BOTTOM -> 0
        GridPadPlacementPolicy.VerticalDirection.BOTTOM_TOP -> rowCount - 1
    }
}

/**
 * Returns the first column index for a specific grid, depends on the placement policy.
 *
 * @param placementPolicy placement policy
 *
 * @return index of first column, always 0 or [GridPadCells.columnCount] - 1
 */
private fun GridPadCells.firstColumn(placementPolicy: GridPadPlacementPolicy): Int {
    return when (placementPolicy.horizontalDirection) {
        GridPadPlacementPolicy.HorizontalDirection.START_END -> 0
        GridPadPlacementPolicy.HorizontalDirection.END_START -> columnCount - 1
    }
}

/**
 * Checks if the row with the span and specific anchor is outside the defined grid.
 *
 * @param row row
 * @param rowSpan span
 * @param anchor anchor
 *
 * @return `true` if outside, `false` otherwise
 */
private fun GridPadCells.isRowOutsideOfGrid(
    row: Int,
    rowSpan: Int,
    anchor: GridPadSpanAnchor
): Boolean {
    val top = anchor.topBound(row, rowSpan)
    val bottom = anchor.bottomBound(row, rowSpan)
    return (0 until rowCount).isOutOf(top) || (0 until rowCount).isOutOf(bottom)
}

/**
 * Checks if the column with the span and specific anchor is outside the defined grid.
 *
 * @param column column
 * @param columnSpan span
 * @param anchor anchor
 *
 * @return `true` if outside, `false` otherwise
 */
private fun GridPadCells.isColumnOutsideOfGrid(
    column: Int,
    columnSpan: Int,
    anchor: GridPadSpanAnchor
): Boolean {
    val left = anchor.leftBound(column, columnSpan)
    val right = anchor.rightBound(column, columnSpan)
    return (0 until columnCount).isOutOf(left) || (0 until columnCount).isOutOf(right)
}

/**
 * Left column index for a specific column and span base on the caller anchor.
 *
 * @param column column
 * @param span span
 *
 * @return left column index
 */
private fun GridPadSpanAnchor.leftBound(column: Int, span: Int): Int {
    return when (horizontal) {
        GridPadSpanAnchor.Horizontal.START -> column
        GridPadSpanAnchor.Horizontal.END -> column - span + 1
    }
}

/**
 * Right column index for a specific column and span base on the caller anchor.
 *
 * @param column column
 * @param span span
 *
 * @return right column index
 */
private fun GridPadSpanAnchor.rightBound(column: Int, span: Int): Int {
    return when (this.horizontal) {
        GridPadSpanAnchor.Horizontal.START -> column + span - 1
        GridPadSpanAnchor.Horizontal.END -> column
    }
}

/**
 * Top row index for a specific row and span base on the caller anchor.
 *
 * @param row row
 * @param span span
 *
 * @return top row index
 */
private fun GridPadSpanAnchor.topBound(row: Int, span: Int): Int {
    return when (this.vertical) {
        GridPadSpanAnchor.Vertical.TOP -> row
        GridPadSpanAnchor.Vertical.BOTTOM -> row - span + 1
    }
}

/**
 * Bottom row index for a specific row and span base on the caller anchor.
 *
 * @param row row
 * @param span span
 *
 * @return bottom row index
 */
private fun GridPadSpanAnchor.bottomBound(row: Int, span: Int): Int {
    return when (this.vertical) {
        GridPadSpanAnchor.Vertical.TOP -> row + span - 1
        GridPadSpanAnchor.Vertical.BOTTOM -> row
    }
}
