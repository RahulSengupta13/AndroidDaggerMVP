package com.photo.doc.ui.fragments.aboutfragment

import com.photo.doc.ui.BaseContract

class AboutContract {
    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun loadMessageSuccess(message: String)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun loadMessage()
    }
}