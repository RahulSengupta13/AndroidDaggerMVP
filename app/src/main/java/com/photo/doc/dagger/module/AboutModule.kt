package com.photo.doc.dagger.module

import android.app.Activity
import com.photo.doc.ui.activities.about.AboutContract
import com.photo.doc.ui.activities.about.AboutPresenter
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