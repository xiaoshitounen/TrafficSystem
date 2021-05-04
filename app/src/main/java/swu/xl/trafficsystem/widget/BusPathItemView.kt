package swu.xl.trafficsystem.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.services.route.BusPath
import kotlinx.android.synthetic.main.item_bus_path.view.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.BusStepAdapter
import swu.xl.trafficsystem.amap.AMapUtil

class BusPathItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val adapter = BusStepAdapter()

    init {
        View.inflate(context, R.layout.item_bus_path, this)
        route_step_list.layoutManager = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        route_step_list.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    fun setBusPath(path: BusPath) {
        //花费时间
        route_time.text = AMapUtil.getFriendlyTime(path.duration.toInt())
        //步行距离
        route_walk_distance.text = AMapUtil.getFriendlyLength(path.walkDistance.toInt())
        //站数
        var number = 0
        path.steps.forEach { step ->
            if (step.busLines.size != 0) {
                number += step.busLines[0].passStationNum + 1
            }
        }
        route_station_number.text = "${number}站"
        //花费金币
        route_cost.text = "${path.cost}元"
        //上站点
        if (path.steps[0].entrance == null) {
            route_station_start.text = "${path.steps[0].busLines[0].departureBusStation.busStationName}上车"
        } else {
            route_station_start.text = "${path.steps[0].busLines[0].departureBusStation.busStationName}（${path.steps[0].entrance.name}）进站"
        }
        //构建路线
        adapter.setSteps(AMapUtil.getBusStepList(path))
    }
}