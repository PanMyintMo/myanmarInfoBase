package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.databinding.PopularRowBinding
import com.pan.mvvm.fragments.MainFragmentDirections
import com.pan.mvvm.models.PopularItem

class PopularPostAdapter : RecyclerView.Adapter<PopularPostAdapter.MyViewHolder>() {

    private var popularList = emptyList<PopularItem>()

    fun setPopularItem(popularList:List<PopularItem>){
        this.popularList= popularList
    }


    class MyViewHolder(val binding: PopularRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PopularRowBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val popularList=popularList[position]
        holder.binding.popularTitle.text= popularList.title
        Glide.with(holder.itemView.context).load(popularList.files[0].filePath).into(holder.binding.postImage)

        holder.binding.root.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToPopularPostDetailFragment(popularList)
            holder.itemView.findNavController().navigate(action)

        }
    }

    override fun getItemCount(): Int =popularList.size
}