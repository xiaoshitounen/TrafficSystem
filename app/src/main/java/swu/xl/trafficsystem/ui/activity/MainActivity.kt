package swu.xl.trafficsystem.ui.activity

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.*
import kotlinx.android.synthetic.main.activity_main.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.constant.Constant
import swu.xl.trafficsystem.constant.Constant.BOTTOM_BAR_TYPE_HOME
import swu.xl.trafficsystem.constant.Constant.BOTTOM_BAR_TYPE_USER
import swu.xl.trafficsystem.constant.Constant.NORMAL_COLOR
import swu.xl.trafficsystem.constant.Constant.SELECT_COLOR
import swu.xl.trafficsystem.log.TrafficSystemLogger
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.FragmentUtil
import swu.xl.trafficsystem.util.PermissionUtil

class MainActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_main

    override fun preInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    )
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    override fun initData() {
        if (PermissionUtil.checkLocation(this)) {
            init()
        } else {
            PermissionUtil.requestLocation(this)
        }
    }

    private fun init() {
        //默认选中首页
        changeBottomBarType(BOTTOM_BAR_TYPE_HOME)
        bottom_type_home.setOnClickListener { changeBottomBarType(BOTTOM_BAR_TYPE_HOME) }
        bottom_type_user.setOnClickListener { changeBottomBarType(BOTTOM_BAR_TYPE_USER) }
    }

    private fun changeBottomBarType(@Constant.BottomType type: Int) {
        when (type) {
            BOTTOM_BAR_TYPE_HOME -> {
                //设置选中
                bottom_title_home.setTextColor(Color.parseColor(SELECT_COLOR))
                bottom_title_home_select.visibility = VISIBLE
                //设置没有选中
                bottom_title_user.setTextColor(Color.parseColor(NORMAL_COLOR))
                bottom_title_user_select.visibility = INVISIBLE
            }
            BOTTOM_BAR_TYPE_USER -> {
                //设置选中
                bottom_title_user.setTextColor(Color.parseColor(SELECT_COLOR))
                bottom_title_user_select.visibility = VISIBLE
                //设置没有选中
                bottom_title_home.setTextColor(Color.parseColor(NORMAL_COLOR))
                bottom_title_home_select.visibility = INVISIBLE
            }
            else -> {}
        }

        changeFragment(type)
    }

    private fun changeFragment(@Constant.BottomType type: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, FragmentUtil.fragmentUtil.getFragment(type), type.toString())
            commit()
        }
        true
    }
}