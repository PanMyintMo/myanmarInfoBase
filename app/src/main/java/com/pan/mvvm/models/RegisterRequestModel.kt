package com.pan.mvvm.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequestModel (
    @SerializedName("username")
    var username:String,
    @SerializedName("email")
    var email:String,
    @SerializedName("password")
    val password:String) : Parcelable