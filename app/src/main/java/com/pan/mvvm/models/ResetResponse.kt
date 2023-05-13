package com.pan.mvvm.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class ResetResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
) : Parcelable