package com.pan.mvvm.repository


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pan.mvvm.api.MyanFoBaseApi
import com.pan.mvvm.database.PopularPostDatabase
import com.pan.mvvm.models.CategoryItem
import com.pan.mvvm.models.LatestPostItem
import com.pan.mvvm.models.PopularItem
import com.pan.mvvm.utils.NetworkResult
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.scopes.ViewModelScoped
import org.json.JSONObject
import javax.inject.Inject



@ViewModelScoped
class MyanfobaseRepository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote=remoteDataSource
    val local=localDataSource
}



/*
@ViewModelScoped
class MyanfobaseRepository @Inject constructor(
    private val myanFoBaseApi: MyanFoBaseApi,
    private val popularPostDatabase: PopularPostDatabase

) {
//check Internet connection
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager =
            getApplication(context).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


    //getCategoryName
    private val _getAllCategoryLiveData = MutableLiveData<NetworkResult<List<CategoryItem>>>()
    val getAllCategoryItem: LiveData<NetworkResult<List<CategoryItem>>>
        get() = _getAllCategoryLiveData

    suspend fun getAllCateName() {
        _getAllCategoryLiveData.postValue(NetworkResult.Loading())
        val response = myanFoBaseApi.getAllCategory()

        if (response.isSuccessful && response.body() != null) {
            _getAllCategoryLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _getAllCategoryLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _getAllCategoryLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    //getPopularPost
    private val _getPopularLiveData = MutableLiveData<NetworkResult<List<PopularItem>>>()
    val getPopularItem: LiveData<NetworkResult<List<PopularItem>>>
        get() = _getPopularLiveData

    suspend fun getAllPopularPost() {
        _getPopularLiveData.postValue(NetworkResult.Loading())
        val response = myanFoBaseApi.getPopular()
        if (response.isSuccessful && response.body() != null) {
            //getting from room database
            //popularPostDatabase.getPopularPostDao().insertPopularItem()
            //getting from api
            _getPopularLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _getPopularLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))

        } else {
            _getPopularLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    //getLatestPost
    private val _getLatestPostLiveData = MutableLiveData<NetworkResult<List<LatestPostItem>>>()
    val getLatestPostItem: LiveData<NetworkResult<List<LatestPostItem>>>
        get() = _getLatestPostLiveData


    suspend fun getLatestPostItem() {
        _getLatestPostLiveData.postValue(NetworkResult.Loading())
        val response = myanFoBaseApi.getLatestPost()
        if (response.isSuccessful && response.body() != null) {
            _getLatestPostLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _getLatestPostLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _getLatestPostLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


    suspend fun insertPopularPostItem(popularPostEntity: PopularPostEntity) {
       popularPostDatabase.getPopularPostDao().insertPopularItem(popularPostEntity)

    }
}*/
