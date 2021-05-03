package swu.xl.trafficsystem.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swu.xl.trafficsystem.sql.entity.LoveEntity
import swu.xl.trafficsystem.widget.LoveItemView

interface OnUserLovePathClickListener {
    fun onPathClick(data: LoveEntity)
}

class UserLoveAdapter : RecyclerView.Adapter<UserLoveAdapter.BusLoveHolder>() {
    private val data = mutableListOf<LoveEntity>()
    private var listener: OnUserLovePathClickListener? = null

    fun setData(data: List<LoveEntity>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addOnBusPathClickListener(listener: OnUserLovePathClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusLoveHolder(LoveItemView(parent.context))

    override fun onBindViewHolder(holder: BusLoveHolder, position: Int) {
        val view = holder.itemView as LoveItemView
        view.setData(data[position])
        view.setOnClickListener { listener?.onPathClick(data[position]) }
    }

    class BusLoveHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}