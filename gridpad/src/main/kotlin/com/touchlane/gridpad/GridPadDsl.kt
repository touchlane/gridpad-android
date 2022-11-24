package com.touchlane.gridpad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Layout that allows place elements in a grid defined through [cells] parameter.
 * For each item can be specified:
 * - location in a grid (row and column)
 * - span size (rowSpan and columnSpan)
 * Each row and column can have specific (fixed) or relative (weight) size.
 *
 * **Layout have to be limited on both sides (width and height) otherwise an
 * exception will be thrown.**
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
            val contentMetaInfo = scopeContent.data[index]
            val maxWidth = (0 until contentMetaInfo.columnSpan).sumOf {
                cellPlaces[contentMetaInfo.row][contentMetaInfo.column + it].width
            }
            val maxHeight = (0 until contentMetaInfo.rowSpan).sumOf {
                cellPlaces[contentMetaInfo.row + it][contentMetaInfo.column].height
            }

            // Measure each children
            measurable.measure(
                constraints.copy(
                    minWidth = min(constraints.minWidth, maxWidth),
                    maxWidth = maxWidth,
                    minHeight = min(constraints.minHeight, maxHeight),
                    maxHeight = maxHeight
                )
            )
        }

        //in cases when all columns have a fixed size, we limit layout width to the sum of them
        val layoutWidth = if (cells.columnsTotalSize.weight == 0f) {
            min(constraints.maxWidth, cells.columnsTotalSize.fixed.roundToPx())
        } else {
            constraints.maxWidth
        }

        //in cases when all rows have a fixed size, we limit layout height to the sum of them
        val layoutHeight = if (cells.rowsTotalSize.weight == 0f) {
            min(constraints.maxHeight, cells.rowsTotalSize.fixed.roundToPx())
        } else {
            constraints.maxHeight
        }

        //place items
        layout(layoutWidth, layoutHeight) {
            placeables.forEachIndexed { index, placeable ->
                val contentMetaInfo = scopeContent.data[index]
                val cellPlaceInfo = cellPlaces[contentMetaInfo.row][contentMetaInfo.column]
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

    //Calculate real cell heights
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
@GridPadScopeMarker
sealed interface GridPadScope {

    /**
     * Adds a single item to the scope.
     * It's possible to overlap items if they will have intersected spans or same location.
     * If [row] is null then content will be placed in a next after last placed item's column,
     * the same logic will work for cases when [column] is null - content will be placed in a next
     * after last placed item's column. When column reach last one - row value will be increased.
     *
     * **Be careful: item that is completely or partially out of grid bounds wouldn't be displayed.**
     *
     * @param row row index
     * @param column column index
     * @param rowSpan row span size
     * @param columnSpan column span size
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