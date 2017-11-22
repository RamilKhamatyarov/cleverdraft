package ru.rkhamatyarov.cleverdraft.utilities.di

import dagger.Module
import dagger.Provides
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.utililities.di.ActivityScope
import ru.rkhamatyarov.cleverdraft.view.DraftListActivity

/**
 * Created by Asus on 21.11.2017.
 */
@Module
class DraftListActivityModule(var draftLIstActivity: DraftListActivity) {

    @Provides
    @ActivityScope
    fun provideDraftListActivity(): DraftListActivity = this.draftLIstActivity

    @Provides
    @ActivityScope
    fun providedPresenterOps(): MainMVP.ProvidedPresenterOps{
        val mainPresenter: MainPresenter = MainPresenter(this.draftLIstActivity)
        val model: MainModel = MainModel(mainPresenter)
        mainPresenter.mainModel = model
        return mainPresenter
    }
}