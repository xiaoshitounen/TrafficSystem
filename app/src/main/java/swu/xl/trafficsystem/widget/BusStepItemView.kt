package swu.xl.trafficsystem.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_bus_step.view.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.CustomBusStep
import swu.xl.trafficsystem.constant.Constant

class BusStepItemView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        View.inflate(context, R.layout.item_bus_step, this)
    }

    fun setStep(step: CustomBusStep, isLast: Boolean) {
        if (isLast) next.visibility = View.GONE
        else next.visibility = View.VISIBLE

        //兜底方案：不是铁路线去除多余的)
        if (step.type != Constant.STEP_TYPE_RAILWAY) {
            text.text = step.name.replace(")", "")
        } else {
            text.text = step.name
        }
        if (step.type == Constant.STEP_TYPE_SUBWAY) {
            text.setBackgroundColor(getStepColor(step.name))
        } else {
            text.setBackgroundColor(Color.parseColor("#2196f3"))
        }
    }

    private fun getStepColor(step: String): Int {
        if (step.contains("1")) {
            return Color.parseColor("#FFFF6F00")
        }

        if (step.contains("2")) {
            return Color.parseColor("#8BC34A")
        }

        if (step.contains("3")) {
            return Color.parseColor("#FFEB3B")
        }

        if (step.contains("4")) {
            return Color.parseColor("#FF9800")
        }

        if (step.contains("5")) {
            return Color.parseColor("#FFC107")
        }

        if (step.contains("6")) {
            return Color.parseColor("#FF5722")
        }

        if (step.contains("7")) {
            return Color.parseColor("#8BC34A")
        }

        if (step.contains("8")) {
            return Color.parseColor("#9C27B0")
        }

        if (step.contains("9")) {
            return Color.parseColor("#E91E63")
        }

        if (step.contains("10")) {
            return Color.parseColor("#009688")
        }

        if (step.contains("11")) {
            return Color.parseColor("#DCE775")
        }

        if (step.contains("12")) {
            return Color.parseColor("#CDDC39")
        }

        if (step.contains("13")) {
            return Color.parseColor("#FFC107")
        }

        if (step.contains("14")) {
            return Color.parseColor("#F44336")
        }

        if (step.contains("15")) {
            return Color.parseColor("#00BCD4")
        }

        if (step.contains("16")) {
            return Color.parseColor("#4CAF50")
        }

        if (step.contains("17")) {
            return Color.parseColor("#FF6F00")
        }

        if (step.contains("18")) {
            return Color.parseColor("#00559e")
        }

        if (step.contains("19")) {
            return Color.parseColor("#E22C48")
        }

        if (step.contains("20")) {
            return Color.parseColor("#FF6AC38A")
        }

        return Color.parseColor("#FFFF6F00")
    }
}