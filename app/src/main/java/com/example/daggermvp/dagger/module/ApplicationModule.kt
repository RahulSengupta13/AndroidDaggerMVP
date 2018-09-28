package com.example.daggermvp.dagger.module

import android.app.Application
import com.example.daggermvp.BaseApp
import com.example.daggermvp.dagger.scope.PerApplication
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