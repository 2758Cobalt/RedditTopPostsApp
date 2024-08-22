package com.kolomoiets.reddittoppostsapp.retrofit.posts.data

import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.subdata.RedditApiTime

data class RedditPostParams(
    val limit: Int? = null,
    val time: RedditApiTime? = null,
    val count: Int? = null,
    val beforeNameId: String? = null,
    val afterNameId: String? = null
)

fun RedditPostParams.toMap(): Map<String, String> {
    return mapOf(
        "limit" to limit.toString(),
        "t" to time.toString(),
        "count" to count.toString(),
        "before" to beforeNameId.toString(),
        "after" to afterNameId.toString()
    )
}
