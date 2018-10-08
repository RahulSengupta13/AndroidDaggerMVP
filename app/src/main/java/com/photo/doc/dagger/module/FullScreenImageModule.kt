package com.photo.doc.dagger.module

import android.app.Activity
import com.photo.doc.ui.activities.fullscreenimage.FullScreenImageContract
import com.photo.doc.ui.activities.fullscreenimage.FullScreenImagePresenter
import dagger.Module
import dagger.Provides

@Module
class FullScreenImageModule(private var activity: Activity){

    @Provides
    fun providesActivity() = activity

    @Provides
    fun providesPresenter(): FullScreenImageContract.Presenter {
        return FullScreenImagePresenter()
    }

}