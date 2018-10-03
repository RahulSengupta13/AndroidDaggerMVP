package com.example.daggermvp.ui.welcomefragment

import com.example.daggermvp.ui.base.BaseContract

class WelcomeFragmentContract {

    interface View : BaseContract.View {
        fun launchAutoVisionActivity()
        fun setCurrentPage(current: Int, layoutSize: Int)
        fun updateButtons(last: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun setCurrentPage(current: Int, layoutSize: Int)
        fun updateButtons(last: Boolean)
    }
}