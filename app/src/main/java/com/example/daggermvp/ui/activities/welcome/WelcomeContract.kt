package com.example.daggermvp.ui.activities.welcome

import com.example.daggermvp.ui.BaseContract

class WelcomeContract {
    interface View: BaseContract.View {
        fun showWelcomeSlider()
        fun launchAutoVisionActivity()
    }

    interface Presenter: BaseContract.Presenter<WelcomeContract.View> {
        fun onAutoVisionClick()
    }
}