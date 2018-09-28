package com.example.daggermvp.ui.autovision

class AutoVisionPresenter: AutoVisionContract.Presenter{

    private lateinit var view: AutoVisionContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }

    override fun attach(view: AutoVisionContract.View) {
        this.view = view
    }
}