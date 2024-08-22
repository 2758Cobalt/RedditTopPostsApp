package com.kolomoiets.reddittoppostsapp.data

/* stores information about the post */

data class RedditPostData (
    val id: String,
    val authorName: String,
    val title: String,
    val createdTime: Double,
    val thumbnailUrl: String,
    val commentsCount: Int
)