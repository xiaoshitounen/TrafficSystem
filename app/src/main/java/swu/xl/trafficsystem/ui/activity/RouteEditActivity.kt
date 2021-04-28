package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import kotlinx.android.synthetic.main.activity_route_edit.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.OnTipClickListener
import swu.xl.trafficsystem.adapter.TipListAdapter
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.constant.Constant.ROUTE_LINE_END
import swu.xl.trafficsystem.constant.Constant.ROUTE_LINE_KEY
import swu.xl.trafficsystem.constant.Constant.ROUTE_LINE_START
import swu.xl.trafficsystem.manager.MapRouteManager
import swu.xl.trafficsystem.model.MapLocation
import swu.xl.trafficsystem.thirdparty.other.MapChooseActivity

class RouteEditActivity : BaseActivity(), Inputtips.InputtipsListener {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, RouteEditActivity::class.java))
        }
    }

    private val adapter = TipListAdapter()

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
        //弹出键盘
        end_edit.isFocusable = true
        end_edit.isFocusableInTouchMode = true
        end_edit.requestFocus()

        //返回
        findViewById<ImageView>(R.id.back).setOnClickListener { back() }

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

        //搜索列表
        map_tip_list.layoutManager = LinearLayoutManager(this)
        map_tip_list.adapter = adapter

        //默认搜索
        MapRouteManager.getLine().also { line ->
            start_edit.setText(line.start.name)
            end_edit.setText(line.end.name)
            //默认光标位置且全部选中
            end_edit.setSelection(0, line.end.name.length)
            //默认搜索内容
            doSearch(line.end.name)
        }
    }

    override fun initListener() {
        start_edit.addTextChangedListener(object : XLTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doSearch(s.toString())
            }
        })
        end_edit.addTextChangedListener(object : XLTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doSearch(s.toString())
            }
        })
        adapter.addOnTipClickListener(object : OnTipClickListener {
            override fun onTipClick(tip: Tip) {
                if (start_edit.hasFocus()) {
                    MapRouteManager.changeStartLocation(MapLocation(tip.point, tip.name))
                }

                if (end_edit.hasFocus()) {
                    MapRouteManager.changeEndLocation(MapLocation(tip.point, tip.name))
                }
                RoutePlanActivity.start(this@RouteEditActivity)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        back()
    }

    private fun back() {
        finish()
        //淡入淡出
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun doSearch(input: String) {
        val query = InputtipsQuery(input, MapRouteManager.city)
        //查询严格限制当前城市
        query.cityLimit = true
        val inputTips = Inputtips(this, query)
        inputTips.setInputtipsListener(this)
        inputTips.requestInputtipsAsyn()
    }

    override fun onGetInputtips(tips: MutableList<Tip>, code: Int) {
        adapter.setTips(tips)
    }
}

open class XLTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) { }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
}