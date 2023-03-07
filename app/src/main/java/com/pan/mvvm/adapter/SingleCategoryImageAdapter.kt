package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.SingleCateImageRowBinding
import com.pan.mvvm.models.SingleCateItems

class SingleCategoryImageAdapter (private val images: List<SingleCateItems>): RecyclerView.Adapter<SingleCategoryImageAdapter.MyImagesHolder>(){


    class MyImagesHolder(imageView: View): RecyclerView.ViewHolder(imageView) {
        val binding= SingleCateImageRowBinding.bind(imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImagesHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.single_cate_image_row,parent,false)
        return MyImagesHolder(view)
    }

    override fun onBindViewHolder(holder: MyImagesHolder, position: Int) {

        val imageList = images[position]

        Glide.with(holder.itemView.context).load(imageList.filePath).centerCrop().into(
            holder.binding.imgRow
        )
    }

    override fun getItemCount(): Int =images.size
}