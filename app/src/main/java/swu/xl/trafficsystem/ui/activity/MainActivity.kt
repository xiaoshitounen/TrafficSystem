package swu.xl.trafficsystem.ui.activity

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import kotlinx.android.synthetic.main.activity_main.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.amap.AMapUtil
import swu.xl.trafficsystem.amap.BusRouteHelper
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.log.TrafficSystemLogger
import swu.xl.trafficsystem.manager.MapManager
import swu.xl.trafficsystem.util.PermissionUtil
import swu.xl.trafficsystem.util.ToastUtil


class MainActivity : BaseActivity() {
    private lateinit var routeSearch: RouteSearch
    private lateinit var locationClient: AMapLocationClient

    private var lastBearing = 0F

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
            initMap()
        } else {
            PermissionUtil.requestLocation(this)
        }
    }

    private fun initMap() {
        //设置是否显示内部建筑
        map.map.showIndoorMap(MapManager.getShowIndoorEnabled())
        //设置是否显示路况
        map.map.isTrafficEnabled = MapManager.getTrafficEnabled()
        //设置默认地图图标
        map.map.uiSettings.apply {
            //缩放按钮
            isZoomControlsEnabled = false
            //指南按钮 使用自定义
            isCompassEnabled = false
            //定位按钮
            isMyLocationButtonEnabled = false
            //比例尺按钮
            isScaleControlsEnabled = MapManager.getScaleEnabled()
            //logo按钮
            logoPosition = MapManager.getLogoPosition()
        }
        startLocation()
        initCompass()
        initLocation()
        initSetting()
    }

    //处理自定义的指南针偏转
    private fun initCompass() {
        map.map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                //do nothing
            }

            override fun onCameraChange(cameraPosition: CameraPosition?) {
                cameraPosition?.let {
                    val bearing = 360 - it.bearing

                    RotateAnimation(lastBearing, bearing,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    ).apply {
                        fillAfter = true
                        map_compass.startAnimation(this)
                        lastBearing = bearing
                    }

                }
            }
        })
    }

    //处理自动定位
    private fun initLocation() {
        map_location.setOnClickListener {
            startLocation()
        }
    }

    private fun initSetting() {
        map_normal.setOnClickListener {
            map.map.mapType = AMap.MAP_TYPE_NORMAL
        }

        map_satellite.setOnClickListener {
            map.map.mapType = AMap.MAP_TYPE_SATELLITE
        }

        map_night.setOnClickListener {
            map.map.mapType = AMap.MAP_TYPE_NIGHT
        }

        map_traffic.setOnClickListener {
            map.map.isTrafficEnabled = !map.map.isTrafficEnabled
        }
    }

    private fun startLocation() {
        //drawer.openDrawer(Gravity.RIGHT)
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        locationClient = AMapLocationClient(this)
        locationClient.setLocationListener {
            //CameraPosition4个参数: 位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
            map.map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
                LatLng(it.latitude, it.longitude), 17F, 0F, 0F
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
                    initMap()
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