package ru.rkhamatyarov.cleverdraft.utilities.di

import dagger.Subcomponent
import ru.rkhamatyarov.cleverdraft.utililities.di.ActivityScope
import ru.rkhamatyarov.cleverdraft.view.DraftListActivity

/**
 * Created by RKhamatyarov on 21.11.2017.
 */
@ActivityScope
@Subcomponent( modules = arrayOf(DraftListActivityModule::class))
interface DraftListActivityComponent {
    fun inject(draftListActivity: DraftListActivity)
}