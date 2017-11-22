package ru.rkhamatyarov.cleverdraft.utililities.di

import android.app.Application
import dagger.Component
import ru.rkhamatyarov.cleverdraft.utilities.di.DraftListActivityComponent
import ru.rkhamatyarov.cleverdraft.utilities.di.DraftListActivityModule
import javax.inject.Singleton

/**
 * Created by Asus on 14.09.2017.
 */


@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun application(): Application
    fun getMainComponent(mainActivityModule: MainActivityModule): MainActivityComponent
    fun getDraftListComponent(draftListActivityModule: DraftListActivityModule): DraftListActivityComponent

}