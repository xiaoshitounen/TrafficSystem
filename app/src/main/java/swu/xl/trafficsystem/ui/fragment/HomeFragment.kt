package swu.xl.trafficsystem.ui.fragment

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
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
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.XPopupUtils
import kotlinx.android.synthetic.main.activity_route_edit.*
import kotlinx.android.synthetic.main.fragment_home.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.amap.AMapUtil
import swu.xl.trafficsystem.base.BaseFragment
import swu.xl.trafficsystem.constant.Constant
import swu.xl.trafficsystem.constant.Constant.NORMAL_COLOR
import swu.xl.trafficsystem.constant.Constant.SELECT_COLOR
import swu.xl.trafficsystem.manager.MapConfigManager
import swu.xl.trafficsystem.manager.MapRouteManager
import swu.xl.trafficsystem.model.MapLocation
import swu.xl.trafficsystem.thirdparty.other.MapChooseActivity
import swu.xl.trafficsystem.thirdparty.xpop.CustomEditTextBottomPopup

class HomeFragment: BaseFragment() {
    private lateinit var locationClient: AMapLocationClient

    private var latitude = 0.0
    private var longitude = 0.0
    private var lastBearing = 0F

    private var currentMapTypeIcon: ImageView? =null
    private var currentMapType: TextView? = null
    private var isTrafficEnable = false
    private var isIndoorEnable = false

    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_home, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        map.onDestroy()
        locationClient.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun initData() {
        initMap()
    }

    override fun initListener() {
        home_set.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(it, MapChooseActivity::class.java).apply {
                    putExtra(Constant.ROUTE_POINT_KEY, Constant.ROUTE_POINT_HOME)
                })
            }
        }

        company_set.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(it, MapChooseActivity::class.java).apply {
                    putExtra(Constant.ROUTE_POINT_KEY, Constant.ROUTE_POINT_COMPANY)
                })
            }
        }
    }

    private fun initMap() {
        initCompass()
        initLayer()
        initZoom()
        initLocation()
        initConfig()
        startLocation()
        initLayout()
    }

    private fun initLayout() {
        map_search_text.setOnClickListener {
            XPopup.Builder(context)
                .maxHeight(XPopupUtils.getAppHeight(context) - XPopupUtils.dp2px(context, 5F))
                .isThreeDrag(false)
                .moveUpToKeyboard(false)
                .autoOpenSoftInput(true)
                .asCustom(CustomEditTextBottomPopup(activity!!, MapRouteManager.city))
                .show()
        }
    }

    //处理自定义的指南针偏转
    private fun initCompass() {
        map.map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
                //do nothing
            }

            override fun onCameraChange(cameraPosition: CameraPosition?) {
                cameraPosition?.let {
                    //指南针旋转
                    val bearing = 360 - it.bearing
                    map_compass.post {
                        map_compass.pivotX = (map_compass.width / 2).toFloat()
                        map_compass.pivotY = (map_compass.height / 2).toFloat()
                        ObjectAnimator.ofFloat(map_compass, "Rotation", lastBearing, bearing).apply {
                            start()
                            lastBearing = bearing
                        }
                    }

                    //marker旋转
                    map.map.mapScreenMarkers.forEach { marker ->
                        marker.rotateAngle = it.bearing
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
            map.map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                LatLng(latitude, longitude), 17F, 180F, 0F
            )
                ))
            map.map.clear()
            map.map.addMarker(
                MarkerOptions()
                .position(AMapUtil.convertToLatLng(LatLonPoint(latitude, longitude)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_3d)))
            true
        }
    }

    //处理抽屉内的按钮
    private fun initConfig() {
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
            isScaleControlsEnabled = MapConfigManager.getScaleEnabled()
            //logo按钮
            logoPosition = MapConfigManager.getLogoPosition()
        }
    }

    //抽屉：自定义按钮的默认打开情况
    private fun initMapSelfConfig() {
        //设置自定义指南针的默认打开情况
        MapConfigManager.getCompassEnabled().also {
            compass_switch.isChecked = it
            map_compass.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        //设置系统比例尺的默认打开情况
        MapConfigManager.getScaleEnabled().also {
            scale_switch.isChecked = it
            map.map.uiSettings.isScaleControlsEnabled = it
        }
        //设置自定义缩放按钮的默认打开情况
        MapConfigManager.getZoomEnabled().also {
            zoom_switch.isChecked = it
            map_zoom.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        //设置自定义定位按钮的默认打开情况
        MapConfigManager.getLocationEnabled().also {
            location_switch.isChecked = it
            map_location.visibility = if (it) View.VISIBLE else View.INVISIBLE
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
            map_compass.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            MapConfigManager.setCompassEnabled(isChecked)
        }
        scale_switch.setOnCheckedChangeListener { _, isChecked ->
            map.map.uiSettings.isScaleControlsEnabled = isChecked
            MapConfigManager.setScaleEnabled(isChecked)
        }
        zoom_switch.setOnCheckedChangeListener { _, isChecked ->
            map_zoom.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            MapConfigManager.setZoomEnabled(isChecked)
        }
        location_switch.setOnCheckedChangeListener { _, isChecked ->
            map_location.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            MapConfigManager.setLocationEnabled(isChecked)
        }
        map_logo_position.setOnClickListener {
            XPopup.Builder(activity)
                .isDestroyOnDismiss(true)
                .asCenterList(
                    "请选择Logo位置", MapConfigManager.getLogoPositionsDescriptions(),
                    null, MapConfigManager.getIndexOfLogoPosition()
                ) { position, _ ->
                    MapConfigManager.setLogoPosition(position)
                    map.map.uiSettings.logoPosition = MapConfigManager.getLogoPosition(position)
                }.show()
        }
    }

    //抽屉：地图类型点击处理
    private fun changeMapType(type: Int) {
        //修改地图类型
        map.map.mapType = type
        //上一个类型还原
        currentMapTypeIcon?.setBackgroundResource(0)
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
        locationClient = AMapLocationClient(activity)
        locationClient.setLocationListener {
            //保存城市信息
            MapRouteManager.city = it.city
            //保存经纬度
            latitude = it.latitude
            longitude = it.longitude
            //保存当前位置
            MapRouteManager.setCurrent(MapLocation(LatLonPoint(latitude, longitude), "当前位置"))

            //CameraPosition4个参数: 位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
            map.map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                LatLng(it.latitude, it.longitude), 17F, 0F, 0F
            )
                ))
            locationClient.stopLocation()
            map.map.clear()
            map.map.addMarker(
                MarkerOptions()
                .position(AMapUtil.convertToLatLng(LatLonPoint(it.latitude, it.longitude)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)))
        }
        locationClient.startLocation()
    }
}