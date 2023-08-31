package com.example.gatherersmap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapApp : Application() {
    companion object {
        lateinit var instance: MapApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}