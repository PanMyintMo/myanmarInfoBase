package com.pan.mvvm.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class FavoriteResponse(
    @SerializedName("result")
    val result: Result,
    @SerializedName("success")
    val success: Boolean
) : Parcelable