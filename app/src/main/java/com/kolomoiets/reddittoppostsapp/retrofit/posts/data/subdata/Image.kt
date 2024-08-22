package com.kolomoiets.reddittoppostsapp.retrofit.posts.data.subdata

data class Image(
    val id: String,
    val resolutions: List<Resolution>,
    val source: Source,
    val variants: Variants
)