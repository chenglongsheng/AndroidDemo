package cn.cls.recycler_view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.cls.recycler_view.BeanAdapter.BeanViewHolder

class BeanAdapter(private var data: List<Bean>, private var context: Context) :
    RecyclerView.Adapter<BeanViewHolder>() {

    lateinit var mOnRecyclerItemClickListener: OnRecyclerItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeanViewHolder {
        val view = View.inflate(context, R.layout.list_item, null)
        return BeanViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeanViewHolder, position: Int) {
        holder.tv.text = data[position].name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnRecyclerItemClickListener(listener: OnRecyclerItemClickListener) {
        mOnRecyclerItemClickListener = listener
    }

    interface OnRecyclerItemClickListener {
        fun onRecyclerItemClick(position: Int)
    }

    inner class BeanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView
        init {
            tv = itemView.findViewById(R.id.tv)
            itemView.setOnClickListener{
                mOnRecyclerItemClickListener.onRecyclerItemClick(adapterPosition)
            }
        }

    }

}