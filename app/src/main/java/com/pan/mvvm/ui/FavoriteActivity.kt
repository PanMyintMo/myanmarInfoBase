package com.pan.mvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.databinding.ActivityFavoriteBinding
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private lateinit var binding: ActivityFavoriteBinding

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = tokenManager.getId() ?: "User"

        Toast.makeText(this@FavoriteActivity, user, Toast.LENGTH_SHORT).show()

        myanfobaseViewModel.getAllFavPost(user)
        //fetch all fav post
        observeAllFavoritePost()

        binding.favorite.setOnClickListener {
            val intent = Intent(this@FavoriteActivity, DetailPostCategory::class.java)
            startActivity(intent)
        }

        binding.removeFav.setOnClickListener {
           // myanfobaseViewModel.removeFromFavorite(FavoriteCheck())
        }

    }

    private fun observeAllFavoritePost() {
        myanfobaseViewModel.getAllFavPostLiveData.observe(this@FavoriteActivity) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.postTitle.text = response.data?.title ?: ""
                    Glide.with(this@FavoriteActivity).load(response.data?.files)
                        .into(binding.imgPost)
                    binding.postCate.text = response.data?.cateName ?: ""
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(this@FavoriteActivity)
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }

                else -> {}
            }

        }
    }
}