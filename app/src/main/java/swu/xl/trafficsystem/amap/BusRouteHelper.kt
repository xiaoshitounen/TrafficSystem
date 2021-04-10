package swu.xl.trafficsystem.amap

import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RouteSearch

object BusRouteHelper {
    //城市
    private var city = "北京"
    //起点
    private var startPoint = LatLonPoint(39.942295, 116.335891)
    //终点
    private var endPoint = LatLonPoint(39.995576, 116.481288)

    fun searchBusRouteResult() = RouteSearch.BusRouteQuery(RouteSearch.FromAndTo(startPoint, endPoint), 0, city, 0)
}