package ru.rkhamatyarov.cleverdraft.utililities.di

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Asus on 14.09.2017.
 */


@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun application(): Application
    fun getMainComponent(module: MainActivityModule): MainActivityModule

}