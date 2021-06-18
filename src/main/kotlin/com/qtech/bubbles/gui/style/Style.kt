@file:Suppress("unused")

package com.qtech.bubbles.gui.style

import java.awt.Color

class Style {
    var borderWidths: StateBundle<Float> = DEFAULT_BORDER_WIDTHS
    var borderColors: StateBundle<Color> = DEFAULT_BORDER_COLORS
    var backgroundColors: StateBundle<Color> = DEFAULT_BACKGROUND_COLORS
    var foregroundColors: StateBundle<Color> = DEFAULT_FOREGROUND_COLORS
    var activeBorderWidths: StateBundle<Float> = DEFAULT_ACTIVE_BORDER_WIDTHS
    var activeBorderColors: StateBundle<Color> = DEFAULT_ACTIVE_BORDER_COLORS
    var activeBackgroundColors: StateBundle<Color> = DEFAULT_ACTIVE_BACKGROUND_COLORS
    var activeForegroundColors: StateBundle<Color> = DEFAULT_ACTIVE_FOREGROUND_COLORS

    companion object {
        val DEFAULT_ACCENT = Color(0, 96, 128)
        val DEFAULT_BACKGROUND = Color(96, 96, 96)
        val DEFAULT_FOREGROUND = Color(160, 160, 160)
        val DEFAULT_BORDER_WIDTHS = StateBundle(1f, 1f, 1f)
        val DEFAULT_BORDER_COLORS = StateBundle(DEFAULT_BACKGROUND.brighter().brighter(), DEFAULT_BACKGROUND.brighter(), DEFAULT_BACKGROUND.darker())
        val DEFAULT_BACKGROUND_COLORS = StateBundle(DEFAULT_BACKGROUND.brighter().brighter(), DEFAULT_BACKGROUND.brighter(), DEFAULT_BACKGROUND.darker())
        val DEFAULT_FOREGROUND_COLORS = StateBundle(DEFAULT_FOREGROUND.brighter().brighter(), DEFAULT_FOREGROUND.brighter(), DEFAULT_FOREGROUND.darker())
        val DEFAULT_ACTIVE_BORDER_WIDTHS = StateBundle(1f, 1f, 1f)
        val DEFAULT_ACTIVE_BORDER_COLORS = StateBundle(DEFAULT_ACCENT.brighter().brighter(), DEFAULT_ACCENT.brighter(), DEFAULT_ACCENT.darker())
        val DEFAULT_ACTIVE_BACKGROUND_COLORS = StateBundle(DEFAULT_BACKGROUND.brighter().brighter(), DEFAULT_BACKGROUND.brighter(), DEFAULT_BACKGROUND.darker())
        val DEFAULT_ACTIVE_FOREGROUND_COLORS = StateBundle(DEFAULT_FOREGROUND.brighter().brighter(), DEFAULT_FOREGROUND.brighter(), DEFAULT_FOREGROUND.darker())
    }

    init {
//        StateBundle<Color> backgroundColors, StateBundle<Color> foregroundColors, StateBundle<Color> borderColors, StateBundle<Float> borderWidths
//        this.backgroundColors = backgroundColors;
//        this.foregroundColors = foregroundColors;
//        this.borderColors = borderColors;
//        this.borderWidths = borderWidths;
    }
}