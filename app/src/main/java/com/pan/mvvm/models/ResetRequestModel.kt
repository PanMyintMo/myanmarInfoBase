package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@kotlinx.parcelize.Parcelize
data class ResetRequestModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("redirectUrl")
    val redirectUrl: String
) : Parcelable