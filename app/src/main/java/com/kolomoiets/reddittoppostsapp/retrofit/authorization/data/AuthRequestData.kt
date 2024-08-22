package com.kolomoiets.reddittoppostsapp.retrofit.authorization.data

data class AuthRequestData(
    val grantType: String,
    val username: String,
    val password: String
)
