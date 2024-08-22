package com.kolomoiets.reddittoppostsapp.retrofit.posts.interfaces

import com.cobaltumapps.reddittoppostsappblank.retrofit.posts.data.PostResponse
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.RedditPostParams

interface RedditPostListener {
    suspend fun getTopPosts(redditPostParams: RedditPostParams): PostResponse?
}