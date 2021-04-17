package swu.xl.trafficsystem.util

import swu.xl.trafficsystem.base.BaseFragment
import swu.xl.trafficsystem.constant.Constant
import swu.xl.trafficsystem.constant.Constant.BOTTOM_BAR_TYPE_HOME
import swu.xl.trafficsystem.constant.Constant.BOTTOM_BAR_TYPE_USER
import swu.xl.trafficsystem.ui.fragment.HomeFragment
import swu.xl.trafficsystem.ui.fragment.UserFragment

class FragmentUtil {
    companion object {
        val fragmentUtil by lazy { FragmentUtil() }
    }

    private val homeFragment by lazy { HomeFragment() }
    private val userFragment by lazy { UserFragment() }

    fun getFragment(@Constant.BottomType type: Int): BaseFragment {
        return when(type) {
            BOTTOM_BAR_TYPE_HOME -> homeFragment
            BOTTOM_BAR_TYPE_USER -> userFragment
            else -> homeFragment
        }
    }
}