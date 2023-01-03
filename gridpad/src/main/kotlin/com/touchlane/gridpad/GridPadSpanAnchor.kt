package com.touchlane.gridpad

/**
 * Anchor for spanned cells
 */
internal data class GridPadSpanAnchor(val horizontal: Horizontal, val vertical: Vertical) {

    /**
     * Horizontal anchor position
     */
    enum class Horizontal {
        START, END
    }

    /**
     * Vertical anchor position
     */
    enum class Vertical {
        TOP, BOTTOM
    }
}
