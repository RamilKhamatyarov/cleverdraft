package ru.rkhamatyarov.cleverdraft.utilities.di.component

import dagger.Subcomponent
import ru.rkhamatyarov.cleverdraft.utililities.di.ActivityScope
import ru.rkhamatyarov.cleverdraft.utilities.di.module.DateTimePickerFragmentModule
import ru.rkhamatyarov.cleverdraft.view.DateTimePickerFragment

/**
 * Created by RKhamatyarov on 12.02.2018.
 */

@ActivityScope
@Subcomponent(modules = arrayOf(DateTimePickerFragmentModule::class))
interface DateTimePickerFragmentComponent {
    fun inject(dateTimePickerFragment: DateTimePickerFragment)
}