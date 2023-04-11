package com.pan.mvvm.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.pan.mvvm.models.FavoriteCheck
import com.pan.mvvm.models.FavoriteRequestModel
import com.pan.mvvm.repository.MyanfobaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MyanfobaseViewModel @Inject constructor(
    private val myanfobaseRepository: MyanfobaseRepository
) : ViewModel() {

    //check User's favorite
    val checkFavoriteData get() = myanfobaseRepository.favoriteCheckResponse
    fun checkUserFavorite(favoriteCheck: FavoriteCheck) {
        viewModelScope.launch {
            myanfobaseRepository.checkUserFavoritePost(favoriteCheck)
        }
    }

    //addToFavorite
    val addToFavoriteLiveData get() = myanfobaseRepository.favoriteResponse
    fun addToFavorite(favoriteRequestModel: FavoriteRequestModel) {
        viewModelScope.launch {
            myanfobaseRepository.addToFavorite(favoriteRequestModel)
        }
    }
    //create new post
    val createNewPostResponseDetail get() = myanfobaseRepository.newPostResponse
    fun createNewPost(
        cateId: RequestBody,
        cateName: RequestBody,
        title: RequestBody,
        description: RequestBody,
        files: List<File>
    ){
        viewModelScope.launch {
            try {
                myanfobaseRepository.createNewPost(
                    cateId,cateName,title,description,files
                )
            }
            catch (e:Exception){
                Log.d("Error", e.message.toString())
            }

        }
    }

    //get update user profile detail

    val updateUserProfileDetail get() = myanfobaseRepository.updateProfileDetailResponse
    fun updateUserProfileDetail(
        id: String,
        profilePicture: File,
        username: RequestBody,
        email: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        address: RequestBody,
        bio: RequestBody
    ) {
        viewModelScope.launch {
            try {
                myanfobaseRepository.updateProfileDetail(
                    id,
                    profilePicture,
                    username,
                    email,
                    dob,
                    gender,
                    address,
                    bio
                )
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }
    //get UserLoginDetailResponse
    val getLoginDetailResponseLiveData get() = myanfobaseRepository.userLoginDetail

    fun getUserLoginDetailResponse(id: String) {
        viewModelScope.launch {
            myanfobaseRepository.getUserLoginDetailResponse(id)
        }
    }

    //get SingleCategory Items

    val getAllCategorySingleLiveData get() = myanfobaseRepository.getSingleCateItem
    fun getSingleCateItem(cateName: String) {
        viewModelScope.launch {
            myanfobaseRepository.getSingleCateItem(cateName)
        }
    }

    //get CategoryName
    val getAllCategoryLiveData get() = myanfobaseRepository.getAllCategoryItem
    fun getAllCategoryItem() {
        viewModelScope.launch {
            myanfobaseRepository.getAllCateName()
        }
    }

    //getPopularPostItem
    val getAllPopuplarPostItemLiveData get() = myanfobaseRepository.getPopularItem
    fun getAllPopularPostItem() {
        viewModelScope.launch {
            myanfobaseRepository.getAllPopularPost()
        }
    }

    //getLatestPostItem
    val getAllLatestPostLiveData get() = myanfobaseRepository.getLatestPostItem
    fun getLatestPostItem() {
        viewModelScope.launch {
            myanfobaseRepository.getLatestPostItem()
        }
    }
}


