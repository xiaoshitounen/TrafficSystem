package swu.xl.trafficsystem.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.route.BusPath
import kotlinx.android.synthetic.main.item_bus_path.view.*
import swu.xl.trafficsystem.widget.BusPathItemView


interface OnBusPathClickListener {
    fun onBusPathClick(path: BusPath)
}

class BusPathAdapter : RecyclerView.Adapter<BusPathAdapter.BusPathHolder>() {
    private var downTime = 0L
    private var upTime = 0L
    private var listener: OnBusPathClickListener? = null
    private val paths = mutableListOf<BusPath>()

    fun setBusPath(paths: List<BusPath>) {
        this.paths.clear()
        this.paths.addAll(paths)
        notifyDataSetChanged()
    }

    fun addOnBusPathClickListener(listener: OnBusPathClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = paths.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusPathHolder(BusPathItemView(parent.context))

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: BusPathHolder, position: Int) {
        val busPathView = holder.itemView as BusPathItemView
        busPathView.setBusPath(paths[position])
        busPathView.setOnClickListener { listener?.onBusPathClick(paths[position]) }
        busPathView.route_step_list.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downTime = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP -> {
                    upTime = System.currentTimeMillis()

                    if (upTime - downTime <= 100) {
                        holder.itemView.performClick()
                    }
                }
            }
            false
        }
    }

    class BusPathHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}