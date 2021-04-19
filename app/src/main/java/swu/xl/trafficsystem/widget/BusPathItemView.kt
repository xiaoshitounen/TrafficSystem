package swu.xl.trafficsystem.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.amap.api.services.route.BusPath
import kotlinx.android.synthetic.main.item_bus_path.view.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.amap.AMapUtil

class BusPathItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        View.inflate(context, R.layout.item_bus_path, this)
    }

    fun setBusPath(path: BusPath) {
        
        text.text = AMapUtil.getBusPathTitle(path)
    }
}