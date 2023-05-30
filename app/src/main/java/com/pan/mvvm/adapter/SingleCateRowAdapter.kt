package com.pan.mvvm.adapter
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.databinding.SingleCategoryRowItemBinding
import com.pan.mvvm.models.SingleCateItem
import com.pan.mvvm.ui.DetailPostCategory
import com.pan.mvvm.utils.Constants.CATA_KEY

class SingleCateRowAdapter :
    RecyclerView.Adapter<SingleCateRowAdapter.SingleCateRowViewHolder>(){

    private var singleCateItemList = emptyList<SingleCateItem>()
    private var cateFilterList = emptyList<SingleCateItem>()
    private lateinit var singleCategoryImageAdapter: SingleCategoryImageAdapter

    fun setLatestPostItem(singleCateItemList: List<SingleCateItem>) {
        this.singleCateItemList = singleCateItemList
        this.cateFilterList = singleCateItemList
    }

    fun filter(query: String?) {
        query?.let {
            cateFilterList = singleCateItemList.filter { singleCateItem ->
                singleCateItem.title.contains(query, ignoreCase = true)
            }
        } ?: run {
            cateFilterList = singleCateItemList
        }
        notifyDataSetChanged()
    }

    class SingleCateRowViewHolder(val binding: SingleCategoryRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleCateRowViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleCategoryRowItemBinding.inflate(inflater, parent, false)

        return SingleCateRowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleCateRowViewHolder, position: Int) {
        val singleCate = cateFilterList[position]
        holder.binding.cateTitle.text = singleCate.title
        holder.binding.cateUserName.text = singleCate.username
        holder.binding.postDate.text = singleCate.createdAt.substring(0, 10)
        holder.binding.cateDescription.text = singleCate.description
        // holder.binding.vCount.text = singleCate.viewcount.toString()

        holder.binding.cateDescription.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailPostCategory::class.java)
            intent.putExtra("POST_ID", singleCate.id)

            intent.putExtra(CATA_KEY, singleCate)

            try {
                holder.itemView.context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("StartActivityError", e.message ?: "Unknown error")
            }
        }

        Glide.with(holder.itemView.context).load(singleCate.userprofile)
            .into(holder.binding.singleImageProfile)


        singleCategoryImageAdapter = SingleCategoryImageAdapter(singleCate.files)
        holder.binding.slider.adapter = singleCategoryImageAdapter
        holder.binding.slider.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)

    }
    override fun getItemCount(): Int = cateFilterList.size

}




