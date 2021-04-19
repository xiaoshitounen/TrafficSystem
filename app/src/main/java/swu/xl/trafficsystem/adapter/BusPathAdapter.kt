package swu.xl.trafficsystem.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.route.BusPath
import swu.xl.trafficsystem.widget.BusPathItemView

class BusPathAdapter : RecyclerView.Adapter<BusPathAdapter.BusPathHolder>() {
    private val paths = mutableListOf<BusPath>()

    fun setBusPath(paths: List<BusPath>) {
        this.paths.clear()
        this.paths.addAll(paths)
        notifyDataSetChanged()
    }

    override fun getItemCount() = paths.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusPathHolder(BusPathItemView(parent.context))

    override fun onBindViewHolder(holder: BusPathHolder, position: Int) {
        val busPathView = holder.itemView as BusPathItemView
        busPathView.setBusPath(paths[position])
    }

    class BusPathHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}