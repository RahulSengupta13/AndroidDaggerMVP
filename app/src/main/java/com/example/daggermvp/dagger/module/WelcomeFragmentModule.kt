package com.example.daggermvp.dagger.module

import com.example.daggermvp.ui.fragments.aboutfragment.AboutContract
import com.example.daggermvp.ui.fragments.aboutfragment.AboutPresenter
import com.example.daggermvp.ui.fragments.welcomefragment.WelcomeFragmentContract
import com.example.daggermvp.ui.fragments.welcomefragment.WelcomeFragmentPresenter
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