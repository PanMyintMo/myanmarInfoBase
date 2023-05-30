package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UserRequestFavorite(
    @SerializedName("user")
    val user: String
) : Parcelable
