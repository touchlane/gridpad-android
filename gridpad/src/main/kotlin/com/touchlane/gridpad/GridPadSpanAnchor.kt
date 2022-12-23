package com.touchlane.gridpad

/**
 * Anchor for spanned cells
 */
internal data class GridPadSpanAnchor(val horizontal: Horizontal, val vertical: Vertical) {
    enum class Horizontal {
        START, END
    }

    enum class Vertical {
        TOP, BOTTOM
    }
}