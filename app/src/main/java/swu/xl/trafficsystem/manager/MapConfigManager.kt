package swu.xl.trafficsystem.manager

import com.amap.api.maps.AMapOptions
import swu.xl.trafficsystem.store.TrafficSystemStore

object MapConfigManager {
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
     * AMapOptions.LOGO_MARGIN_LEFT:LOGO边缘MARGIN（左边）
     * AMapOptions.LOGO_MARGIN_BOTTOM:LOGO边缘MARGIN（底部）
     * AMapOptions.LOGO_MARGIN_RIGHT:LOGO边缘MARGIN（右边）
     * AMapOptions.LOGO_POSITION_BOTTOM_CENTER:Logo位置（地图底部居中）
     * AMapOptions.LOGO_POSITION_BOTTOM_LEFT:Logo位置（地图左下角）
     * AMapOptions.LOGO_POSITION_BOTTOM_RIGHT:Logo位置（地图右下角）
     */
    fun setLogoPosition(position: Int) {
        logoPosition = getLogoPosition(position)
        TrafficSystemStore.save(TrafficSystemStore.KEY_MAP_LOGO_POSITION, position)
    }

    fun getLogoPosition() = TrafficSystemStore.getInt(TrafficSystemStore.KEY_MAP_LOGO_POSITION)

    fun getLogoPosition(position: Int): Int {
        return when (position) {
            0 -> AMapOptions.LOGO_POSITION_BOTTOM_LEFT
            1 -> AMapOptions.LOGO_POSITION_BOTTOM_CENTER
            2 -> AMapOptions.LOGO_POSITION_BOTTOM_RIGHT
            else -> AMapOptions.LOGO_POSITION_BOTTOM_LEFT
        }
    }

    fun getIndexOfLogoPosition(): Int {
        return when (getLogoPosition()) {
            AMapOptions.LOGO_POSITION_BOTTOM_LEFT -> 0
            AMapOptions.LOGO_POSITION_BOTTOM_CENTER -> 1
            AMapOptions.LOGO_POSITION_BOTTOM_RIGHT -> 2
            else -> 0
        }
    }


    fun getLogoPositionsDescriptions(): Array<String> {
        return arrayOf("左下角", "底部居中", "右下角")
    }
}