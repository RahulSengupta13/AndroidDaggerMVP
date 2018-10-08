package com.photo.doc.dagger.module

import android.app.Application
import com.photo.doc.BaseApp
import com.photo.doc.dagger.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private var baseApp: BaseApp) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp;
    }
}