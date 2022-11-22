package com.touchlane.gridpad

import androidx.compose.runtime.Composable

internal class GridPadScopeImpl(private val cells: GridPadCells) : GridPadScope {

    internal val data: MutableList<GridPadContent> = mutableListOf()

    override fun item(
        row: Int?,
        column: Int?,
        rowSpan: Int,
        columnSpan: Int,
        itemContent: @Composable () -> Unit
    ) {
        check(row == null || row >= 0 && row < cells.rows) {
            "`row` out of bounds [0..${cells.rows - 1}]"
        }
        check(column == null || column >= 0 && column < cells.columns) {
            "`column` out of bounds [0..${cells.columns - 1}]"
        }
        val lastItem = data.lastOrNull()
        val lastRow = lastItem?.row ?: 0
        val lastColumn = lastItem?.column ?: -1
        val lastRowSpan = lastItem?.rowSpan ?: 1
        val lastColumnSpan = lastItem?.columnSpan ?: 1
        var cellColumn = lastColumn + lastColumnSpan
        var cellRow = lastRow
        if (cellColumn >= cells.columns) {
            cellColumn = 0
            cellRow += lastRowSpan
        }
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

internal class GridPadContent(
    val row: Int,
    val column: Int,
    val rowSpan: Int,
    val columnSpan: Int,
    val item: @Composable () -> Unit
)