package com.vecentek.decorelinkdemo

import android.app.Application
import android.content.Context
import com.vecentek.decorelink.DLConfigure
import com.vecentek.decorelink.DLEngine

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getInstance(): MyApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 初始化您的应用程序的其他内容
    }
}
