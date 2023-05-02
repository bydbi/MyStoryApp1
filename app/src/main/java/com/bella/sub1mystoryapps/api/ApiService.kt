package com.bella.sub1mystoryapps.api

import androidx.room.Entity
import com.bella.sub1mystoryapps.apirespon.AddStoryRespon
import com.bella.sub1mystoryapps.apirespon.LoginRespon
import com.bella.sub1mystoryapps.apirespon.RegistRespon
import com.bella.sub1mystoryapps.apirespon.StoryRespon
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

@Entity(tableName = "quote")
interface ApiService {
    @FormUrlEncoded
    @POST("/v1/login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password : String
    ): Call<LoginRespon>

    @FormUrlEncoded
    @POST("/v1/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegistRespon>

    @Multipart
    @POST("/v1/stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryRespon>

    @GET("/v1/stories?location=1")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<StoryRespon>

}