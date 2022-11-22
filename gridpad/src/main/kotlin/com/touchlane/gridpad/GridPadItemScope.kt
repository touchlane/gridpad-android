package com.touchlane.gridpad

import androidx.compose.runtime.Stable

/**
 * Receiver scope being used by the item content parameter of [GridPad].
 */
@Stable
@GridPadScopeMarker
sealed interface GridPadItemScope

object GridPadItemScopeImpl : GridPadItemScope