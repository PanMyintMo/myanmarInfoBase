package com.pan.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.pan.mvvm.R
import com.pan.mvvm.adapter.CategoryRowAdapter
import com.pan.mvvm.databinding.ActivityCategoryBinding
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var adapter: CategoryRowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //custom toolbar
        setSupportActionBar(binding.toolbar)

        val actionBar = supportActionBar
        actionBar?.title = "Categories"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        //Initialize adapter
        adapter = CategoryRowAdapter()
        bindObserverForCategoryName()
        //getAllCategoryName
        myanfobaseViewModel.getAllCategoryItem()
    }

    private fun bindObserverForCategoryName() {
        myanfobaseViewModel.getAllCategoryLiveData.observe(this) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    val cateItemList = response.data
                    cateItemList?.let {
                        binding.cateRecycler.layoutManager =
                            GridLayoutManager(
                                this,
                                2
                            )
                        binding.cateRecycler.setHasFixedSize(true)
                        adapter.setCateNamList(it)
                        binding.cateRecycler.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }

                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {
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