package com.kolomoiets.reddittoppostsapp.retrofit.posts.interfaces

import com.cobaltumapps.reddittoppostsappblank.retrofit.posts.data.PostResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface RedditPostApi {
    @GET("top.json")
    fun getTopPosts(
        @Header("Authorization") authorizationHeader: String,
        @QueryMap queryMap: Map<String, String>,
    ): Call<PostResponse>
}