package com.touchlane.gridpad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

@Composable
fun GridPad(
    cells: GridPadCells,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
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
                    maxWidth = cellPlaceInfo.width,
                    maxHeight = cellPlaceInfo.height
                )
            )
        }

        // Set the size of the layout as big as it can
        //TODO change to actual size
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

/**
 * Receiver scope which is used by [GridPad].
 */
sealed interface GridPadScope {
    /**
     * Adds a single item to the scope.
     *
     * @param content the content of the item
     */
    fun item(
        content: @Composable GridPadItemScope.() -> Unit
    )
}