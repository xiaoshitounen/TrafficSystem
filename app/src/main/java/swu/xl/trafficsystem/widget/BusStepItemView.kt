package swu.xl.trafficsystem.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_bus_step.view.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.CustomBusStep

class BusStepItemView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        View.inflate(context, R.layout.item_bus_step, this)
    }

    fun setStep(step: CustomBusStep) {
        //兜底方案：去除多余的)
        text.text = step.name.replace(")", "")
    }
}