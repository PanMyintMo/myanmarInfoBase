package com.pan.mvvm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pan.mvvm.database.converter.PopularTypeConverter
import com.pan.mvvm.database.dao.PopularPostDao
import javax.inject.Inject

@Database(
    entities = [PopularPostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PopularTypeConverter::class)
abstract class PopularPostDatabase: RoomDatabase() {
    abstract fun getPopularPostDao(): PopularPostDao

/*    companion object {
          @Volatile
          private var INSTANCE: PopularPostDatabase? = null

        fun getDatabase(context: Context): PopularPostDatabase {

              if (INSTANCE == null) {
                  synchronized(this)
                  {
                      INSTANCE =
                          Room.databaseBuilder(context, PopularPostDatabase::class.java, "popularDB")
                              .build()

                  }
              }
              return INSTANCE!!
          }
      }*/
}