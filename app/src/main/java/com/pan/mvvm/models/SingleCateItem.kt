package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SingleCateItem(
    @SerializedName("cateId")
    val cateId: String,
    @SerializedName("cateName")
    val cateName: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("files")
    val files: List<SingleCateItems>,
    @SerializedName("_id")
    val id: String,
    @SerializedName("postAccept")
    val postAccept: Boolean,
    @SerializedName("timeCreated")
    val timeCreated: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("userprofile")
    val userprofile: String,
    @SerializedName("__v")
    val v: Int,
    @SerializedName("viewcount")
    var viewcount: Double
) : Parcelable