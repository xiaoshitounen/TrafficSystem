package swu.xl.trafficsystem.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swu.xl.trafficsystem.widget.BusStepItemView

class BusStepAdapter : RecyclerView.Adapter<BusStepAdapter.BusStepHolder>() {
    private val steps = mutableListOf<CustomBusStep>()

    fun setSteps(steps: List<CustomBusStep>) {
        //TODO 处理不同地铁的背景颜色，根据包含的数字，写死
        this.steps.clear()
        this.steps.addAll(steps)
        notifyDataSetChanged()
    }

    override fun getItemCount() = steps.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusStepHolder(BusStepItemView(parent.context))

    override fun onBindViewHolder(holder: BusStepHolder, position: Int) {
        val view = holder.itemView as BusStepItemView
        view.setStep(steps[position])
    }

    class BusStepHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

data class CustomBusStep(val type: Int, val name: String)