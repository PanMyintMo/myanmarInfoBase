package com.pan.mvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.PopularPostdetailImageRowBinding
import com.pan.mvvm.models.FileX

class PopularPostImageAdapter(
    private var context: Context,
    private var popularImageList: List<FileX>
) : RecyclerView.Adapter<PopularPostImageAdapter.MyImageDetailHolder>() {
    class MyImageDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PopularPostdetailImageRowBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageDetailHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.popular_postdetail_image_row, parent, false)

        return MyImageDetailHolder(view)
    }

    override fun onBindViewHolder(holder: MyImageDetailHolder, position: Int) {
        val imageList = popularImageList[position]
        Glide.with(context).load(imageList.filePath).into(holder.binding.detailImage)
    }

    override fun getItemCount(): Int = popularImageList.size
}