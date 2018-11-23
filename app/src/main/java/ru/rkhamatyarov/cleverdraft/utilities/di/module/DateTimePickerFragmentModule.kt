package ru.rkhamatyarov.cleverdraft.utilities.di.module

import dagger.Module
import dagger.Provides
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.utililities.di.ActivityScope
import ru.rkhamatyarov.cleverdraft.view.DateTimePickerFragment

/**
 * Created by RKhamatyarov on 09.02.2018.
 */

@Module
class DateTimePickerFragmentModule(var dateTimePickerFragment: DateTimePickerFragment) {

    @Provides
    @ActivityScope
    fun providedDateTimePickerFragment(): DateTimePickerFragment = this.dateTimePickerFragment

    @Provides
    @ActivityScope
    fun providesDateTimePickerPresenter(): DateTimePickerPresenter {
        return DateTimePickerPresenter(dateTimePickerFragment)
    }
}