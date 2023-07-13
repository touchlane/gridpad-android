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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
 * @param placementPolicy implicit placement policy
 * @param content content
 */
@Composable
public fun GridPad(
    cells: GridPadCells,
    modifier: Modifier = Modifier,
    placementPolicy: GridPadPlacementPolicy = GridPadPlacementPolicy.DEFAULT,
    content: GridPadScope.() -> Unit
) {
    val scopeContent: GridPadScopeImpl = GridPadScopeImpl(cells, placementPolicy).apply(content)
    val displayContent: ImmutableList<GridPadContent> = scopeContent.data.toImmutableList()
    Layout(modifier = modifier, content = {
        displayContent.forEach {
            it.item(GridPadItemScopeImpl)
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
        val placeables = measurables.measure(displayContent, cellPlaces, constraints)

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
                val contentMetaInfo = displayContent[index]
                val cellPlaceInfo = cellPlaces[contentMetaInfo.top][contentMetaInfo.left]
                placeable.placeRelative(x = cellPlaceInfo.x, y = cellPlaceInfo.y)
            }
        }
    }
}

/**
 * Measure children
 */
private fun List<Measurable>.measure(
    content: ImmutableList<GridPadContent>,
    cellPlaces: Array<Array<CellPlaceInfo>>,
    constraints: Constraints
): List<Placeable> = mapIndexed { index, measurable ->
    val contentMetaInfo = content[index]
    val maxWidth = (contentMetaInfo.left..contentMetaInfo.right).sumOf { column ->
        cellPlaces[contentMetaInfo.top][column].width
    }
    val maxHeight = (contentMetaInfo.top..contentMetaInfo.bottom).sumOf { row ->
        cellPlaces[row][contentMetaInfo.left].height
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

/**
 * Build the grid that will be used in the measurement and placement stage.
 */
private fun MeasureScope.calculateCellPlaces(
    cells: GridPadCells, width: Int, height: Int
): Array<Array<CellPlaceInfo>> {
    val columnWidths = calculateSizesForDimension(width, cells.columnSizes, cells.columnsTotalSize)
    val columnHeights = calculateSizesForDimension(height, cells.rowSizes, cells.rowsTotalSize)

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

/**
 * Calculate sizes in pixels for each row or column.
 *
 * @param availableSize parent size that available for distribution
 * @param cellSizes sizes for rows or columns
 * @param totalSize precalculated total size
 */
private fun MeasureScope.calculateSizesForDimension(
    availableSize: Int, cellSizes: ImmutableList<GridPadCellSize>, totalSize: TotalSize
): List<Int> {
    val availableWeight = availableSize - totalSize.fixed.toPx()
    var reminder = 0f
    return cellSizes.map { cellSize ->
        when (cellSize) {
            is GridPadCellSize.Fixed -> {
                val floatSize = cellSize.size.toPx() + reminder
                val size = floatSize.roundToInt()
                reminder = floatSize - size
                size
            }

            is GridPadCellSize.Weight -> {
                val floatSize = availableWeight * cellSize.size / totalSize.weight + reminder
                val size = floatSize.roundToInt()
                reminder = floatSize - size
                size
            }
        }
    }.toMutableList()
}

/**
 * Stores information about the position and size of the cell in the parent bounds.
 *
 * @param x x position, in pixels
 * @param y y position, in pixels
 * @param width cell width, in pixels
 * @param height cell height, in pixels
 */
private data class CellPlaceInfo(val x: Int, val y: Int, val width: Int, val height: Int)

/**
 * Receiver scope which is used by [GridPad].
 */
@GridPadScopeMarker
public sealed interface GridPadScope {

    /**
     * Explicit placement of content on the grid.
     * It's possible to overlap items if they will have intersected spans or same location.
     *
     * **Be careful: item that is completely or partially out of grid bounds wouldn't be
     * placed and displayed.**
     *
     * @param row row index
     * @param column column index
     * @param rowSpan row span size, must be > 0
     * @param columnSpan column span size, must be > 0
     * @param itemContent the content of the item
     */
    public fun item(
        row: Int,
        column: Int,
        rowSpan: Int = 1,
        columnSpan: Int = 1,
        itemContent: @Composable GridPadItemScope.() -> Unit
    )

    /**
     * Implicit placement of content on the grid.
     * Content will be placed in the next position after the last placed item.
     * The next position depends on the [GridPadPlacementPolicy] value in the [GridPad] method.
     *
     * @see item all limitations from the explicit method are also applicable to this method
     * @see GridPadPlacementPolicy placement policy description
     *
     * @param rowSpan row span size, must be > 0
     * @param columnSpan column span size, must be > 0
     * @param itemContent the content of the item
     */
    public fun item(
        rowSpan: Int = 1,
        columnSpan: Int = 1,
        itemContent: @Composable GridPadItemScope.() -> Unit
    )
}
