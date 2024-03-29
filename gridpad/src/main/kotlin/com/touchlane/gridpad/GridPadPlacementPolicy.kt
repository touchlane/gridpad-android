/*
 * MIT License
 *
 * Copyright (c) 2023 Touchlane LLC tech@touchlane.com
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

/**
 * Implicit placement policy for items.
 * There are two major types of settings here: the main axis of placement presented by the [mainAxis]
 * property and the direction of placement presented by [horizontalDirection] and [verticalDirection] properties.
 *
 * The [mainAxis] property describes which axis would be used to find the next position.
 * For example, [MainAxis.HORIZONTAL] means that firstly next position will look in a current row
 * and if there isn't a place for the item algorithm will move to the next row.
 *
 * The [horizontalDirection] property describes the direction for choosing the next item on the
 * horizontal axis: left or right side, depending on LTR or RTL settings.
 *
 * The [verticalDirection] property describes the direction for choosing the next item on the vertical
 * axis: above or below.
 *
 * @param mainAxis the main axis for selecting the next location
 * @param horizontalDirection horizontal placement policy
 * @param verticalDirection vertical placement policy
 */
public data class GridPadPlacementPolicy(
    val mainAxis: MainAxis = MainAxis.HORIZONTAL,
    val horizontalDirection: HorizontalDirection = HorizontalDirection.START_END,
    val verticalDirection: VerticalDirection = VerticalDirection.TOP_BOTTOM
) {
    /**
     * Anchor for spanned cells
     */
    internal val anchor: GridPadSpanAnchor = run {
        val horizontalDirection = when (this.horizontalDirection) {
            HorizontalDirection.START_END -> GridPadSpanAnchor.Horizontal.START
            HorizontalDirection.END_START -> GridPadSpanAnchor.Horizontal.END
        }
        val verticalDirection = when (this.verticalDirection) {
            VerticalDirection.TOP_BOTTOM -> GridPadSpanAnchor.Vertical.TOP
            VerticalDirection.BOTTOM_TOP -> GridPadSpanAnchor.Vertical.BOTTOM
        }
        GridPadSpanAnchor(horizontalDirection, verticalDirection)
    }

    /**
     * Main axis of placement
     */
    public enum class MainAxis {
        HORIZONTAL, VERTICAL
    }

    /**
     * Horizontal placement policy
     */
    public enum class HorizontalDirection {
        START_END, END_START
    }

    /**
     * Vertical placement policy
     */
    public enum class VerticalDirection {
        TOP_BOTTOM, BOTTOM_TOP
    }

    public companion object {
        public val DEFAULT: GridPadPlacementPolicy = GridPadPlacementPolicy()
    }
}
