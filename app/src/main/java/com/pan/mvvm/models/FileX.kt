package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class FileX(
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("fileSize")
    val fileSize: String,
    @SerializedName("fileType")
    val fileType: String
) : Parcelable