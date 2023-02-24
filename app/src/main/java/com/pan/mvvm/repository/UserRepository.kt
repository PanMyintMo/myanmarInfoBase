package com.pan.mvvm.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pan.mvvm.api.UserApi
import com.pan.mvvm.models.LoginRequestModel
import com.pan.mvvm.models.LoginResponse
import com.pan.mvvm.models.RegisterRequestModel
import com.pan.mvvm.models.RegisterResponse
import com.pan.mvvm.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _loginResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val loginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _loginResponseLiveData


    private val _registerResponseLiveData = MutableLiveData<NetworkResult<RegisterResponse>>()
    val registerResponseLiveData: LiveData<NetworkResult<RegisterResponse>>
        get() = _registerResponseLiveData


    suspend fun registerUser(registerRequestModel: RegisterRequestModel) {

        _registerResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signup(registerRequestModel)
        registerHandleResponse(response)

         Log.d(TAG,response.body().toString())
    }

    private fun registerHandleResponse(response: Response<RegisterResponse>) {
        if (response.isSuccessful) {
            _registerResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _registerResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _registerResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun loginUser(loginRequestModel: LoginRequestModel) {
        _loginResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signIn(loginRequestModel)

        handleLoginResponse(response)

        //  Log.d(TAG,response.body().toString())
    }

    private fun handleLoginResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _loginResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _loginResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _loginResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}