package com.touchlane.gridpad

import androidx.compose.runtime.Composable
import kotlin.math.min

internal class GridPadScopeImpl(private val cells: GridPadCells) : GridPadScope {

    internal val data: MutableList<GridPadContent> = mutableListOf()

    override fun item(
        row: Int?,
        column: Int?,
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable () -> Unit
    ) {
        if (row != null && (0 until cells.rows).isOutOf(row)) {
            throw IndexOutOfBoundsException("`row` out of range [0..${cells.rows - 1}]")
        }
        if (column != null && (0 until cells.columns).isOutOf(column)) {
            throw IndexOutOfBoundsException("`column` out of range [0..${cells.columns - 1}]")
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
        if ((0 until cells.rows).isOutOf(cellRow) || (0 until cells.columns).isOutOf(cellColumn)) {
            throw IndexOutOfBoundsException("Specify `row` and `column` explicitly")
        }
        this.data.add(
            GridPadContent(
                row = cellRow,
                column = cellColumn,
                //limit span to avoid out of bounds
                rowSpan = min(rowSpan, cells.rows - cellRow),
                //limit span to avoid out of bounds
                columnSpan = min(columnSpan, cells.columns - cellColumn),
                item = { itemContent() }
            )
        )
    }
}

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