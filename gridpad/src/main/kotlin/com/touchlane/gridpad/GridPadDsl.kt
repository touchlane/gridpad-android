package com.touchlane.gridpad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

/**
 * Layout that allows place elements in a grid defined through [cells] parameter.
 * For each item can be specified:
 * - location in a grid (row and column)
 * - span size (rowSpan and columnSpan)
 * Each row and column can have specific (fixed) or relative (weight) size.
 *
 * @param cells grid specification
 * @param modifier container modifier
 * @param content content
 */
@Composable
fun GridPad(
    cells: GridPadCells, modifier: Modifier = Modifier, content: GridPadScope.() -> Unit
) {
    val scopeContent: GridPadScopeImpl = GridPadScopeImpl(cells).apply(content)
    Layout(modifier = modifier, content = {
        scopeContent.data.forEach {
            it.item()
        }
    }) { measurables, constraints ->
        check(constraints.maxWidth != Constraints.Infinity) {
            "Infinity width not allowed in GridPad"
        }
        check(constraints.maxHeight != Constraints.Infinity) {
            "Infinity height not allowed in GridPad"
        }
        val cellPlaces =
            calculateCellPlaces(cells, width = constraints.maxWidth, height = constraints.maxHeight)
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->
            val row = index / cells.columns
            val col = index % cells.columns
            val cellPlaceInfo = cellPlaces[row][col]

            // Measure each children
            measurable.measure(
                constraints.copy(
                    maxWidth = cellPlaceInfo.width, maxHeight = cellPlaceInfo.height
                )
            )
        }

        // Set the size of the layout as big as it can
        //TODO change to actual size in cases when all columns and rows are fixed
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val row = index / cells.columns
                val col = index % cells.columns
                val cellPlaceInfo = cellPlaces[row][col]
                placeable.placeRelative(x = cellPlaceInfo.x, y = cellPlaceInfo.y)
            }
        }
    }
}

private fun MeasureScope.calculateCellPlaces(
    cells: GridPadCells, width: Int, height: Int
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

private data class CellPlaceInfo(val x: Int, val y: Int, val width: Int, val height: Int)

/**
 * Receiver scope which is used by [GridPad].
 */
sealed interface GridPadScope {

    /**
     * Adds a single item to the scope.
     * It's possible to overlap items if they will have intersected spans or same location.
     *
     * @param row row index, must be in [0..[GridPadCells.rows]] range
     * @param column column index, must be in [0..[GridPadCells.columns]] range
     * @param rowSpan row span size, default value is 1. In cases, when item potentially
     * goes out of the grid, value will be trimmed to fit grid
     * @param columnSpan column span size, default value is 1. In cases, when item potentially
     * goes out of the grid, value will be trimmed to fit grid
     * @param itemContent the content of the item
     */
    fun item(
        row: Int? = null,
        column: Int? = null,
        rowSpan: Int = 1,
        columnSpan: Int = 1,
        itemContent: @Composable () -> Unit
    )
}