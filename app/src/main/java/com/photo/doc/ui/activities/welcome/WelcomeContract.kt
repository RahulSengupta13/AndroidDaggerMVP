package com.photo.doc.ui.activities.welcome

import com.photo.doc.ui.BaseContract

class WelcomeContract {
    interface View: BaseContract.View {
        fun showWelcomeSlider()
        fun launchAutoVisionActivity()
    }

    interface Presenter: BaseContract.Presenter<WelcomeContract.View> {
        fun onAutoVisionClick()
    }
}