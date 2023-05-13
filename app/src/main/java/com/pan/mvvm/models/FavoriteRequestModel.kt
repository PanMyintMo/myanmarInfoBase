package com.pan.mvvm.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteRequestModel(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("cateId")
    val cateId: String,
    @SerializedName("cateName")
    val cateName: String,
    @SerializedName("files")
    val files: String
) : Parcelable