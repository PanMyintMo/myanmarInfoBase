package com.pan.mvvm.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class GetAllFavoriteResponse(
    @SerializedName("files")
    val files:List<FavoriteResponseData> ,
    @SerializedName("success")
    val success: Boolean
) : Parcelable