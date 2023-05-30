package com.pan.mvvm.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pan.mvvm.R
import com.pan.mvvm.adapter.SingleCateRowAdapter
import com.pan.mvvm.databinding.ActivitySingleCategoryBinding
import com.pan.mvvm.models.FavoriteCheckResponse
import com.pan.mvvm.utils.Constants.CAT_KEY
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleCategoryActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var binding: ActivitySingleCategoryBinding
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private var singleCateRowAdapter: SingleCateRowAdapter? = null
    private var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup custom tool bar
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val cateName = bundle?.getString(CAT_KEY)
        supportActionBar?.title = cateName.toString()
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        // Use the ViewModel here
        myanfobaseViewModel.getSingleCateItem(cateName.toString())

        // Initialize adapter
        singleCateRowAdapter = SingleCateRowAdapter()
        // Set the adapter to your RecyclerView
        binding.cateRowRecycler.adapter = singleCateRowAdapter

        fetchSingleCateItem()
    }

    private fun fetchSingleCateItem() {
        myanfobaseViewModel.getAllCategorySingleLiveData.observe(this) { response ->
            // binding.progressBar.isVisible = false
            when (response) {
                is NetworkResult.Success -> {
                    val singleCateItemList = response.data
                   // Log.d("List","$singleCateItemList")
                    singleCateItemList?.let {
                        binding.cateRowRecycler.layoutManager =
                            LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        singleCateRowAdapter?.setLatestPostItem(it)
                        binding.cateRowRecycler.adapter = singleCateRowAdapter
                        singleCateRowAdapter?.notifyDataSetChanged()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    //  binding.progressBar.isVisible = true

                }
                else -> {}
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_category, menu)

        val search = menu?.findItem(R.id.searchCategory)
        searchView = search?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                singleCateRowAdapter?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                singleCateRowAdapter?.filter(newText)
                return true
            }
        })

        return true
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