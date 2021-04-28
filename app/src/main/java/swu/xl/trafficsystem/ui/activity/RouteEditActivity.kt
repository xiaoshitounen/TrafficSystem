package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_route_edit.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.constant.Constant
import swu.xl.trafficsystem.constant.Constant.ROUTE_LINE_END
import swu.xl.trafficsystem.constant.Constant.ROUTE_LINE_KEY
import swu.xl.trafficsystem.constant.Constant.ROUTE_LINE_START
import swu.xl.trafficsystem.manager.MapRouteManager
import swu.xl.trafficsystem.thirdparty.other.MapChooseActivity

class RouteEditActivity : BaseActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, RouteEditActivity::class.java))
        }
    }

    override fun preInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    )
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    override fun getLayoutId() = R.layout.activity_route_edit

    override fun initData() {
        MapRouteManager.getLine().also { line ->
            start_edit.setText(line.start.name)
            end_edit.setText(line.end.name)
        }

        //弹出键盘
        end_edit.isFocusable = true
        end_edit.isFocusableInTouchMode = true
        end_edit.requestFocus()

        //返回
        findViewById<ImageView>(R.id.back).setOnClickListener { finish() }

        //地图选点
        map_choose.setOnClickListener {
            startActivity(Intent(this, MapChooseActivity::class.java).apply {
                if (start_edit.hasFocus()) {
                    putExtra(ROUTE_LINE_KEY, ROUTE_LINE_START)
                }

                if (end_edit.hasFocus()) {
                    putExtra(ROUTE_LINE_KEY, ROUTE_LINE_END)
                }
            })
        }
    }
}