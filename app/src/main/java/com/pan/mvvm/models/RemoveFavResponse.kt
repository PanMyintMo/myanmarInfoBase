package com.pan.mvvm.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class RemoveFavResponse(
    @SerializedName("result")
    val result: ResultX,
    @SerializedName("success")
    val success: Boolean
) : Parcelable