package com.pan.mvvm.api

import com.pan.mvvm.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MyanFoBaseApi {
    @GET("category")
    suspend fun getAllCategory(): Response<List<CategoryItem>>


    @GET("popular")
    suspend fun getPopular(): Response<List<PopularItem>>

    @GET("lastposts")
    suspend fun getLatestPost(): Response<List<LatestPostItem>>


    @GET("postcate/{cate}")
    suspend fun getSingleCate(@Path("cate") cate: String): Response<List<SingleCateItem>>

    @GET("users/detail/{id}")
    suspend fun getUserLoginDetail(@Path("id") id: String): Response<UserLoginDetailResponse>



    @Multipart
    @PUT("users/update/{id}")
    suspend fun updateProfileDetail(
        @Path("id") id: String,
        @Part profilePicture: MultipartBody.Part,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address") address: RequestBody,
        @Part("bio") bio: RequestBody
    ): Response<ProfileResponse>



    /* @GET("post")
     fun getPost(): Call<List<AllPostModelItem>>

     @GET("latest")
     fun getCurrency(): Call<Currency>

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