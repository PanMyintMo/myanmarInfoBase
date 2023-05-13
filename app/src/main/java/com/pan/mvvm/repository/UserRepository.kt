package com.pan.mvvm.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pan.mvvm.api.UserApi
import com.pan.mvvm.models.*
import com.pan.mvvm.utils.NetworkResult
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    //login
    private val _loginResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val loginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _loginResponseLiveData

    //register
    private val _registerResponseLiveData = MutableLiveData<NetworkResult<RegisterResponse>>()
    val registerResponseLiveData: LiveData<NetworkResult<RegisterResponse>>
        get() = _registerResponseLiveData

    //reset password
    private val _resetPasswordLiveData = MutableLiveData<NetworkResult<ResetResponse>>()
    val resetPassword: MutableLiveData<NetworkResult<ResetResponse>> get() = _resetPasswordLiveData

    //actual password reset
    private val _passwordResetLiveData = MutableLiveData<NetworkResult<ResetResponse>>()
    val passwordReset: MutableLiveData<NetworkResult<ResetResponse>> get() = _passwordResetLiveData


    //register
    suspend fun registerUser(registerRequestModel: RegisterRequestModel) {
        _registerResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signup(registerRequestModel)
        registerHandleResponse(response)
        //  Log.d(TAG, response.body().toString())
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

    //login
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

    //reset password
    suspend fun resetPassword(resetRequestModel: ResetRequestModel) {
        _resetPasswordLiveData.postValue(NetworkResult.Loading())
        val response = userApi.passwordReset(resetRequestModel)
        handleResetPasswordResponse(response)
    }

    private fun handleResetPasswordResponse(response: Response<ResetResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _resetPasswordLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorText = response.errorBody()!!.charStream().readText()
            val errorMessage = try {
                JSONObject(errorText).getString("message")
            } catch (e: JSONException) {
                errorText
            }
            _resetPasswordLiveData.postValue(NetworkResult.Error(errorMessage))
        } else {
            _resetPasswordLiveData.postValue(NetworkResult.Error("Something is wrong"))
        }
    }

    //actual reset password
    suspend fun passwordReset(actualResetRequestModel: ActualResetRequestModel) {
        _passwordResetLiveData.postValue(NetworkResult.Loading())
        val response = userApi.resetPassword(actualResetRequestModel)
        handlePasswordResetResponse(response)
    }

    private fun handlePasswordResetResponse(response: Response<ResetResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _passwordResetLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorText = response.errorBody()!!.charStream().readText()
            val errorMsg = JSONObject(errorText).getString("message")
            _passwordResetLiveData.postValue(NetworkResult.Error(errorMsg))
        } else {
            _passwordResetLiveData.postValue(NetworkResult.Error("Something wrong!"))
        }
    }

}