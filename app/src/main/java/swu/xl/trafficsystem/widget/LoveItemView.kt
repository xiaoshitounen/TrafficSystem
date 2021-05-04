package swu.xl.trafficsystem.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_bus_path.view.*
import kotlinx.android.synthetic.main.item_user_love.view.*
import kotlinx.android.synthetic.main.item_user_love.view.route_step_list
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.BusStepAdapter
import swu.xl.trafficsystem.amap.AMapUtil
import swu.xl.trafficsystem.sql.entity.LoveEntity

class LoveItemView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val adapter = BusStepAdapter()

    init {
        View.inflate(context, R.layout.item_user_love, this)
        route_step_list.layoutManager = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        route_step_list.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    fun setData(data: LoveEntity) {
        start.text = "起点：${data.start}"
        end.text = "终点：${data.target}"
        time.text = data.time
        distance.text = data.walkDistance
        //构建路线
        adapter.setSteps(data.steps)
    }
}