package com.touchlane.gridpad

/**
 * Implicit placement policy for items.
 * There are two major types of settings here: the main axis of placement presented by the [mainAxis]
 * property and the direction of placement presented by [horizontal] and [vertical] properties.
 *
 * The [mainAxis] property describes which axis would be used to find the next position.
 * For example, [MainAxis.HORIZONTAL] means that firstly next position will look in a current row
 * and if there isn't a place for the item algorithm will move to the next row.
 *
 * The [horizontal] property describes the direction for choosing the next item on the
 * horizontal axis: left or right side, depending on LTR or RTL settings.
 *
 * The [vertical] property describes the direction for choosing the next item on the vertical
 * axis: above or below.
 *
 * @param mainAxis the main axis for selecting the next location
 * @param horizontal horizontal placement policy
 * @param vertical vertical placement policy
 */
public data class GridPadPlacementPolicy(
    val mainAxis: MainAxis = MainAxis.HORIZONTAL,
    val horizontal: Horizontal = Horizontal.FROM_START,
    val vertical: Vertical = Vertical.FROM_TOP
) {
    /**
     * Anchor for spanned cells
     */
    internal val anchor: GridPadSpanAnchor = run {
        val horizontal = when (this.horizontal) {
            Horizontal.FROM_START -> GridPadSpanAnchor.Horizontal.START
            Horizontal.FROM_END -> GridPadSpanAnchor.Horizontal.END
        }
        val vertical = when (this.vertical) {
            Vertical.FROM_TOP -> GridPadSpanAnchor.Vertical.TOP
            Vertical.FROM_BOTTOM -> GridPadSpanAnchor.Vertical.BOTTOM
        }
        GridPadSpanAnchor(horizontal, vertical)
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
    public enum class Horizontal {
        FROM_START, FROM_END
    }

    /**
     * Vertical placement policy
     */
    public enum class Vertical {
        FROM_TOP, FROM_BOTTOM
    }

    public companion object {
        public val DEFAULT: GridPadPlacementPolicy = GridPadPlacementPolicy()
    }
}