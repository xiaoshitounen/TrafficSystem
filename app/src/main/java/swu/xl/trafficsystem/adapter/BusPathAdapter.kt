package swu.xl.trafficsystem.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.route.BusPath
import swu.xl.trafficsystem.widget.BusPathItemView

interface OnBusPathClickListener {
    fun onBusPathClick(path: BusPath)
}

class BusPathAdapter : RecyclerView.Adapter<BusPathAdapter.BusPathHolder>() {
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

    override fun onBindViewHolder(holder: BusPathHolder, position: Int) {
        val busPathView = holder.itemView as BusPathItemView
        busPathView.setBusPath(paths[position])
        busPathView.setOnClickListener { listener?.onBusPathClick(paths[position]) }
    }

    class BusPathHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}