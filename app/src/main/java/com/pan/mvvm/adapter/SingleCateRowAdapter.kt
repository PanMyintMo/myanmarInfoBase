package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.SingleCategoryRowItemBinding
import com.pan.mvvm.models.FavoriteRequestModel
import com.pan.mvvm.models.SingleCateItem

class SingleCateRowAdapter(private val listener: OnClickListener) :
    RecyclerView.Adapter<SingleCateRowAdapter.SingleCateRowViewHolder>() {

    private var singleCateItemList = emptyList<SingleCateItem>()
    private lateinit var singleCategoryImageAdapter: SingleCategoryImageAdapter





    fun setLatestPostItem(singleCateItemList: List<SingleCateItem>) {
        this.singleCateItemList = singleCateItemList
    }

    class SingleCateRowViewHolder(val binding: SingleCategoryRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleCateRowViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleCategoryRowItemBinding.inflate(inflater, parent, false)

        return SingleCateRowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleCateRowViewHolder, position: Int) {
        val singleCate = singleCateItemList[position]
        holder.binding.cateTitle.text = singleCate.title
        holder.binding.cateUserName.text = singleCate.username
        holder.binding.postDate.text = singleCate.createdAt.substring(0, 10)
        holder.binding.cateDescription.text = singleCate.description
        holder.binding.vCount.text = singleCate.viewcount.toString()




        holder.binding.btnFavorite.setOnClickListener {
            listener.viewModelOnClick(
                position,
                favoriteRequestModel =
                FavoriteRequestModel(
                    singleCate.id,
                    singleCate.username,
                    singleCate.title,
                    singleCate.cateId,
                    singleCate.cateName,
                    singleCate.files.toString()
                )
            )
            // Change background color of the favorite button to red


            holder.binding.favorite.setColorFilter(R.drawable.ic_red_favorite)
            holder.binding.btnFavorite.setBackgroundColor(R.color.green)
        }

        Glide.with(holder.itemView.context).load(singleCate.userprofile)
            .into(holder.binding.singleImageProfile)


        singleCategoryImageAdapter = SingleCategoryImageAdapter(singleCate.files)
        holder.binding.slider.adapter = singleCategoryImageAdapter
        holder.binding.slider.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)

    }

    override fun getItemCount(): Int = singleCateItemList.size


    interface OnClickListener {
        fun viewModelOnClick(position: Int, favoriteRequestModel: FavoriteRequestModel)



    }

}


