package com.pan.mvvm.api

import com.pan.mvvm.models.CategoryItem
import com.pan.mvvm.models.LatestPostItem
import com.pan.mvvm.models.PopularItem
import retrofit2.Response
import retrofit2.http.*

interface MyanFoBaseApi {
    @GET("category")
    suspend fun getAllCategory(): Response<List<CategoryItem>>


    @GET("popular")
    suspend fun getPopular(): Response<List<PopularItem>>

    @GET("lastposts")
    suspend fun getLatestPost(): Response<List<LatestPostItem>>


    /* @GET("post")
     fun getPost(): Call<List<AllPostModelItem>>

     @GET("latest")
     fun getCurrency(): Call<Currency>

  @GET("users/detail/{id}")
     fun getUserDetail(@Path("id") id: String): Call<UserDetailResponseClass>

     @GET("postcate/{cate}")
     fun getSingleCate(@Path("cate") cate: String): Response<SingleCateItem>


     @Headers("Content-Type:application/json")
     @POST("addFav")
     fun addToFavoritePost(
         @Header("Authorization") token: String,
         @Body addFavPost: ResponseFavPost
     ): Call<ResponseFavPost>


     @Headers("Content-Type:application/json")
     @POST("checked")
     fun checkFavoritePost(
         @Header("Authorization") token: String,
         @Body checkFavorite: CheckFavorite
     ): Call<ResponseClass>


     @Headers("Content-Type:application/json")
     @POST("removeFav")
     fun removeFromFavoritePost(
         @Body removeFavPost: CheckFavorite
     ): Call<RemoveFavResponseClass>

     @Headers("Content-Type:application/json")
     @POST("getFavposts")
     fun getAllFavPosts(
         @Header("Authorization") token: String,
         @Body requestFavPost: RequestFavPosts
     ): Call<AllFavoriteDataClass>
 */
}