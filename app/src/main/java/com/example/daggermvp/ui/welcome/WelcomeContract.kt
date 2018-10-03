package com.example.daggermvp.ui.welcome

import com.example.daggermvp.ui.base.BaseContract

class WelcomeContract {
    interface View: BaseContract.View {
        fun showWelcomeSlider()
        fun launchAutoVisionActivity()
    }

    interface Presenter: BaseContract.Presenter<WelcomeContract.View> {
        fun onAutoVisionClick()
    }
}