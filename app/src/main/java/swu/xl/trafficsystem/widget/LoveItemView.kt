package swu.xl.trafficsystem.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_user_love.view.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.sql.entity.LoveEntity

class LoveItemView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        View.inflate(context, R.layout.item_user_love, this)
    }

    fun setData(data: LoveEntity) {
        start.text = data.start
        end.text = data.end
        time.text = data.time
    }
}