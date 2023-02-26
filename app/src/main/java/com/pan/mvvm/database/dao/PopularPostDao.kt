package com.pan.mvvm.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pan.mvvm.database.PopularPostEntity
import com.pan.mvvm.models.PopularItem

@Dao
interface PopularPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularItem(popularPostEntity: PopularPostEntity)

    /*@Query("SELECT * FROM popular_table ORDER BY id ASC ")
    fun readPopularItem(): LiveData<List<PopularItem>>*/


}