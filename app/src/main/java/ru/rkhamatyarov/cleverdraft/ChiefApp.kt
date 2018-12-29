package ru.rkhamatyarov.cleverdraft

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.rkhamatyarov.cleverdraft.utilities.di.draftListActivityModule
import ru.rkhamatyarov.cleverdraft.utilities.di.mainActivityModule


/**
 * Created by RKhamatyarov on 16.09.2017.
 */

class ChiefApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin DSL
        startKoin(this, listOf(mainActivityModule, draftListActivityModule))
    }
}