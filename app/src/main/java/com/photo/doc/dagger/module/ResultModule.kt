package com.photo.doc.dagger.module

import android.app.Activity
import com.photo.doc.ui.activities.result.ResultContract
import com.photo.doc.ui.activities.result.ResultPresenter
import dagger.Module
import dagger.Provides

@Module
class ResultModule(private var activity: Activity) {

    @Provides
    fun providesActivity(): Activity {
        return activity
    }

    @Provides
    fun providesPresenter(): ResultContract.Presenter {
        return ResultPresenter(activity)
    }
}