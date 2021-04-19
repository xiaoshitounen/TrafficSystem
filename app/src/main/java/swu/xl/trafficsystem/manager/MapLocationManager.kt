package swu.xl.trafficsystem.manager

import com.amap.api.services.core.LatLonPoint
import swu.xl.trafficsystem.model.MapLine
import swu.xl.trafficsystem.model.MapLocation

object MapRouteManager {
    var city ="北京"
    private var startPoint = MapLocation(LatLonPoint(39.942295, 116.335891), "默认起点")
    private var endPoint: MapLocation = MapLocation(LatLonPoint(39.995576, 116.481288), "默认终点")
    private var currentPoint = startPoint

    fun setCurrent(current: MapLocation) {
        currentPoint = current
    }

    fun setLine(end: MapLocation) {
        startPoint = currentPoint
        endPoint = end
    }

    fun getLine(): MapLine {
        return MapLine(startPoint, endPoint)
    }

    fun changeStartLocation(start: MapLocation) {
        startPoint = start
    }

    fun changeEndLocation(end: MapLocation) {
        endPoint = end
    }
}