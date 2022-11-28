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
     * Rows count. It's guaranteed that [rowCount] will be equal `rowSizes.size`
     */
    val rowCount: Int = rowSizes.size

    /**
     * Columns count. It's guaranteed that [columnCount] will be equal `columnSizes.size`
     */
    val columnCount: Int = columnSizes.size

    /**
     * Calculated total size of all rows.
     */
    internal val rowsTotalSize: TotalSize = rowSizes.calculateTotalSize()

    /**
     * Calculated total size of all columns.
     */
    internal val columnsTotalSize: TotalSize = columnSizes.calculateTotalSize()

    /**
     * @param rowCount grid row count
     * @param columnCount grid column count
     */
    class Builder(rowCount: Int, columnCount: Int) {

        private val rowSizes: MutableList<GridPadCellSize> = GridPadCellSize.weight(rowCount)
        private val columnSizes: MutableList<GridPadCellSize> = GridPadCellSize.weight(columnCount)

        /**
         * Set size for specific row
         *
         * @param index index of row
         * @param size row's size
         */
        fun rowSize(index: Int, size: GridPadCellSize) = apply {
            rowSizes[index] = size
        }

        /**
         * Set size for all rows
         *
         * @param size rows size
         */
        fun rowsSize(size: GridPadCellSize) = apply {
            rowSizes.fill(size)
        }

        /**
         * Set size for specific column
         *
         * @param index index of column
         * @param size column's size
         */
        fun columnSize(index: Int, size: GridPadCellSize) = apply {
            columnSizes[index] = size
        }

        /**
         * Set size for all columns
         *
         * @param size columns size
         */
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
internal data class TotalSize(
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

/**
 * Class describes grid cell size
 */
@Stable
sealed class GridPadCellSize {

    /**
     * Fixed grid cell size in Dp.
     *
     * @param size size in Dp, should be greater than 0
     */
    data class Fixed(val size: Dp) : GridPadCellSize() {
        init {
            check(size.value > 0) { "size have to be > 0" }
        }
    }

    /**
     * Weight grid cell size.
     *
     * @param size size, should be greater than 0
     */
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