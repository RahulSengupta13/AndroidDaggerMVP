package com.example.daggermvp.ui.activities.about

class AboutPresenter: AboutContract.Presenter {

    private lateinit var view: AboutContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }

    override fun attach(view: AboutContract.View) {
        this.view = view
    }

}