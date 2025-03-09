package com.example.picksy

import android.app.Application

class MyApplication : Application() {

    companion object {
        @JvmStatic
        private var instance: MyApplication? = null

        @JvmStatic
        fun getInstance(): MyApplication? {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener?) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }
}
