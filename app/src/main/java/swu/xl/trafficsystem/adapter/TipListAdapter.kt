package swu.xl.trafficsystem.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.help.Tip
import swu.xl.trafficsystem.widget.TipItemView

interface OnTipClickListener {
    fun onTipClick(tip: Tip)
}

class TipListAdapter : RecyclerView.Adapter<TipListAdapter.TipHolder>() {
    private var listener: OnTipClickListener? = null
    private val tips = mutableListOf<Tip>()

    fun setTips(tips: List<Tip>) {
        this.tips.clear()
        this.tips.addAll(tips)
        notifyDataSetChanged()
    }

    fun addOnTipClickListener(listener: OnTipClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = tips.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TipHolder(TipItemView(parent.context))

    override fun onBindViewHolder(holder: TipHolder, position: Int) {
        val tipView = holder.itemView as TipItemView
        tipView.setTip(tips[position])
        tipView.setOnClickListener { listener?.onTipClick(tips[position]) }
    }

    class TipHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}