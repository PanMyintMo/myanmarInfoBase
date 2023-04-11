package com.pan.mvvm.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.PickupimagesBinding

class PickUpImageAdapter(
    private val context: Context,
    private val imageList: ArrayList<Uri>
) : RecyclerView.Adapter<PickUpImageAdapter.MyViewHolder>() {



   inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

      val binding=PickupimagesBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pickupimages, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (imageList != null) {
            Glide.with(context)
                .load(imageList[position])
                .into(holder.binding.ivImage)
        }

    }

    override fun getItemCount(): Int = imageList?.size!!


}