package com.pan.mvvm.repository


import com.pan.mvvm.api.MyanFoBaseApi
import com.pan.mvvm.models.LatestPostItem
import com.pan.mvvm.models.PopularItem
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val myanFoBaseApi: MyanFoBaseApi) {

    suspend fun getPopular(): Response<List<PopularItem>> {
        return myanFoBaseApi.getPopular()

    }

    suspend fun getLatestPost(): Response<List<LatestPostItem>> {
        return myanFoBaseApi.getLatestPost()
    }


}