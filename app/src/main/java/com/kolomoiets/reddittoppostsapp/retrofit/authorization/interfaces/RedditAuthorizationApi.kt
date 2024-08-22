package com.kolomoiets.reddittoppostsapp.retrofit.authorization.interfaces

import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthResponseData
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditAuthorizationApi {
    @FormUrlEncoded
    @POST("api/v1/access_token")
    fun authorization(
        @Header("Authorization") basicAuthHeader: String,
        @FieldMap grantType: Map<String, String>
    ): Call<AuthResponseData>
}