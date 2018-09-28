package com.example.daggermvp.ui.aboutfragment

import com.example.daggermvp.ui.base.BaseContract

class AboutContract {
    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun loadMessageSuccess(message: String)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun loadMessage()
    }
}