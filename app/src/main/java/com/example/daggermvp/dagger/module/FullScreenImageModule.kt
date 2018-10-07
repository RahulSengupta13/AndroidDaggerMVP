package com.example.daggermvp.dagger.module

import android.app.Activity
import com.example.daggermvp.ui.activities.fullscreenimage.FullScreenImageContract
import com.example.daggermvp.ui.activities.fullscreenimage.FullScreenImagePresenter
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