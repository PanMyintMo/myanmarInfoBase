package com.pan.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pan.mvvm.R
import com.pan.mvvm.adapter.CurrencyRowAdapter
import com.pan.mvvm.adapter.FuelRateAdapter
import com.pan.mvvm.databinding.ActivityCurrencyExchangeBinding
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyExchangeActivity : AppCompatActivity() {
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private var currencyRowAdapter: CurrencyRowAdapter? = null
    private var fuelRowAdapter: FuelRateAdapter? = null

    private lateinit var binding: ActivityCurrencyExchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //custom toolbar
        setSupportActionBar(binding.toolbar)
        //Initialize the adapter
        currencyRowAdapter = CurrencyRowAdapter()
        fuelRowAdapter = FuelRateAdapter()

        val actionBar = supportActionBar
        actionBar?.title = "Currency Rate"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        //bindObserverCurrencyRate
        bindObserverForCurrencyRate()
        //bindObserverFuelRate
        bindObserverFuelRate()
        myanfobaseViewModel.getGoldAndFuel()
        myanfobaseViewModel.getAllCurrencyRate()
    }

    private fun bindObserverFuelRate() {
        myanfobaseViewModel.getGoldAndFuelResponseLiveData.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val fuelList = response.data
                    fuelList.let {
                        binding.fuelRecycler.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    }
                    fuelList.let {
                        it?.let { it1 -> fuelRowAdapter?.setFuelList(it1) }
                    }
                    binding.fuelRecycler.adapter = fuelRowAdapter
                    fuelRowAdapter?.notifyDataSetChanged()
                }
                else -> {}
            }
        }
    }

    private fun bindObserverForCurrencyRate() {
        myanfobaseViewModel.getCurrencyRateLiveData.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val currencyList = response.data
                    currencyList.let {
                        binding.currencyRecycler.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    }

                    currencyList?.let { currencyRowAdapter?.setCurrencyList(it) }
                    binding.currencyRecycler.adapter = currencyRowAdapter
                    currencyRowAdapter?.notifyDataSetChanged()
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {

                }
                else -> {}
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}