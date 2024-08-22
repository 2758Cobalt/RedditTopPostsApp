package com.kolomoiets.reddittoppostsapp.fileStream

import com.kolomoiets.reddittoppostsapp.retrofit.authorization.data.AuthRequestData
import java.io.FileInputStream
import java.util.Properties

class AuthConfig {
    private var authConfig: AuthRequestData

    init {
        val configStream = FileInputStream("auth.config")
        val properties = Properties()

        properties.load(configStream)

        authConfig = AuthRequestData(
            properties.getProperty("grant_type"),
            properties.getProperty("username"),
            properties.getProperty("password")
        )
    }

    fun getAuthConfig(): AuthRequestData {
        return this.authConfig
    }
}