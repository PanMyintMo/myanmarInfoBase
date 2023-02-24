package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoginResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("isAdmin")
    val isAdmin: Boolean,
    @SerializedName("login")
    val login: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("verified")
    val verified: Boolean
) : Parcelable