package com.touchlane.gridpad

import androidx.compose.runtime.Stable
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Stable
data class GridPadCells(
    val rowSizes: Array<GridPadCellSize>, val columnSizes: Array<GridPadCellSize>
) {
    val rows: Int = rowSizes.size
    val columns: Int = columnSizes.size
    val rowsTotalSize: TotalSize = rowSizes.calculateTotalSize()
    val columnsTotalSize: TotalSize = columnSizes.calculateTotalSize()

    class Builder(rows: Int, columns: Int) {

        private val rowSizes: Array<GridPadCellSize> = Array(rows) {
            GridPadCellSize.Weight()
        }

        private val columnSizes: Array<GridPadCellSize> = Array(columns) {
            GridPadCellSize.Weight()
        }

        fun rowSize(index: Int, size: GridPadCellSize) = apply {
            rowSizes[index] = size
        }

        fun rowsSize(size: GridPadCellSize) = apply {
            rowSizes.fill(size)
        }

        fun columnSize(index: Int, size: GridPadCellSize) = apply {
            columnSizes[index] = size
        }

        fun columnsSize(size: GridPadCellSize) = apply {
            columnSizes.fill(size)
        }

        fun build(): GridPadCells {
            return GridPadCells(
                rowSizes = rowSizes, columnSizes = columnSizes
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GridPadCells

        if (rows != other.rows) return false
        if (columns != other.columns) return false
        if (!rowSizes.contentEquals(other.rowSizes)) return false
        if (!columnSizes.contentEquals(other.columnSizes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + rowSizes.contentHashCode()
        result = 31 * result + columnSizes.contentHashCode()
        return result
    }
}

fun MeasureScope.calculateCellPlaces(
    cells: GridPadCells,
    width: Int,
    height: Int
): Array<Array<CellPlaceInfo>> {
    val availableWeightWidth = width - cells.columnsTotalSize.fixed.roundToPx()
    val availableWeightHeight = height - cells.rowsTotalSize.fixed.roundToPx()

    //Calculate real cell widths
    val columnWidths = cells.columnSizes.map { columnSize ->
        when (columnSize) {
            is GridPadCellSize.Fixed -> columnSize.size.roundToPx()
            is GridPadCellSize.Weight -> (availableWeightWidth * columnSize.size / cells.columnsTotalSize.weight).roundToInt()
        }
    }

    //Calculate real cell height
    val columnHeights = cells.rowSizes.map { rowSize ->
        when (rowSize) {
            is GridPadCellSize.Fixed -> rowSize.size.roundToPx()
            is GridPadCellSize.Weight -> (availableWeightHeight * rowSize.size / cells.rowsTotalSize.weight).roundToInt()
        }
    }

    //Calculate grid with positions and cell sizes
    var y = 0f
    val cellPlaces = columnHeights.map { columnHeight ->
        var x = 0f
        val cellY = y
        y += columnHeight
        columnWidths.map { columnWidth ->
            val cellX = x
            x += columnWidth
            CellPlaceInfo(
                x = cellX.roundToInt(),
                y = cellY.roundToInt(),
                width = columnWidth,
                height = columnHeight
            )
        }
    }
    return cellPlaces.map { it.toTypedArray() }.toTypedArray()
}

private fun Array<GridPadCellSize>.calculateTotalSize(): TotalSize {
    var totalWeightSize = 0f
    var totalFixedSize = 0f.dp
    forEach {
        when (it) {
            is GridPadCellSize.Weight -> totalWeightSize += it.size
            is GridPadCellSize.Fixed -> totalFixedSize += it.size
        }
    }
    return TotalSize(weight = totalWeightSize, fixed = totalFixedSize)
}

data class TotalSize(val weight: Float, val fixed: Dp)

data class CellPlaceInfo(val x: Int, val y: Int, val width: Int, val height: Int)

@Stable
sealed class GridPadCellSize {

    data class Fixed(val size: Dp) : GridPadCellSize() {
        init {
            check(size.value > 0) { "size have to be > 0" }
        }
    }

    data class Weight(val size: Float = 1f) : GridPadCellSize() {
        init {
            check(size > 0) { "size have to be > 0" }
        }
    }
}