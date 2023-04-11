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
    @POST("post")
   // @Headers("Content-Type: multipart/form-data")
    suspend fun createNewPost(
        @Part("cateId") cateId: RequestBody,
        @Part("cateName") cateName: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<NewPostResponse>


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


    @Headers("Content-Type:application/json")
    @POST("addFav")
    suspend fun addToFavoritePost(
        @Body addFavPost: FavoriteRequestModel
    ): Response<FavoriteResponse>


    @Headers("Content-Type:application/json")
    @POST("checked")
    suspend fun checkFavoritePost(@Body favoriteCheck: FavoriteCheck):
            Response<FavoriteCheckResponse>


    /* @GET("post")
     fun getPost(): Call<List<AllPostModelItem>>

     @GET("latest")
     fun getCurrency(): Call<Currency>


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