package swu.xl.trafficsystem.manager

import com.amap.api.maps.AMapOptions
import swu.xl.trafficsystem.store.TrafficSystemStore

object MapManager {
    private var isZoomEnabled = true
    private var isCompassEnabled = true
    private var isLocationEnabled = true
    private var isScaleEnabled = true
    private var logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT

    fun setZoomEnabled(enable: Boolean) {
        isZoomEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_ZOOM, enable)
    }

    fun getZoomEnabled() = isZoomEnabled

    fun setCompassEnabled(enable: Boolean) {
        isCompassEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_COMPASS, enable)
    }

    fun getCompassEnabled() = TrafficSystemStore.getBoolean(TrafficSystemStore.KEY_MAP_COMPASS)

    fun setLocationEnabled(enable: Boolean) {
        isLocationEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_LOCATION, enable)
    }

    fun getLocationEnabled() = TrafficSystemStore.getBoolean(TrafficSystemStore.KEY_MAP_LOCATION)

    fun setScaleEnabled(enable: Boolean) {
        isScaleEnabled = enable
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_SCALE, enable)
    }

    fun getScaleEnabled() = TrafficSystemStore.getBoolean(TrafficSystemStore.KEY_MAP_SCALE)

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

    fun getLogoPosition() = TrafficSystemStore.getInt(TrafficSystemStore.KEY_MAP_LOGO_POSITION)
}