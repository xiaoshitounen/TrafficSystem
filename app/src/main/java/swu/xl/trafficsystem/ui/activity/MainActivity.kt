package swu.xl.trafficsystem.ui.activity

import com.amap.api.services.route.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.amap.BusRouteHelper
import swu.xl.trafficsystem.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var routeSearch: RouteSearch

    override fun getLayoutId() = R.layout.activity_main

    override fun initData() {
        println("onCreate")
    }

    override fun initListener() {
        routeSearch = RouteSearch(this)
        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {

            }

            override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {
                if (errorCode == 1000) {
                    if (result != null && result.paths != null && result.paths.size > 0) {
                        //展示数据
                        println("${result.paths.size}")
                    } else {
                        //没有搜集到数据
                        println("没有搜集到数据")
                    }
                } else {
                    //发生错误
                    print("xl-ddd 发生错误")
                }
            }

            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

            }

            override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {

            }
        })

        println("xl-ddd 计算方案")
        routeSearch.calculateBusRouteAsyn(BusRouteHelper.searchBusRouteResult())
    }
}