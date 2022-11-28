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
internal class GridPadScopeImpl(private val cells: GridPadCells) : GridPadScope {

    /**
     * Display list with locations in a grid
     */
    internal val data: MutableList<GridPadContent> = mutableListOf()

    override fun item(
        row: Int?,
        column: Int?,
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable () -> Unit
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
        ) { cellRow, cellColumn ->
            this.data.add(
                GridPadContent(
                    row = cellRow,
                    column = cellColumn,
                    rowSpan = rowSpan,
                    columnSpan = columnSpan,
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
        callback: (row: Int, column: Int) -> Unit
    ): Boolean {
        //Skipping displaying items that out of grid
        if (row != null && (0 until cells.rows).isOutOf(row)) {
            return false
        }
        if (column != null && (0 until cells.columns).isOutOf(column)) {
            return false
        }

        val lastItem = data.lastOrNull()
        val lastRowSpan = lastItem?.rowSpan ?: 0
        val lastColumnSpan = lastItem?.columnSpan ?: 0
        // detect actual column for item
        var cellColumn = column ?: run {
            val lastColumn = lastItem?.column ?: 0
            lastColumn + lastColumnSpan
        }
        // detect actual row for item
        var cellRow = row ?: run {
            val lastRow = lastItem?.row ?: 0
            lastRow
        }
        if (cellColumn >= cells.columns) {
            cellColumn = 0
            cellRow += lastRowSpan
        }
        val rowPlacedOutside = (0 until cells.rows).isOutOf(cellRow)
        val columnPlacedOutside = (0 until cells.columns).isOutOf(cellColumn)
        val rowSpanOutside = cells.rows - cellRow < rowSpan
        val columnSpanOutside = cells.columns - cellColumn < columnSpan
        //Skipping displaying items that out of grid
        return if (rowPlacedOutside || columnPlacedOutside || rowSpanOutside || columnSpanOutside) {
            false
        } else {
            callback(cellRow, cellColumn)
            true
        }
    }
}

/**
 * Contains information about exact place in a grid and the placed composable
 */
internal class GridPadContent(
    val row: Int,
    val column: Int,
    val rowSpan: Int,
    val columnSpan: Int,
    val item: @Composable () -> Unit
)

private fun <T : Comparable<T>> ClosedRange<T>.isOutOf(value: T): Boolean {
    return !contains(value)
}