package com.example.gitsurfer

import android.app.Application
import com.example.gitsurfer.utils.FirebaseRemoteConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        mContext = this
        FirebaseRemoteConfig.init()
    }

    companion object {
        private lateinit var mContext: Application

        fun getContext() = mContext
    }
}