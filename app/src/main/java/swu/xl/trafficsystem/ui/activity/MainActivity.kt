package swu.xl.trafficsystem.ui.activity

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.*
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import kotlinx.android.synthetic.main.activity_main.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.amap.AMapUtil
import swu.xl.trafficsystem.amap.BusRouteHelper
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.log.TrafficSystemLogger
import swu.xl.trafficsystem.util.PermissionUtil
import swu.xl.trafficsystem.util.ToastUtil


class MainActivity : BaseActivity() {
    private lateinit var routeSearch: RouteSearch
    private lateinit var locationClient: AMapLocationClient

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
            startLocation()
        } else {
            PermissionUtil.requestLocation(this)
        }
    }

    private fun startLocation() {
        val locationStyle = MyLocationStyle()
        locationStyle.showMyLocation(false)
        map.map.myLocationStyle = locationStyle
        map.map.isMyLocationEnabled = true
        map.map.isTrafficEnabled = true
        map.map.showIndoorMap(true)
        init()
    }

    fun init() {
        locationClient = AMapLocationClient(this)
        locationClient.setLocationListener {
            ToastUtil.toast("定位：${it.city}")
            map.map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
                LatLng(it.latitude, it.longitude), 17F, 90F, 300F
            )))
            locationClient.stopLocation()
            map.map.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(LatLonPoint(it.latitude, it.longitude)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)))
        }
        locationClient.startLocation()

        routeSearch = RouteSearch(this)
        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {

            }

            override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {
                if (errorCode == 1000) {
                    if (result != null && result.paths != null && result.paths.size > 0) {
                        //展示数据
                        TrafficSystemLogger.d("${result.paths.size}")
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionUtil.location_code -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocation()
                } else {
                    ToastUtil.toast("用户拒绝授予定位权限")
                }
            }
            else -> {
                //do nothing
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
        locationClient.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        map.onSaveInstanceState(outState)
    }
}