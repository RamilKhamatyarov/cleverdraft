package ru.rkhamatyarov.cleverdraft.utilities.di

import dagger.Module
import dagger.Provides
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.presenter.DraftListPresenter
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.utililities.di.ActivityScope
import ru.rkhamatyarov.cleverdraft.view.DraftListActivity

/**
 * Created by RKhamatyarov on 21.11.2017.
 */
@Module
    class DraftListActivityModule(var draftListActivity: DraftListActivity) {

    @Provides
    @ActivityScope
    fun provideDraftListActivity(): DraftListActivity = this.draftListActivity

    @Provides
    @ActivityScope
    fun providedPresenterOps(): MainMVP.ProvidedPresenterOps{
        val draftListPresenter: DraftListPresenter = DraftListPresenter(this.draftListActivity)
        val model: MainModel = MainModel(draftListPresenter)
        draftListPresenter.mainModel = model
        return draftListPresenter
    }
}