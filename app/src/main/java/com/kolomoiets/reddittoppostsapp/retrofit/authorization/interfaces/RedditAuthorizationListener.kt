package com.kolomoiets.reddittoppostsapp.retrofit.authorization.interfaces

import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthRequestData
import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthResponseData

interface RedditAuthorizationListener {
    suspend fun authorization(): AuthResponseData?
}