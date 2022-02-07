package gr.atcom.gpslocationservice.application

import android.app.Application
import gr.atcom.gpslocationservice.retrofit.NetworkProvider
import gr.atcom.gpslocationservice.retrofit.NetworkProviderImpl

class GpsLocationApplication: Application() {


    companion object{
        private lateinit var instance: GpsLocationApplication

        @JvmStatic
        fun get(): GpsLocationApplication {
            return instance
        }
    }

    val networkProvider: NetworkProvider by lazy {
        return@lazy NetworkProviderImpl()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}