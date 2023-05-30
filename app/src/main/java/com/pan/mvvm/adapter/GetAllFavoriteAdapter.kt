package com.pan.mvvm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.databinding.FavoriteRowBinding
import com.pan.mvvm.models.FavoriteResponseData
import com.pan.mvvm.ui.DetailPostCategory

class GetAllFavoriteAdapter(val unfavoriteContract: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<GetAllFavoriteAdapter.MyViewHolder>() {

    var favoriteResponseList = emptyList<FavoriteResponseData>()
        private set

   fun removeFavorite(position: Int) {
        if (position >= 0 && position < favoriteResponseList.size) {
            favoriteResponseList = favoriteResponseList.toMutableList().apply {
                removeAt(position)
            }
            notifyItemRemoved(position)
        }
    }

    inner class MyViewHolder(val binding: FavoriteRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setFavoriteResponse(favoriteResponseList: List<FavoriteResponseData>) {

        this.favoriteResponseList = favoriteResponseList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteRowBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favoriteItems = favoriteResponseList[position]
        holder.binding.postTitle.text = favoriteItems.title
        holder.binding.postCate.text = favoriteItems.cateName

        Glide.with(holder.itemView.context).load(favoriteItems.files).into(holder.binding.imgPost)

        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailPostCategory::class.java)
            intent.putExtra("POST_ID", favoriteItems.postId)
            unfavoriteContract.launch(intent)

        }
    }

    override fun getItemCount(): Int = favoriteResponseList.size

}