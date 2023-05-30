package com.pan.mvvm.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.adapter.GetAllFavoriteAdapter
import com.pan.mvvm.databinding.ActivityFavoriteBinding
import com.pan.mvvm.models.UserRequestFavorite
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private lateinit var binding: ActivityFavoriteBinding
    private var favoriteAdapter: GetAllFavoriteAdapter? = null

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = tokenManager.getId() ?: "User"
        Log.d("USER", user)
        //Toast.makeText(this@FavoriteActivity, user, Toast.LENGTH_SHORT).show()

        favoriteAdapter = GetAllFavoriteAdapter(unfavoriteContract)

        binding.favoriteRecycler.layoutManager = LinearLayoutManager(
            this@FavoriteActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.favoriteRecycler.adapter = favoriteAdapter

        myanfobaseViewModel.getAllFavPost(UserRequestFavorite(user))
        observeAllFavoritePost()
    }

    private fun observeAllFavoritePost() {
        myanfobaseViewModel.getAllFavPostLiveData.observe(this@FavoriteActivity) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val favoriteResponseList = response.data?.files

                    favoriteResponseList?.let {
                        favoriteAdapter?.setFavoriteResponse(favoriteResponseList)
                    }
                }

                is NetworkResult.Loading -> {
                    // Handle loading state if needed
                }

                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(this@FavoriteActivity)
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }

                else -> {
                    // Handle other states if needed
                }
            }
        }
    }

    private val unfavoriteContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val postId = result.data?.getStringExtra("POST_ID")
            if (postId != null) {
                val position = favoriteAdapter?.favoriteResponseList?.indexOfFirst { it.postId == postId }
                if (position != -1) {
                    position?.let { favoriteAdapter?.removeFavorite(it) }
                }
            }
        }
    }

}
