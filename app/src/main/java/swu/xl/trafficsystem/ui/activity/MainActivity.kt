package swu.xl.trafficsystem.ui.activity

import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
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
    companion object {
        private const val NORMAL_COLOR = "#707070"
        private const val SELECT_COLOR = "#fa7832"
    }

    private lateinit var locationClient: AMapLocationClient

    private var latitude = 0.0
    private var longitude = 0.0
    private var lastBearing = 0F

    private var currentMapTypeIcon: ImageView? =null
    private var currentMapType: TextView? = null
    private var isTrafficEnable = false
    private var isIndoorEnable = false

    private lateinit var routeSearch: RouteSearch

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
        initCompass()
        initLayer()
        initZoom()
        initLocation()
        initSetting()
        startLocation()
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
                    map_compass.post {
                        map_compass.pivotX = (map_compass.width / 2).toFloat()
                        map_compass.pivotY = (map_compass.height / 2).toFloat()
                        ObjectAnimator.ofFloat(map_compass, "Rotation", lastBearing, bearing).apply {
                            start()
                            lastBearing = bearing
                        }
                    }
                }
            }
        })
    }

    //处理右边抽屉
    private fun initLayer() {
        map_layer.setOnClickListener {
            drawer.openDrawer(Gravity.RIGHT)
            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    //处理自定义缩放
    private fun initZoom() {
        map_zoom_add.setOnClickListener {
            map.map.animateCamera(CameraUpdateFactory.zoomIn())
        }

        map_zoom_reduce.setOnClickListener {
            map.map.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    //处理自定义定位
    private fun initLocation() {
        map_location.setOnClickListener {
            startLocation()
        }

        map_location.setOnLongClickListener {
            map.map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
                LatLng(latitude, longitude), 17F, 180F, 0F
            )))
            true
        }
    }

    //处理抽屉内的按钮
    private fun initSetting() {
        initMapDefaultConfig()
        initMapSelfConfig()
        initMapSelfListener()
    }

    //高德地图默认配置修改
    private fun initMapDefaultConfig() {
        //设置是否显示内部建筑
        map.map.showIndoorMap(false)
        //设置是否显示路况
        map.map.isTrafficEnabled = false
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
    }

    //抽屉：自定义按钮的默认打开情况
    private fun initMapSelfConfig() {
        //设置自定义指南针的默认打开情况
        MapManager.getCompassEnabled().also {
            compass_switch.isChecked = it
            map_compass.visibility = if (it) VISIBLE else INVISIBLE
        }
        //设置系统比例尺的默认打开情况
        MapManager.getScaleEnabled().also {
            scale_switch.isChecked = it
            map.map.uiSettings.isScaleControlsEnabled = it
        }
        //设置自定义缩放按钮的默认打开情况
        MapManager.getZoomEnabled().also {
            zoom_switch.isChecked = it
            map_zoom.visibility = if (it) VISIBLE else INVISIBLE
        }
        //设置自定义定位按钮的默认打开情况
        MapManager.getLocationEnabled().also {
            location_switch.isChecked = it
            map_location.visibility = if (it) VISIBLE else INVISIBLE
        }
    }

    //抽屉：自定义按钮的事件监听
    private fun initMapSelfListener() {
        //地图类型
        changeMapType(AMap.MAP_TYPE_NORMAL)
        map_normal.setOnClickListener { changeMapType(AMap.MAP_TYPE_NORMAL) }
        map_satellite.setOnClickListener { changeMapType(AMap.MAP_TYPE_SATELLITE) }
        map_night.setOnClickListener { changeMapType(AMap.MAP_TYPE_NIGHT) }
        //事件
        map_traffic.setOnClickListener { modifyMapEventTraffic() }
        map_indoor.setOnClickListener { modifyMapEventIndoor() }
        //设置
        compass_switch.setOnCheckedChangeListener { _, isChecked ->
            map_compass.visibility = if (isChecked) VISIBLE else INVISIBLE
            MapManager.setCompassEnabled(isChecked)
        }
        scale_switch.setOnCheckedChangeListener { _, isChecked ->
            map.map.uiSettings.isScaleControlsEnabled = isChecked
            MapManager.setScaleEnabled(isChecked)
        }
        zoom_switch.setOnCheckedChangeListener { _, isChecked ->
            map_zoom.visibility = if (isChecked) VISIBLE else INVISIBLE
            MapManager.setZoomEnabled(isChecked)
        }
        location_switch.setOnCheckedChangeListener { _, isChecked ->
            map_location.visibility = if (isChecked) VISIBLE else INVISIBLE
            MapManager.setLocationEnabled(isChecked)
        }
    }

    //抽屉：地图类型点击处理
    private fun changeMapType(type: Int) {
        //修改地图类型
        map.map.mapType = type
        //上一个类型还原
        currentMapTypeIcon?.setBackgroundResource(R.drawable.map_type_bg)
        currentMapType?.setTextColor(Color.parseColor(NORMAL_COLOR))
        //选中当前地图
        when (type) {
            AMap.MAP_TYPE_NORMAL -> {
                //设置选中样式
                map_type_normal_icon.setBackgroundResource(R.drawable.map_type_bg_select)
                map_type_normal.setTextColor(Color.parseColor(SELECT_COLOR))
                //设置新的选中地图类型
                currentMapTypeIcon = map_type_normal_icon
                currentMapType = map_type_normal
            }
            AMap.MAP_TYPE_SATELLITE -> {
                //设置选中样式
                map_type_satellite_icon.setBackgroundResource(R.drawable.map_type_bg_select)
                map_type_satellite.setTextColor(Color.parseColor(SELECT_COLOR))
                //设置新的选中地图类型
                currentMapTypeIcon = map_type_satellite_icon
                currentMapType = map_type_satellite
            }
            AMap.MAP_TYPE_NIGHT -> {
                //设置选中样式
                map_type_night_icon.setBackgroundResource(R.drawable.map_type_bg_select)
                map_type_night.setTextColor(Color.parseColor(SELECT_COLOR))
                //设置新的选中地图类型
                currentMapTypeIcon = map_type_night_icon
                currentMapType = map_type_night
            }
            else -> {
                //do nothing
            }
        }
    }

    //抽屉：路况点击处理
    private fun modifyMapEventTraffic() {
        isTrafficEnable = !isTrafficEnable
        if (isTrafficEnable) {
            map_event_traffic_bg.setBackgroundResource(R.drawable.map_event_bg_select)
            map_event_traffic_icon.setColorFilter(Color.parseColor(SELECT_COLOR))
            map_event_traffic.setTextColor(Color.parseColor(SELECT_COLOR))
        } else {
            map_event_traffic_bg.setBackgroundResource(R.drawable.map_event_bg)
            map_event_traffic_icon.setColorFilter(Color.parseColor(NORMAL_COLOR))
            map_event_traffic.setTextColor(Color.parseColor(NORMAL_COLOR))
        }
        map.map.isTrafficEnabled = isTrafficEnable
    }

    //抽屉：内部建筑点击处理
    private fun modifyMapEventIndoor() {
        isIndoorEnable = !isIndoorEnable
        if (isIndoorEnable) {
            map_event_indoor_bg.setBackgroundResource(R.drawable.map_event_bg_select)
            map_event_indoor_icon.setColorFilter(Color.parseColor(SELECT_COLOR))
            map_event_indoor.setTextColor(Color.parseColor(SELECT_COLOR))
        } else {
            map_event_indoor_bg.setBackgroundResource(R.drawable.map_event_bg)
            map_event_indoor_icon.setColorFilter(Color.parseColor(NORMAL_COLOR))
            map_event_indoor.setTextColor(Color.parseColor(NORMAL_COLOR))
        }
        map.map.showIndoorMap(isIndoorEnable)
    }

    //开始定位、自动定位
    private fun startLocation() {
        locationClient = AMapLocationClient(this)
        locationClient.setLocationListener {
            //保存经纬度
            latitude = it.latitude
            longitude = it.longitude

            //CameraPosition4个参数: 位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
            map.map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
                LatLng(it.latitude, it.longitude), 17F, 0F, 0F
            )))
            locationClient.stopLocation()
            map.map.clear()
            map.map.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(LatLonPoint(it.latitude, it.longitude)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)))
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