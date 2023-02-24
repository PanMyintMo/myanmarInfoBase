package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.databinding.LatestpostRowBinding
import com.pan.mvvm.models.LatestPostItem

class LatestPostAdapter : RecyclerView.Adapter<LatestPostAdapter.MyViewHolder>() {

    private var latestPostList = emptyList<LatestPostItem>()

    fun setLatestPostItem(latestPostItem:List<LatestPostItem>){
        this.latestPostList=latestPostItem
    }


    class MyViewHolder(val binding: LatestpostRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LatestpostRowBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val latestPostItem = latestPostList[position]
        holder.binding.latestUserName.text = latestPostItem.username
        holder.binding.latestDescription.text = latestPostItem.description
        holder.binding.timeCreate.text = latestPostItem.createdAt.substring(0, 10)
        Glide.with(holder.itemView.context).load(latestPostItem.userprofile)
            .into(holder.binding.latestProfile)

        Glide.with(holder.itemView.context).load(latestPostItem.files[0].filePath)
            .into(holder.binding.latestBackgroundImage)
    }

    override fun getItemCount(): Int = latestPostList.size
}