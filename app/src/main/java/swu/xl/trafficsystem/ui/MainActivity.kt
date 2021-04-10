package swu.xl.trafficsystem.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.services.route.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.amap.BusRouteHelper

class MainActivity : AppCompatActivity() {
    private lateinit var routeSearch: RouteSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("onCreate")
        initListener()
    }

    private fun initListener() {
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