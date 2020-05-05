package com.android.filterexample.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.filterexample.R
import com.android.filterexample.model.Image
import com.bumptech.glide.Glide
import com.android.filterexample.ui.adapter.ImagesAdapter.DataViewHolder
import kotlinx.android.synthetic.main.item_image.view.imageViewAvatar

class ImagesAdapter(private val images: ArrayList<Image>,
                    private val selectedItems: ArrayList<Image>) :
    RecyclerView.Adapter<DataViewHolder>() {

    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(image: Image) {
            itemView.apply {
                Glide.with(imageViewAvatar.context)
                    .load(image.url)
                    .fitCenter()
                    .placeholder(itemView.context.getDrawable(R.drawable.ic_loading))
                    .into(imageViewAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false))

    override fun getItemCount(): Int = selectedItems.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(selectedItems[position])
    }

    fun addImages(images: List<Image>) {
        this.images.apply {
            clear()
            addAll(images)
        }
        addSelectedImages(images)
    }

    fun addSelectedImages(filteredImages: List<Image>) {
        this.selectedItems.apply {
            clear()
            addAll(if(filteredImages.isNotEmpty()) filteredImages else images)
        }
    }

    fun filterImages(filters: List<String>): List<Image> {
        return images.filter {
            filters.contains(it.category)
        }
    }
}