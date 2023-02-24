package com.pan.mvvm.viewModel


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.pan.mvvm.database.PopularPostEntity
import com.pan.mvvm.models.PopularItem
import com.pan.mvvm.repository.MyanfobaseRepository
import com.pan.mvvm.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject


class MyanfobaseViewModel @Inject constructor(
    private val repository: MyanfobaseRepository,
    application: Application
) :
    AndroidViewModel(application) {
    /**Retrofit*/

    private val _getPopularLiveData = MutableLiveData<NetworkResult<List<PopularItem>>>()
    val getPopularItem: LiveData<NetworkResult<List<PopularItem>>>
        get() = _getPopularLiveData

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getAllPopularPost() {
        _getPopularLiveData.postValue(NetworkResult.Loading())

        if (hasInternetConnection()) {
            val response = repository.remote.getPopular()
            if (response.isSuccessful && response.body() != null) {
                val popularResponse = NetworkResult.Success(response.body()!!)
                val popularData = popularResponse.data
                _getPopularLiveData.postValue(popularResponse)

                if (popularData != null) {
                    for (i in popularData.iterator()) {
                        offlineCachePopularPostItem(i)
                    }
                }
            }
        }
    }

    //Room
    private fun offlineCachePopularPostItem(popularItem: PopularItem) {
        val popularPostEntity = PopularPostEntity(popularItem)
        insertPopularPostItem(popularPostEntity)
    }
    private fun insertPopularPostItem(popularPostEntity: PopularPostEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertPopularItem(popularPostEntity)
        }
    }

    //To check internet connection
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}


/*
@HiltViewModel
class MyanfobaseViewModel @Inject constructor(
    private val myanfobaseRepository: MyanfobaseRepository
) :
    ViewModel() {



    */
/**Retrofit*//*

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

    */
/**Room Database*//*

    //insert popularpost item

    fun insertPopularPostItem(popularPostEntity: PopularPostEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            myanfobaseRepository.insertPopularPostItem(popularPostEntity)
        }
    }
}*/
