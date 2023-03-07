/*
package com.pan.mvvm.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pan.mvvm.models.FileX
import com.pan.mvvm.models.PopularItem

@ProvidedTypeConverter
class PopularTypeConverter {
    private var gson = Gson()


    @TypeConverter
    fun popularItemPostToString(popularItem: PopularItem): String {
        return gson.toJson(popularItem)
    }

    @TypeConverter
    fun stringToPopularItemPost(data: String): PopularItem {
        val listType = object : TypeToken<PopularItem>() {}.type
        return gson.fromJson(data, listType)
    }



    @TypeConverter
    fun fromFileXToString(fileX: List<*>): String {
        val type=object : TypeToken<List<*>>() {}.type
        return gson.toJson(fileX,type)
    }

    @TypeConverter
    fun toFiles(data: String?):List<FileX>  {
        if(data == null || data.isEmpty()){
            return mutableListOf()
        }

        val listType = object : TypeToken<List<FileX>>() {}.type
        return gson.fromJson(data, listType)
    }
}*/
