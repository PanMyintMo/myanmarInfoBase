package com.pan.mvvm.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class RegisterResponse(val status:String,val message: String) : Parcelable