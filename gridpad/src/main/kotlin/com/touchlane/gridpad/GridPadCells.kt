package com.touchlane.gridpad

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.toImmutableList

/**
 * Non-modifiable class that store information about grid: rows and columns count, size information.
 */
@Stable
data class GridPadCells(
    /**
     * Contains information about size of each row
     */
    val rowSizes: ImmutableCollection<GridPadCellSize>,
    /**
     * Contains information about size of each column
     */
    val columnSizes: ImmutableCollection<GridPadCellSize>
) {
    constructor(
        rowSizes: Iterable<GridPadCellSize>, columnSizes: Iterable<GridPadCellSize>
    ) : this(rowSizes = rowSizes.toImmutableList(), columnSizes = columnSizes.toImmutableList())

    /**
     * Creating a grid with [GridPadCellSize.Weight] sizes
     * where [GridPadCellSize.Weight.size] equal 1.
     */
    constructor(rows: Int, columns: Int) : this(
        rowSizes = GridPadCellSize.weight(rows),
        columnSizes = GridPadCellSize.weight(columns)
    )

    /**
     * Rows count. It's guaranteed that [rows] will be equal `rowSizes.size`
     */
    val rows: Int = rowSizes.size

    /**
     * Columns count. It's guaranteed that [columns] will be equal `columnSizes.size`
     */
    val columns: Int = columnSizes.size

    /**
     * Calculated total size of all rows.
     */
    val rowsTotalSize: TotalSize = rowSizes.calculateTotalSize()

    /**
     * Calculated total size of all columns.
     */
    val columnsTotalSize: TotalSize = columnSizes.calculateTotalSize()

    class Builder(rows: Int, columns: Int) {

        private val rowSizes: MutableList<GridPadCellSize> = GridPadCellSize.weight(rows)
        private val columnSizes: MutableList<GridPadCellSize> = GridPadCellSize.weight(columns)

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
}

private fun Iterable<GridPadCellSize>.calculateTotalSize(): TotalSize {
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

/**
 * Total size for rows or columns information.
 */
data class TotalSize(
    /**
     * Total weight for all rows or columns.
     * Can be 0 in cases where all rows or columns have [GridPadCellSize.Fixed] size.
     */
    val weight: Float,

    /**
     * Total size for all rows or columns.
     * Can be 0 in cases where all rows or columns have [GridPadCellSize.Weight] size.
     */
    val fixed: Dp
)

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

    companion object
}

fun GridPadCellSize.Companion.fixed(count: Int, size: Dp): MutableList<GridPadCellSize> {
    return mutableListOfElement(count, GridPadCellSize.Fixed(size = size))
}

fun GridPadCellSize.Companion.fixed(sizes: Array<Dp>): MutableList<GridPadCellSize> {
    return sizes.map { GridPadCellSize.Fixed(size = it) }.toMutableList()
}

fun GridPadCellSize.Companion.weight(count: Int, size: Float = 1f): MutableList<GridPadCellSize> {
    return mutableListOfElement(count, GridPadCellSize.Weight(size = size))
}

fun GridPadCellSize.Companion.weight(sizes: FloatArray): MutableList<GridPadCellSize> {
    return sizes.map { GridPadCellSize.Weight(size = it) }.toMutableList()
}

private fun <T> mutableListOfElement(size: Int, fillElement: T): MutableList<T> {
    return (0 until size).map { fillElement }.toMutableList()
}