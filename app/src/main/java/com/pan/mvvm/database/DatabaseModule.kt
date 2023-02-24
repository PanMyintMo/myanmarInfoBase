package com.pan.mvvm.database

import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import com.pan.mvvm.database.converter.PopularTypeConverter
import com.pan.mvvm.utils.Constants.POPULAR_TABLE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
@TypeConverters(PopularTypeConverter::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providePopularDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, PopularPostDatabase::class.java, "popular_table")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDao(database: PopularPostDatabase) = database.getPopularPostDao()
}

