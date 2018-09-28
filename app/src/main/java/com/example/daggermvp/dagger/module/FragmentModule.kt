package com.example.daggermvp.dagger.module

import com.example.daggermvp.api.ApiServiceInterface
import com.example.daggermvp.ui.aboutfragment.AboutContract
import com.example.daggermvp.ui.aboutfragment.AboutPresenter
import com.example.daggermvp.ui.listfragment.ListContract
import com.example.daggermvp.ui.listfragment.ListPresenter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(){

    @Provides
    fun provideAboutPresenter(): AboutContract.Presenter {
        return AboutPresenter()
    }

    @Provides
    fun provideListPresenter(): ListContract.Presenter {
        return ListPresenter()
    }

    @Provides
    fun provideApiService(): ApiServiceInterface {
        return ApiServiceInterface.create()
    }
}