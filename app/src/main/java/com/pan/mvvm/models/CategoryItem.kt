package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CategoryItem(
    @SerializedName("catename")
    val catename: String,
    @SerializedName("_id")
    val id: Int,
    @SerializedName("__v")
    val v: Int,
) : Parcelable