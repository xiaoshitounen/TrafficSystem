package swu.xl.trafficsystem.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.cardview.widget.CardView
import com.amap.api.services.help.Tip
import com.lxj.xpopup.util.XPopupUtils
import kotlinx.android.synthetic.main.item_tip.view.*
import swu.xl.trafficsystem.R

class TipItemView : CardView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            setMargins(0, 0, 0, XPopupUtils.dp2px(context, 4F))
        }
        radius = XPopupUtils.dp2px(context, 8F).toFloat()
        View.inflate(context, R.layout.item_tip, this)
    }

    fun setTip(tip: Tip) {
        name.text = tip.name
        if (tip.address.isNotEmpty()) {
            detail.text = "${tip.district}-${tip.address}"
        } else {
            detail.text = tip.district
        }
    }
}