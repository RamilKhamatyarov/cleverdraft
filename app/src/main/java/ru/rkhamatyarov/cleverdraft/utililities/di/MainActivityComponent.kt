package ru.rkhamatyarov.cleverdraft.utililities.di

import dagger.Subcomponent
import ru.rkhamatyarov.cleverdraft.MainActivity

/**
 * Created by Asus on 14.09.2017.
 */


@ActivityScope
@Subcomponent( modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}




