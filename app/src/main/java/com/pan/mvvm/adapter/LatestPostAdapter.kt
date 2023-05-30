package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.databinding.LatestpostRowBinding
import com.pan.mvvm.fragments.MainFragmentDirections
import com.pan.mvvm.models.LatestPostItem

class LatestPostAdapter : RecyclerView.Adapter<LatestPostAdapter.MyViewHolder>() {

    private var latestPostList = emptyList<LatestPostItem>()
    private var filterLatestPostList = emptyList<LatestPostItem>()

    fun setLatestPostItem(latestPostItem: List<LatestPostItem>) {
        this.latestPostList = latestPostItem
        this.filterLatestPostList = latestPostItem
    }
 fun filter(query: String?) {
        query?.let {
            filterLatestPostList = latestPostList.filter { latestPostItem ->
                latestPostItem.title.contains(query, ignoreCase = true)
            }
        }
            ?: run {
                filterLatestPostList = latestPostList
            }
        notifyDataSetChanged()
    }

    class MyViewHolder(val binding: LatestpostRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LatestpostRowBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val latestPostList = filterLatestPostList[position]
        holder.binding.cateTitle.text= latestPostList.cateName
        holder.binding.latestUserName.text = latestPostList.username
        holder.binding.latestDescription.text = latestPostList.description
        holder.binding.timeCreate.text = latestPostList.createdAt.substring(0, 10)
        Glide.with(holder.itemView.context).load(latestPostList.userprofile)
            .into(holder.binding.latestProfile)

        Glide.with(holder.itemView.context).load(latestPostList.files[0].filePath)
            .into(holder.binding.latestBackgroundImage)


        holder.binding.root.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLatestPostDetailFragment(latestPostList)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = filterLatestPostList.size
}