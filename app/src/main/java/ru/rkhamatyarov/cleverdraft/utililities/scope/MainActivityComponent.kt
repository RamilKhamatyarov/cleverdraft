package ru.rkhamatyarov.cleverdraft.utililities.scope

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
@Scope
annotation class ActivityScope

@Module
class MainActivityModule (val mainActivity: MainActivity) {

    @Provides
    @ActivityScope
    fun provideMainActivity(): MainActivity = this.mainActivity

    @Provides
    @ActivityScope
    fun providePresenterOps(): MainMVP.ProvidedPresenterOps {
        val mainPresenter: MainPresenter = MainPresenter(this.mainActivity)
        val mainModel: MainModel = MainModel(mainPresenter)
        mainPresenter.setModel(mainModel)
        return mainPresenter
    }
}

@ActivityScope
@Subcomponent( modules = arrayOf(MainActivityComponent::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}




