package com.example.daggermvp.ui.main

import io.reactivex.disposables.CompositeDisposable

class MainPresenter: MainContract.Presenter {

    private val disposables = CompositeDisposable()
    private lateinit var view: MainContract.View

    override fun onDrawerOptionAboutClick() {
        view.showAboutFragment()
    }

    override fun onAutoVisionClick() {
        view.launchAutoVisionActivity()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun attach(view: MainContract.View) {
        this.view = view
        view.showListFragment()
    }
}
