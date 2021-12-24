package com.example.gitsurfer.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


object FirebaseRemoteConfig {
    val header: String
    get() = Firebase.remoteConfig.getString("DATA")

    fun init() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 * 24
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }
}