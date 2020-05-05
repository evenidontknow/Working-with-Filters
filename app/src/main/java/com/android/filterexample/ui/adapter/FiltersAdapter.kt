package com.android.filterexample.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.filterexample.R
import com.android.filterexample.ui.adapter.FiltersAdapter.DataViewHolder
import com.android.filterexample.ui.listeners.FilterClickListener
import kotlinx.android.synthetic.main.item_filter.view.tv_filter_option

class FiltersAdapter(private val filters: ArrayList<String>,
      private val listener: FilterClickListener) :
    RecyclerView.Adapter<DataViewHolder>() {

    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(filter: String) {
            itemView.apply {
                tv_filter_option.text = filter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false))

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val filter = filters[position]
        holder.bind(filter)
        holder.itemView.apply {
            tv_filter_option.setOnClickListener {
                it.isSelected = !it.isSelected
                if(it.isSelected) {
                    it.background =
                        holder.itemView.context
                            .getDrawable(R.drawable.ic_rounded_rectangle_selected)
                    tv_filter_option.setTextColor(ContextCompat.getColor(this.context,R.color.white))
                } else {
                    it.background =
                        holder.itemView.context
                            .getDrawable(R.drawable.ic_rounded_rectangle_unselected)
                    tv_filter_option.setTextColor(ContextCompat.getColor(this.context,R.color.black))
                }
                listener.onFilterItemClicked(filter, it.isSelected)
            }

        }
    }
}