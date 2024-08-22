package com.kolomoiets.reddittoppostsapp.retrofit.posts

import android.util.Log
import com.cobaltumapps.reddittoppostsappblank.retrofit.posts.data.PostResponse
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthResponseData
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.RedditPostParams
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.toMap
import com.kolomoiets.reddittoppostsapp.retrofit.posts.interfaces.RedditPostApi
import com.kolomoiets.reddittoppostsapp.retrofit.posts.interfaces.RedditPostListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RedditPostManager: RedditPostListener {
    private var baseUrl : String = "https://oauth.reddit.com/"

    private var redditPostListener: RedditPostListener? = null
    private var authResponseData: AuthResponseData? = null

    private var redditPostsApi : RedditPostApi

    init {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val clientHttp = OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .build()

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientHttp)
            .build()

        redditPostsApi = retrofit.create(RedditPostApi::class.java)
    }

    fun setNewListener(listener: RedditPostListener) {
        this.redditPostListener = listener
    }

    fun setAuthData(newAuthResponseData: AuthResponseData) {
        this.authResponseData = newAuthResponseData
    }

    override suspend fun getTopPosts(redditPostParams: RedditPostParams): PostResponse? {
        val call = if (authResponseData != null) {
            authResponseData?.accessToken?.let {
                redditPostsApi.getTopPosts(
                    authorizationHeader = it,
                    queryMap = redditPostParams.toMap()
                )
            }
        }
        else {
            Log.e(LOG_TAG, "Auth response is null")
            null
        }

        return call?.execute()?.body()
    }


    companion object {
        const val LOG_TAG = "RedditPostManagerDebugTag"
    }
}

