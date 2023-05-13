package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.FuelRowBinding
import com.pan.mvvm.models.GoldAndFuelResponseItem

class FuelRateAdapter : RecyclerView.Adapter<FuelRateAdapter.MyViewHolder>() {

    private var fuelResponseItem = emptyList<GoldAndFuelResponseItem>()

    fun setFuelList(fuelList: List<GoldAndFuelResponseItem>) {
        this.fuelResponseItem = fuelList
    }

    class MyViewHolder(private val binding: FuelRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fuelResponseItem: GoldAndFuelResponseItem) {
            binding.fuelRate.text = fuelResponseItem.category
            binding.fuelPriceTag.text = fuelResponseItem.price.toString()
            Glide.with(itemView.context).load(fuelResponseItem.category).error(R.drawable.ic_baseline_favorite)
                .into(binding.fuelImg)
        }

        companion object {
            fun form(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FuelRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder.form(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fuelResponseItemList = fuelResponseItem[position]
        holder.bind(fuelResponseItemList)
    }

    override fun getItemCount():Int = fuelResponseItem.size

}




