package com.qtech.bubbles.common.logging.old

import java.util.logging.Level

open class GameLoggingLevels : Level {
    protected constructor(name: String?, value: Int) : super(name, value) {}
    protected constructor(name: String?, value: Int, resourceBundleName: String?) : super(name, value, resourceBundleName) {}

    companion object {
        val DEBUG: Level = GameLoggingLevels("DEBUG", 1)
    }
}