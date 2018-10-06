package com.example.daggermvp.dagger.module

import android.app.Activity
import com.example.daggermvp.ui.activities.about.AboutContract
import com.example.daggermvp.ui.activities.about.AboutPresenter
import dagger.Module
import dagger.Provides

@Module
class AboutModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity{
        return activity
    }

    @Provides
    fun providePresenter(): AboutContract.Presenter {
        return AboutPresenter()
    }

}