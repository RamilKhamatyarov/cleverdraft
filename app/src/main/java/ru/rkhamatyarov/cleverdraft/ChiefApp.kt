package ru.rkhamatyarov.cleverdraft

import android.app.Application
import android.content.Context
import android.util.Log
import ru.rkhamatyarov.cleverdraft.utililities.di.AppComponent
import ru.rkhamatyarov.cleverdraft.utililities.di.AppModule
import ru.rkhamatyarov.cleverdraft.utililities.di.DaggerAppComponent


/**
 * Created by Asus on 16.09.2017.
 */
//fun get(context: Context): ChiefApp {
//    return context.getApplicationContext() as ChiefApp
//}


class ChiefApp: Application() {

    companion object Factory {
        fun get(context: Context): ChiefApp {
            return context.getApplicationContext() as ChiefApp
        }
    }

    override fun onCreate() {
        super.onCreate()

        initAppComponent()
    }

    private lateinit var appComponent: AppComponent

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

}