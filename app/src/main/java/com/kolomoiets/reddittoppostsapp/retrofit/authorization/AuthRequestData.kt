package com.kolomoiets.reddittoppostsapp.retrofit.authorization

data class AuthRequestData(
    val grantType: String,
    val username: String,
    val password: String
)
