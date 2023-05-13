package com.pan.mvvm.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class AllFavoritedItemResponseClass(
    @SerializedName("cateId")
    val cateId: String,
    @SerializedName("cateName")
    val cateName: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("files")
    val files: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("postId")
    val postId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("__v")
    val v: Int
) : Parcelable