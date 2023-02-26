package com.pan.mvvm.repository

import androidx.lifecycle.LiveData
import com.pan.mvvm.database.PopularPostEntity
import com.pan.mvvm.database.dao.PopularPostDao
import com.pan.mvvm.models.PopularItem
import javax.inject.Inject

class LocalDataSource @Inject() constructor(val popularPostDao: PopularPostDao) {

    suspend fun insertPopularItem(popularPostEntity: PopularPostEntity) {
        return popularPostDao.insertPopularItem(popularPostEntity)
    }

}