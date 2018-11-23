package ru.rkhamatyarov.cleverdraft.utililities.di

import dagger.Module
import dagger.Provides
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.DateTimePickerFragment
import ru.rkhamatyarov.cleverdraft.view.MainActivity

/**
 * Created by RKhamatyarov on 19.09.2017.
 */


@Module
class MainActivityModule (var mainActivity: MainActivity) {

    @Provides
    @ActivityScope
    fun providesMainActivity(): MainActivity = this.mainActivity

    @Provides
    @ActivityScope
    fun providedPresenterOps(dateTimePickerPresenter: DateTimePickerPresenter): MainMVP.ProvidedPresenterOps {
        val mainPresenter: MainPresenter = MainPresenter(this.mainActivity, dateTimePickerPresenter)
        val model: MainModel = MainModel(mainPresenter)
        mainPresenter.mainModel = model
        return mainPresenter
    }

    @Provides
    @ActivityScope
    fun providesDateTimePickerPresenter(): DateTimePickerPresenter{
        return DateTimePickerPresenter(DateTimePickerFragment())
    }


}