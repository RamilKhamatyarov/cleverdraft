package ru.rkhamatyarov.cleverdraft.utililities.di

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.MainActivity
import javax.inject.Scope

/**
 * Created by Asus on 14.09.2017.
 */


@ActivityScope
@Subcomponent( modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}




