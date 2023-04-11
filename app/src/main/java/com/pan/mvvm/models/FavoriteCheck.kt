package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class FavoriteCheck(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("user")
    val user: String
) : Parcelable