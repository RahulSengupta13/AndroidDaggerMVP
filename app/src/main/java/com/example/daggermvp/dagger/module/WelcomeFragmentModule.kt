package com.example.daggermvp.dagger.module

import com.example.daggermvp.ui.aboutfragment.AboutContract
import com.example.daggermvp.ui.aboutfragment.AboutPresenter
import com.example.daggermvp.ui.welcomefragment.WelcomeFragmentContract
import com.example.daggermvp.ui.welcomefragment.WelcomeFragmentPresenter
import com.example.daggermvp.utils.SharedPreferencesManager
import dagger.Module
import dagger.Provides

@Module
class WelcomeFragmentModule {

    @Provides
    fun provideAboutPresenter(): AboutContract.Presenter {
        return AboutPresenter()
    }

    @Provides
    fun provideWelcomeFragmentPresenter(): WelcomeFragmentContract.Presenter {
        return WelcomeFragmentPresenter()
    }
}