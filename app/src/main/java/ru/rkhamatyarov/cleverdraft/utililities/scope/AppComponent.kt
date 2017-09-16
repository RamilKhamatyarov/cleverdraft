package ru.rkhamatyarov.cleverdraft.utililities.scope

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Asus on 14.09.2017.
 */

@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun provideApplication() = application
}

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun application(): Application
    fun getMainComponent(module: MainActivityModule): MainActivityModule
}