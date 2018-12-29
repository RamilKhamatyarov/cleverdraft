package ru.rkhamatyarov.cleverdraft.utilities.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.presenter.DraftListPresenter
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.DateTimePickerFragment
import ru.rkhamatyarov.cleverdraft.view.DraftListActivity
import ru.rkhamatyarov.cleverdraft.view.MainActivity

val mainActivityModule = module {

    // single instance of HelloRepository
    single<MainMVP.ProvidedModelOps> { MainModel(androidContext()) }

    factory<MainMVP.ViewOps> { MainActivity() }
    factory<MainMVP.ProvidedPresenterOps> { MainPresenter( mainModel = get(), dateTimePickerPresenter = get()) }

    //TODO move dataTimePicker to particular module
    factory { DateTimePickerPresenter(dialogFragment = get()) }
    factory { DateTimePickerFragment() }

}

val draftListActivityModule = module {

    factory { DraftListActivity() }
    factory { DraftListPresenter(view = get()) }

}