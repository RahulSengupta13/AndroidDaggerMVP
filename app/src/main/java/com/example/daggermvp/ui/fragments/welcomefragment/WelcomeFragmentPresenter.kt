package com.example.daggermvp.ui.fragments.welcomefragment

import io.reactivex.disposables.CompositeDisposable

class WelcomeFragmentPresenter : WelcomeFragmentContract.Presenter {

    private val disposables = CompositeDisposable()
    private lateinit var view: WelcomeFragmentContract.View

    override fun setCurrentPage(current: Int, layoutSize: Int) {
        view.setCurrentPage(current, layoutSize)
    }

    override fun updateButtons(last: Boolean) {
        view.updateButtons(last)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun attach(view: WelcomeFragmentContract.View) {
        this.view = view
    }
}
