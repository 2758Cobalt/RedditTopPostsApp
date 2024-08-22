package com.kolomoiets.reddittoppostsapp

import com.cobaltumapps.reddittoppostsappblank.retrofit.posts.data.PostResponse
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.RedditAuthorizationManager
import com.kolomoiets.reddittoppostsapp.retrofit.posts.RedditPostManager
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.RedditPostParams
import com.kolomoiets.reddittoppostsapp.retrofit.posts.interfaces.RedditPostListener

class RedditManager: RedditPostListener {
    private val redditAuthorizationManager = RedditAuthorizationManager()
    private val redditPostManager = RedditPostManager()

    init {
        redditPostManager.setNewListener(this)
    }

    override suspend fun getTopPosts(redditPostParams: RedditPostParams): PostResponse? {
        val token = redditAuthorizationManager.getAuthResponse() ?: redditAuthorizationManager.authorization()
        token?.let { redditPostManager.setAuthData(it) }

        return redditPostManager.getTopPosts(redditPostParams)
    }
}