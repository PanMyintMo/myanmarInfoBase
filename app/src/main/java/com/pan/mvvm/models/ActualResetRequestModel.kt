package com.pan.mvvm.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActualResetRequestModel(
    val userId:String,val resetString:String,val newPassword:String
):Parcelable
