package com.example.daggermvp.ui.main

import com.example.daggermvp.ui.base.BaseContract

class MainContract {
    interface View: BaseContract.View {
        fun showAboutFragment()
        fun showListFragment()
        fun launchAutoVisionActivity()
    }

    interface Presenter: BaseContract.Presenter<MainContract.View> {
        fun onDrawerOptionAboutClick()
        fun onAutoVisionClick()
    }
}