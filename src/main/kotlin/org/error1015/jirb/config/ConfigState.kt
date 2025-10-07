package org.error1015.jirb.config

import org.error1015.jirb.config.ConfigState.DEBUG
import org.error1015.jirb.config.ConfigState.DISABLE
import org.error1015.jirb.config.ConfigState.ENABLE
import org.error1015.jirb.config.ConfigState.SHIFT
import org.error1015.jirb.config.ConfigState.SHIFT_AND_DEBUG

/**
 * 用于控制Config的开关状态
 */
enum class ConfigState {
    DISABLE,
    ENABLE,
    SHIFT,
    DEBUG,
    SHIFT_AND_DEBUG;
}

fun ConfigState.isEnabled(shift: Boolean, advanced : Boolean) = when (this) {
    ENABLE -> true
    DISABLE -> false
    SHIFT -> shift
    DEBUG -> shift
    SHIFT_AND_DEBUG -> shift && advanced
}