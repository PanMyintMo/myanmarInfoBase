package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Keep
@kotlinx.parcelize.Parcelize
data class LoginRequestModel(
    @SerializedName("loginemail")
    @Expose
    val loginemail: String,
    @SerializedName("loginpassword")
    @Expose
    val loginpassword: String
) : Parcelable