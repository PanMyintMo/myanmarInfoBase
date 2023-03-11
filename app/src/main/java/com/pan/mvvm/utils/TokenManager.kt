package com.pan.mvvm.utils

import android.content.Context
import com.pan.mvvm.utils.Constants.PREF_TOKEN
import com.pan.mvvm.utils.Constants.USER_ID
import com.pan.mvvm.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context:Context) {
    private var preference=context.getSharedPreferences(PREF_TOKEN,Context.MODE_PRIVATE)


    fun saveToken(token:String){
        val editor=preference.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }

    fun getToken():String?{
        return preference.getString(USER_TOKEN,null)
    }

    fun saveId(id:String){
        val editor=preference.edit()
        editor.putString(USER_ID,id)
        editor.apply()
    }

    fun getId() : String?{
        return preference.getString(USER_ID,null)
    }

}