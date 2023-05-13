package com.pan.mvvm.api

import com.pan.mvvm.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @Headers("Content-Type:application/json")
    @POST("users")
    suspend fun signup(@Body registerRequestModel: RegisterRequestModel): Response<RegisterResponse>

    @Headers("Content-Type:application/json")
    @POST("users/login")
    suspend fun signIn(
        @Body loginRequestModel: LoginRequestModel
    ): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("users/requestPasswordReset")
    suspend fun passwordReset(@Body resetRequestModel: ResetRequestModel): Response<ResetResponse>

    @Headers("Content-Type:application/json")
    @POST("users/resetPassword")
    suspend fun resetPassword(
        @Body actualResetRequestModel: ActualResetRequestModel
    ): Response<ResetResponse>


}