package ru.rkhamatyarov.cleverdraft.utilities.di.module

import dagger.Module
import dagger.Provides
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.utililities.di.ActivityScope
import ru.rkhamatyarov.cleverdraft.view.DateTimePickerFragment
import ru.rkhamatyarov.cleverdraft.view.MainActivity

/**
 * Created by RKhamatyarov on 09.02.2018.
 */

@Module
class DateTimePickerFragmentModule {
    lateinit var dateTimePickerFragment: DateTimePickerFragment
    lateinit var mainActivity: MainActivity

    constructor (dateTimePickerFragment: DateTimePickerFragment)  {
        this.dateTimePickerFragment = dateTimePickerFragment
    }

    @Provides
    @ActivityScope
    fun providedDateTimePickerFragment(): DateTimePickerFragment = this.dateTimePickerFragment

    @Provides
    @ActivityScope
    fun providedDateTimePickerPresenter(): DateTimePickerPresenter {
        val dateTimePickerPresenter: DateTimePickerPresenter = DateTimePickerPresenter(this.dateTimePickerFragment)

        return dateTimePickerPresenter
    }
}