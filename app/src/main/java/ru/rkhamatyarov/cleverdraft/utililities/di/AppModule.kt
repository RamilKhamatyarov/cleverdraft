package ru.rkhamatyarov.cleverdraft.utililities.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Asus on 19.09.2017.
 */
@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun providesApplication(): Application = application
}