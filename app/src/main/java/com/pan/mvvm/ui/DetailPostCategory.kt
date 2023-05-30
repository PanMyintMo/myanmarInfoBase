package com.pan.mvvm.ui

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.R
import com.pan.mvvm.adapter.ImageAdapter
import com.pan.mvvm.databinding.ActivityDetailPostCategoryBinding
import com.pan.mvvm.models.FavoriteCheck
import com.pan.mvvm.models.FavoriteRequestModel
import com.pan.mvvm.models.SingleCateItem
import com.pan.mvvm.utils.Constants.CATA_KEY
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailPostCategory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostCategoryBinding
    private val myanFobaseViewModel by viewModels<MyanfobaseViewModel>()
    private var favorite: Boolean = false
    private var removeFavoriteCheck: Boolean = false
    private var user: String? = null
    private  var singleCateItem: SingleCateItem?= null
 @Inject
    lateinit var tokenManager: TokenManager
    private var imageAdapter: ImageAdapter? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //to get post id
        if (intent.hasExtra("POST_ID")) {
            id = intent.extras?.getString("POST_ID")
        //Toast.makeText(this@DetailPostCategory, "$id", Toast.LENGTH_SHORT).show()
        }

        if (intent.hasExtra(CATA_KEY)) {
            singleCateItem = intent.getParcelableExtra(CATA_KEY)
        }

        binding.btnFavorite.setOnClickListener {
            checkAlreadyFavoritedOrNot()
        }

        binding.btnShare.setOnClickListener {
            shareForApplication()
        }

        //observe
        bindObserverForCategoryDetailPost()
        //get Detail category post
        myanFobaseViewModel.getCategoryDetailPost(id.toString())


        user = tokenManager.getId() ?: "UserId"
        //check favorite or not
        myanFobaseViewModel.checkUserFavorite(FavoriteCheck(id.toString(), user!!))
        //observe already favorite or not
        observeFavoriteCheck()

        //observe for favorite remove
        observeRemoveFavorite()

    }

    private fun shareForApplication() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val body = "Download this app"
        val sub = "http://play.google.com"
        intent.putExtra(Intent.EXTRA_TEXT, body)
        intent.putExtra(Intent.EXTRA_TEXT, sub)
        startActivity(Intent.createChooser(intent, "Share using"))

    }

    private fun observeRemoveFavorite() {
        myanFobaseViewModel.removeFavoriteData.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    removeFavoriteCheck = response.data?.success!!

                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {

                }

                else -> {}
            }

        }
    }

    private fun checkAlreadyFavoritedOrNot() {
        favorite = !favorite
        if (favorite) {
            myanFobaseViewModel.addToFavorite(
                FavoriteRequestModel(
                    id.toString(),
                    user.toString(),
                    singleCateItem?.title ?: "",
                    singleCateItem?.cateId ?: "",
                    singleCateItem?.cateName ?: "",
                    singleCateItem?.files?.get(0)?.filePath.toString()
                )
            )
            binding.imageView6.setColorFilter(
                ContextCompat.getColor(this, R.color.red),
                PorterDuff.Mode.SRC_IN
            )
            Toast.makeText(this@DetailPostCategory, "Add Success favorite", Toast.LENGTH_SHORT)
                .show()
        } else {
            myanFobaseViewModel.removeFromFavorite(FavoriteCheck(id.toString(), user.toString()))
            binding.imageView6.clearColorFilter()
            Toast.makeText(this@DetailPostCategory, "Remove Success favorite", Toast.LENGTH_SHORT)
                .show()

            //Notify GetAllFavoriteAdapter about unfavorite

            val resultIntent=Intent().apply {
                putExtra("POST_ID",id.toString())
            }
            setResult(Activity.RESULT_OK,resultIntent)
        }
    }

    private fun observeFavoriteCheck() {
        myanFobaseViewModel.checkFavoriteData.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    favorite = response.data?.favorited!!
                    if (favorite) {
                        binding.imageView6.setColorFilter(
                            ContextCompat.getColor(this, R.color.red),
                            PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        binding.imageView6.clearColorFilter()
                    }

                  //  Toast.makeText(this, "$favorite", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(this@DetailPostCategory)
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
                else -> {}
            }

        }
    }

    private fun bindObserverForCategoryDetailPost() {
        myanFobaseViewModel.getCategoryDetailPost.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.detailName.text = response.data?.username ?: "User Name"
                    binding.detailDate.text =
                        response.data?.timeCreated?.subSequence(0, 10) ?: "Date Posted"
                    binding.detailTitle.text = response.data?.title ?: "Title"
                    binding.detailDescription.text =
                        response.data?.description ?: "Description"

                    Glide.with(this).load(response.data?.userprofile).into(binding.detailProfile)
                    imageAdapter = ImageAdapter(response.data?.files ?: emptyList())
                    binding.detailImages.adapter = imageAdapter
                    binding.detailImages.layoutManager = LinearLayoutManager(
                        this@DetailPostCategory,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(this@DetailPostCategory)
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()

                }
                else -> {
                    Toast.makeText(
                        this@DetailPostCategory,
                        "${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}