package com.pan.mvvm.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pan.mvvm.databinding.CategoryRowBinding
import com.pan.mvvm.models.CategoryItem
import com.pan.mvvm.ui.SingleCategoryActivity
import com.pan.mvvm.utils.Constants.CAT_KEY

class CategoryRowAdapter : RecyclerView.Adapter<CategoryRowAdapter.MyViewHolder>() {

    private var cateItem = emptyList<CategoryItem>()

    fun setCateNamList(cateItemList: List<CategoryItem>?) {
        this.cateItem = cateItemList ?: emptyList()

    }

    class MyViewHolder(private val binding: CategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cateItem: CategoryItem) {
            binding.btnCate.text = cateItem.catename

            binding.btnCate.setOnClickListener {
                val intent = Intent(itemView.context, SingleCategoryActivity::class.java)
                intent.putExtra(CAT_KEY, cateItem.catename)
                itemView.context.startActivity(intent)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cateItem = cateItem[position]
        holder.bind(cateItem)
    }

    override fun getItemCount() = cateItem.size



}


