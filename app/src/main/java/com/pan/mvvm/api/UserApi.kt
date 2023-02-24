package com.pan.mvvm.api

import com.pan.mvvm.models.LoginRequestModel
import com.pan.mvvm.models.LoginResponse
import com.pan.mvvm.models.RegisterRequestModel
import com.pan.mvvm.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApi {
    @Headers("Content-Type:application/json")
    @POST("users")
    suspend fun signup(@Body registerRequestModel: RegisterRequestModel):Response<RegisterResponse>

    @Headers("Content-Type:application/json")
    @POST("users/login")
    suspend fun signIn(
        @Body loginRequestModel: LoginRequestModel
    ): Response<LoginResponse>


}