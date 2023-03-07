package com.pan.mvvm.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class ProfilePicture(
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("fileSize")
    val fileSize: String,
    @SerializedName("fileType")
    val fileType: String
) : Parcelable