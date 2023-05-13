package com.pan.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pan.mvvm.R
import com.pan.mvvm.databinding.CurrencyRowBinding
import com.pan.mvvm.models.CurrencyResponseItem

class CurrencyRowAdapter : RecyclerView.Adapter<CurrencyRowAdapter.MyViewHolder>() {

    private var currencyResponse = emptyList<CurrencyResponseItem>()
    fun setCurrencyList(currencyList: List<CurrencyResponseItem>) {
        this.currencyResponse = currencyList

    }


    class MyViewHolder(private val binding: CurrencyRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currencyResponse: CurrencyResponseItem) {
            binding.currencyRate.text = currencyResponse.price.toString()
            binding.currencyTxt.text = currencyResponse.name

            Glide.with(itemView.context).load(currencyResponse.image).error(R.drawable.ic_baseline_favorite)
                   .into(binding.currencyImg)
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CurrencyRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currencyResponse = currencyResponse[position]
        holder.bind(currencyResponse)
    }

    override fun getItemCount(): Int = currencyResponse.size
}