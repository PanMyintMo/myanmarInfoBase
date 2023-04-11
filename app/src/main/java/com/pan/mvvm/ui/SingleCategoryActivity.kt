package com.pan.mvvm.ui


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pan.mvvm.R
import com.pan.mvvm.adapter.SingleCateRowAdapter
import com.pan.mvvm.databinding.ActivitySingleCategoryBinding
import com.pan.mvvm.models.FavoriteCheck
import com.pan.mvvm.models.FavoriteRequestModel
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SingleCategoryActivity : AppCompatActivity(), SingleCateRowAdapter.OnClickListener {

    companion object {
        const val CAT_KEY = "cat_key"
    }

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var binding: ActivitySingleCategoryBinding
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private var singleCateRowAdapter: SingleCateRowAdapter? = null


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


        //initialize token Manager
        //   (application as MyanfobaseApplication).appComponent.inject(this)


        // Use the ViewModel here
        myanfobaseViewModel.getSingleCateItem(cateName.toString())

        // Initialize adapter
        singleCateRowAdapter = SingleCateRowAdapter(this, tokenManager)




        fetchSingleCateItem()


    }

    private fun fetchSingleCateItem() {

        myanfobaseViewModel.getAllCategorySingleLiveData.observe(this) { response ->
            // binding.progressBar.isVisible = false

            when (response) {
                is NetworkResult.Success -> {
                    val singleCateItemList = response.data
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


    override fun viewModelOnClick(position: Int, favoriteRequestModel: FavoriteRequestModel) {

        myanfobaseViewModel.addToFavorite(favoriteRequestModel)

        myanfobaseViewModel.addToFavoriteLiveData.observe(this) { favoriteResponse ->
            when (favoriteResponse) {
                is NetworkResult.Success -> {
                    val viewHolder =
                        binding.cateRowRecycler.findViewHolderForAdapterPosition(position)
                    // Get the view that contains the icon
                    val iconImageView = viewHolder?.itemView?.findViewById<View>(R.id.favorite)

                    // Get the icon drawable and set its tint color
                    val iconDrawable =
                        ContextCompat.getDrawable(this, R.drawable.ic_red_favorite)?.mutate()
                    iconDrawable?.setTint(ContextCompat.getColor(applicationContext, R.color.red))

                    // Set the tinted drawable as the icon
                    iconImageView?.setBackgroundDrawable(iconDrawable)

                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {}
                else -> {}
            }

        }
    }

    override fun checkFavorite(position: Int, favCheck: FavoriteCheck) {
        myanfobaseViewModel.checkUserFavorite(favCheck)
        myanfobaseViewModel.checkFavoriteData.observe(this) { favCheckResponse ->
            when (favCheckResponse) {
                is NetworkResult.Success -> {
                    val isFavorited =
                        favCheckResponse.data?.favorited // assuming the response contains a boolean indicating whether the item is favorited or not

                  //  Log.d("FAV",isFavorited.toString())
                    if (isFavorited!!) Color.RED else Color.WHITE // set the color based on whether the item is favorited or not
                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {}
            }
        }
    }
}