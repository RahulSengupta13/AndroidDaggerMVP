package com.example.daggermvp.ui.welcome

import io.reactivex.disposables.CompositeDisposable

class WelcomePresenter: WelcomeContract.Presenter {

    private val disposables = CompositeDisposable()
    private lateinit var view: WelcomeContract.View

    override fun onAutoVisionClick() {
        view.launchAutoVisionActivity()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun attach(view: WelcomeContract.View) {
        this.view = view
        view.showWelcomeSlider()
    }
}
