/*
package com.pan.mvvm.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.pan.mvvm.models.PopularItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularPostDao {
*/
/*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularItem(popularPostEntity: PopularPostEntity)*//*


    */
/*@Query("SELECT * FROM popular_table ORDER BY id ASC ")
    fun readPopularItem(): LiveData<List<PopularItem>>*//*




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularItems(items: List<PopularItemEntity>)

    @Query("SELECT * FROM popular_items")
    fun getPopularItems(): List<PopularItemEntity>

}*/
