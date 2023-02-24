package com.pan.mvvm.viewModel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pan.mvvm.models.LoginRequestModel
import com.pan.mvvm.models.LoginResponse
import com.pan.mvvm.models.RegisterRequestModel
import com.pan.mvvm.models.RegisterResponse
import com.pan.mvvm.repository.UserRepository
import com.pan.mvvm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val loginResponseLiveData:LiveData<NetworkResult<LoginResponse>>
        get() =userRepository.loginResponseLiveData

    val registerResponseLiveData:LiveData<NetworkResult<RegisterResponse>>
    get()=userRepository.registerResponseLiveData

    fun registerUser(registerRequestModel: RegisterRequestModel){
        viewModelScope.launch {
            userRepository.registerUser(registerRequestModel)
        }
    }

    fun loginUser(loginRequestModel: LoginRequestModel){
        viewModelScope.launch {
            userRepository.loginUser(loginRequestModel)
        }
    }

    fun validateCredentials(username: String, email: String,password :String):Pair<Boolean,String>{
        var result=Pair(true,"")

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result=Pair(false,"Please provide the credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result=Pair(false,"Please provide valid email")
        }
        else if (password.length<=5){
            result=Pair(false,"Password length should be greater than 5")
        }

        return result
    }


    fun validateLoginCredentials(email: String,password :String):Pair<Boolean,String>{
        var result=Pair(true,"")

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result=Pair(false,"Please provide the credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result=Pair(false,"Please provide valid email")
        }
        else if (password.length<=5){
            result=Pair(false,"Password length should be greater than 5")
        }

        return result
    }



}