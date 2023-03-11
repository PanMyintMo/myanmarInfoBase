package com.pan.mvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.LatestPostImageRowBinding
import com.pan.mvvm.models.ProfilePicture

class LatestPostImageAdapter(
    private var context: Context,
    private var latestPostImageList: List<ProfilePicture>
) : RecyclerView.Adapter<LatestPostImageAdapter.MyImageDetailHolder>() {
    class MyImageDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = LatestPostImageRowBinding.bind(itemView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageDetailHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.latest_post_image_row, parent, false)

        return LatestPostImageAdapter.MyImageDetailHolder(view)
    }

    override fun onBindViewHolder(holder: MyImageDetailHolder, position: Int) {

        val imageList = latestPostImageList[position]
        Glide.with(context).load(imageList.filePath).into(holder.binding.detailLatestImage)
    }

    override fun getItemCount(): Int = latestPostImageList.size
}