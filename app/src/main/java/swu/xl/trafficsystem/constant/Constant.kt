package swu.xl.trafficsystem.constant

import androidx.annotation.IntDef

object Constant {
    const val NORMAL_COLOR = "#707070"
    const val SELECT_COLOR = "#fa7832"

    const val BOTTOM_BAR_TYPE_HOME = 0
    const val BOTTOM_BAR_TYPE_USER = 1
    @IntDef(BOTTOM_BAR_TYPE_HOME, BOTTOM_BAR_TYPE_USER)
    annotation class BottomType
}