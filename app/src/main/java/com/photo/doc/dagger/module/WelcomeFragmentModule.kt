package com.photo.doc.dagger.module

import com.photo.doc.ui.fragments.aboutfragment.AboutContract
import com.photo.doc.ui.fragments.aboutfragment.AboutPresenter
import com.photo.doc.ui.fragments.welcomefragment.WelcomeFragmentContract
import com.photo.doc.ui.fragments.welcomefragment.WelcomeFragmentPresenter
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