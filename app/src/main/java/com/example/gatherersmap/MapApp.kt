package com.example.gatherersmap

import android.app.Application

class MapApp: Application() {
    companion object {
        lateinit var instance: MapApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}