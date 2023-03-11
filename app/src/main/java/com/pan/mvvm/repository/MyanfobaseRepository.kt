package com.pan.mvvm.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pan.mvvm.api.MyanFoBaseApi
import com.pan.mvvm.models.*
import com.pan.mvvm.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import javax.inject.Inject


class MyanfobaseRepository @Inject constructor(
    private val myanFoBaseApi: MyanFoBaseApi
) {
/*//check Internet connection
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
    }*/

    //favorite check is already true or not

    private val _checkFavoriteLiveData = MutableLiveData<NetworkResult<FavoriteCheckResponse>>()
    val favoriteCheckResponse: LiveData<NetworkResult<FavoriteCheckResponse>>
        get() = _checkFavoriteLiveData

    suspend fun checkFavoritePost(favoriteCheck: FavoriteCheck) {
        _checkFavoriteLiveData.postValue(NetworkResult.Loading())
        val response = myanFoBaseApi.checkFavoritePost(favoriteCheck)

        if (response.isSuccessful && response.body() != null) {
            _checkFavoriteLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorText = response.errorBody()!!.charStream().readText()
            val errorMessage = try {
                JSONObject(errorText).getString("message")
            } catch (e: JSONException) {
                errorText
            }
            _checkFavoriteLiveData.postValue(NetworkResult.Error(errorMessage))
        } else {
            _checkFavoriteLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }


    //for favorite
    private val _favoriteLiveData = MutableLiveData<NetworkResult<FavoriteResponse>>()
    val favoriteResponse: LiveData<NetworkResult<FavoriteResponse>>
        get() = _favoriteLiveData


    suspend fun addToFavorite(favoriteRequestModel: FavoriteRequestModel) {

        _favoriteLiveData.postValue(NetworkResult.Loading())
        val response = myanFoBaseApi.addToFavoritePost(favoriteRequestModel)

        if (response.isSuccessful && response.body() != null) {
            _favoriteLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorText = response.errorBody()!!.charStream().readText()
            val errorMessage = try {
                JSONObject(errorText).getString("message")
            } catch (e: JSONException) {
                errorText
            }
            _favoriteLiveData.postValue(NetworkResult.Error(errorMessage))
        } else {
            _favoriteLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }


    //put update user profile detail
    private val _updateProfileDetailResponseLiveData =
        MutableLiveData<NetworkResult<ProfileResponse>>()
    val updateProfileDetailResponse: LiveData<NetworkResult<ProfileResponse>>
        get() = _updateProfileDetailResponseLiveData

    suspend fun updateProfileDetail(
        id: String,
        profilePicture: File,
        username: RequestBody,
        email: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        address: RequestBody,
        bio: RequestBody
    ) {
        val profilePictureRequestBody = profilePicture
            .asRequestBody("image/jpg".toMediaTypeOrNull())
        //MultiplePartBody.Part is used to send also the actual file
        val profilePicturePart = MultipartBody.Part.createFormData(
            "profilePicture",
            profilePicture.name,
            profilePictureRequestBody
        )

        _updateProfileDetailResponseLiveData.postValue(NetworkResult.Loading())

        val response = myanFoBaseApi.updateProfileDetail(
            id,
            profilePicturePart, username, email, dob, gender, address, bio
        )
        val responseBody = response.body().toString()
        // Log.d("API_RESPONSE", responseBody)

        if (response.isSuccessful && response.body() != null) {
            _updateProfileDetailResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorText = response.errorBody()!!.charStream().readText()
            val errorMessage = try {
                JSONObject(errorText).getString("message")
            } catch (e: JSONException) {
                errorText
            }
            _updateProfileDetailResponseLiveData.postValue(NetworkResult.Error(errorMessage))
        } else {
            _updateProfileDetailResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


    //getUserLoginDetail
    private val _userLoginDetail = MutableLiveData<UserLoginDetailResponse>()
    val userLoginDetail: LiveData<UserLoginDetailResponse>
        get() = _userLoginDetail

    suspend fun getUserLoginDetailResponse(id: String) {
        val response = myanFoBaseApi.getUserLoginDetail(id)
        if (response.isSuccessful && response.body() != null) {
            _userLoginDetail.postValue(response.body()!!)
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


    //get Single Category Items

    private val _getSingleCateLiveData = MutableLiveData<NetworkResult<List<SingleCateItem>>>()
    val getSingleCateItem: LiveData<NetworkResult<List<SingleCateItem>>>
        get() = _getSingleCateLiveData

    suspend fun getSingleCateItem(cateName: String) {
        _getSingleCateLiveData.postValue(NetworkResult.Loading())

        val response = myanFoBaseApi.getSingleCate(cateName)
        if (response.isSuccessful && response.body() != null) {
            //getting from Api
            _getSingleCateLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _getSingleCateLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _getSingleCateLiveData.postValue(NetworkResult.Error("Something went wrong"))
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
}

