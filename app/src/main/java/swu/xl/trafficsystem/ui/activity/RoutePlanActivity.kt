package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_route_plan.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.BusPathAdapter
import swu.xl.trafficsystem.adapter.OnBusPathClickListener
import swu.xl.trafficsystem.amap.AMapUtil
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.log.TrafficSystemLogger
import swu.xl.trafficsystem.manager.MapRouteManager
import swu.xl.trafficsystem.util.ToastUtil

class RoutePlanActivity : BaseActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, RoutePlanActivity::class.java))
        }
    }

    private val adapter = BusPathAdapter()
    private lateinit var routeSearch: RouteSearch

    override fun getLayoutId() = R.layout.activity_route_plan

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

    override fun initData() {
        routeSearch = RouteSearch(this)
        initTab()
        initPaths()

        MapRouteManager.getLine().also { line ->
            start.text = line.start.name
            end.text = line.end.name
        }

        calculateBusRouteAsync(BUS_DEFAULT)
    }

    private fun initTab() {
        val tabList = mutableListOf<TabLayout.Tab>().apply {
            add(bus_route_tab.newTab().setText("最快捷").apply { tag = BUS_DEFAULT })
            add(bus_route_tab.newTab().setText("换乘少").apply { tag = BUS_LEASE_CHANGE })
            add(bus_route_tab.newTab().setText("步行少").apply { tag = BUS_LEASE_WALK })
            add(bus_route_tab.newTab().setText("最舒适").apply { tag = BUS_COMFORTABLE })
            add(bus_route_tab.newTab().setText("最经济").apply { tag = BUS_SAVE_MONEY })
            add(bus_route_tab.newTab().setText("不乘地铁").apply { tag = BUS_NO_SUBWAY })
        }

        tabList.forEach { bus_route_tab.addTab(it) }
    }

    private fun initPaths() {
        bus_route_list.layoutManager = LinearLayoutManager(this)
        bus_route_list.adapter = adapter
    }

    private fun calculateBusRouteAsync(type: Int) {
        MapRouteManager.getLine().also { line ->
            // fromAndTo包含路径规划的起点和终点，mode表示公交查询模式
            // city表示城市，nightflag表示是否计算夜班车 0表示不计算,1表示计算
            // mode 有：
            //  BUS_DEFAULT 最快捷模式 0
            //  BUS_SAVE_MONEY 最经济模式 1
            //  BUS_LEASE_CHANGE 最少换乘 2
            //  BUS_LEASE_WALK 最少步行 3
            //  BUS_COMFORTABLE 最舒适 4
            //  BUS_NO_SUBWAY 不乘地铁 5
            routeSearch.calculateBusRouteAsyn(
                BusRouteQuery(
                    FromAndTo(line.start.location, line.end.location),
                    type, MapRouteManager.city, 0
                )
            )
        }
    }


    override fun initListener() {
        back.setOnClickListener { finish() }
        routeSearch.setRouteSearchListener(object : OnBusRouteSearchListener() {
            override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {
                if (errorCode == 1000) {
                    if (result != null && result.paths != null && result.paths.size > 0) {
                        //展示数据
                        adapter.setBusPath(result.paths)
                    } else {
                        //没有搜集到数据
                        TrafficSystemLogger.e("没有搜集到数据")
                        ToastUtil.toast("没有搜集到数据")
                    }
                } else {
                    //发生错误
                    TrafficSystemLogger.e("发生错误")
                    ToastUtil.toast("发生错误")
                }
            }
        })

        bus_route_tab.addOnTabSelectedListener(object : OnTabSelectedListener() {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                calculateBusRouteAsync(tab?.tag as Int)
            }
        })

        adapter.addOnBusPathClickListener(object : OnBusPathClickListener {
            override fun onBusPathClick(path: BusPath) {
                TrafficSystemLogger.d("公交站点图：${AMapUtil.getBusPathTitle(path)}")
            }
        })
    }
}

open class OnBusRouteSearchListener : OnRouteSearchListener {
    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) { }
    override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) { }
    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) { }
    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) { }
}

open class OnTabSelectedListener : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) { }
    override fun onTabUnselected(tab: TabLayout.Tab?) { }
    override fun onTabSelected(tab: TabLayout.Tab?) { }
}