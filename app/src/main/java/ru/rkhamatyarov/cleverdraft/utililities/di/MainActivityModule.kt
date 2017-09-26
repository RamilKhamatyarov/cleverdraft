package ru.rkhamatyarov.cleverdraft.utililities.di

import dagger.Module
import dagger.Provides
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.MainActivity
import javax.inject.Scope

/**
 * Created by Asus on 19.09.2017.
 */


@Module
class MainActivityModule (val mainActivity: MainActivity) {

    @Provides
    @ActivityScope
    fun providesMainActivity(): MainActivity = this.mainActivity

    @Provides
    @ActivityScope
    fun providedPresenterOps(): MainMVP.ProvidedPresenterOps {
        val mainPresenter: MainPresenter = MainPresenter(this.mainActivity)
        val mainModel: MainModel = MainModel(mainPresenter)
        mainPresenter.setModel(mainModel)
        return mainPresenter
    }

}