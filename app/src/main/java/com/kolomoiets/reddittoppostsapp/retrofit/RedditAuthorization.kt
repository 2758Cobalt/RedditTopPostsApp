package com.kolomoiets.reddittoppostsapp.retrofit

import com.kolomoiets.reddittoppostsapp.retrofit.authorization.AuthResponseData
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditAuthorization {
    @FormUrlEncoded
    @POST("api/v1/access_token")
    fun authorization(
        @Header("Authorization") basicAuthHeader: String,
        @FieldMap grantType: Map<String, String>
    ): Call<AuthResponseData>
}