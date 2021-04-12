package swu.xl.trafficsystem.manager

import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import swu.xl.trafficsystem.store.TrafficSystemStore

object MapManager {
    private var mapType = AMap.MAP_TYPE_NORMAL
    private var isShowIndoorEnabled = true
    private var isTrafficEnabled = true
    private var isZoomEnabled = true
    private var isCompassEnabled = true
    private var isLocationEnabled = true
    private var isScaleEnabled = true
    private var logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT

    /**
     * MAP_TYPE_NAVI:导航地图
     * MAP_TYPE_NIGHT:夜景地图
     * MAP_TYPE_NORMAL白昼地图（即普通地图）
     * MAP_TYPE_SATELLITE:卫星图
     */
    fun setMapType(type: Int) {
        mapType = type
        //TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_TYPE, type)
    }

    fun getMapType() = mapType

    fun setShowIndoorEnabled(enable: Boolean) {
        isShowIndoorEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_INDOOR, enable)
    }

    fun getShowIndoorEnabled() = isShowIndoorEnabled

    fun setTrafficEnabled(enable: Boolean) {
        isTrafficEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_TRAFFIC, enable)
    }

    fun getTrafficEnabled() = isTrafficEnabled

    fun setZoomEnabled(enable: Boolean) {
        isZoomEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_ZOOM, enable)
    }

    fun getZoomEnabled() = isZoomEnabled

    fun setCompassEnabled(enable: Boolean) {
        isCompassEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_COMPASS, enable)
    }

    fun getCompassEnabled() = isCompassEnabled

    fun setLocationEnabled(enable: Boolean) {
        isLocationEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_LOCATION, enable)
    }

    fun getLocationEnabled() = isLocationEnabled

    fun setScaleEnabled(enable: Boolean) {
        isScaleEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_SCALE, enable)
    }

    fun getScaleEnabled() = isScaleEnabled

    /**
     * AMapOptions.LOGO_POSITION_BOTTOM_LEFT:LOGO边缘MARGIN（左边）
     * AMapOptions.LOGO_MARGIN_BOTTOM:LOGO边缘MARGIN（底部）
     * AMapOptions.LOGO_MARGIN_RIGHT:LOGO边缘MARGIN（右边）
     * AMapOptions.LOGO_POSITION_BOTTOM_CENTER:Logo位置（地图底部居中）
     * AMapOptions.LOGO_POSITION_BOTTOM_LEFT:Logo位置（地图左下角）
     * AMapOptions.LOGO_POSITION_BOTTOM_RIGHT:Logo位置（地图右下角）
     */
    fun setLogoPosition(position: Int) {
        logoPosition = position
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_LOGO_POSITION, position)
    }

    fun getLogoPosition() = logoPosition
}