package com.pan.mvvm.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import androidx.annotation.Keep

@Keep
@Parcelize
data class FavoriteCheckResponse(
    @SerializedName("favorited")
    val favorited: Boolean,
    @SerializedName("success")
    val success: Boolean
) : Parcelable




