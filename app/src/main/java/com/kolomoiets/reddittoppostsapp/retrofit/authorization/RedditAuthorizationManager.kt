package com.kolomoiets.reddittoppostsapp.retrofit.authorization

import android.util.Base64
import android.util.Log
import com.kolomoiets.reddittoppostsapp.fileStream.AuthConfig
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthRequestData
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthResponseData
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.interfaces.RedditAuthorizationApi
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.interfaces.RedditAuthorizationListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileInputStream
import java.util.Properties

class RedditAuthorizationManager: RedditAuthorizationListener {
    protected var baseUrl : String = "https://www.reddit.com/"

    private var authConfig: AuthRequestData = AuthConfig().getAuthConfig()
    private var authResponseData: AuthResponseData? = null
    private var redditAuthorizationAPI : RedditAuthorizationApi

    init {

        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val clientHttp = OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientHttp)
            .build()

        redditAuthorizationAPI = retrofit.create(RedditAuthorizationApi::class.java)

    }

    override suspend fun authorization(): AuthResponseData? {
        val credentials = with(authConfig) {
            "$username:$password"
        }
        val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)


        val call = redditAuthorizationAPI.authorization(
            authHeader,
            hashMapOf(Pair("grant_type", authConfig.grantType)))

        authResponseData = call.execute().body()

        authResponseData?.apply {
            Log.i(
                LOG_TAG,
                "Auth body\n" +
                        "access_token: ${accessToken}\n" +
                        "token_type: $tokenType\n" +
                        "expires_in: $expiresIn\n" +
                        "scope: $scope")
        }

        return authResponseData
    }

    fun getAuthResponse(): AuthResponseData? {
        return this.authResponseData
    }

    companion object {
        const val LOG_TAG = "RedditAuthorizationDebugTag"
    }
}

