package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.CategoryDetailPostRowBinding
import com.pan.mvvm.models.ProfilePicture

class ImageAdapter(private val imageList: List<ProfilePicture>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CategoryDetailPostRowBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_detail_post_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(holder.itemView.context)
            .load(imageList[position].filePath)
            .into(holder.binding.imgDetailView)
    }
    override fun getItemCount(): Int = imageList.size
}