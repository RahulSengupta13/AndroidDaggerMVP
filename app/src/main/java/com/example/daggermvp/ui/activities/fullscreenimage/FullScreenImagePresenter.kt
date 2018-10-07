package com.example.daggermvp.ui.activities.fullscreenimage

class FullScreenImagePresenter: FullScreenImageContract.Presenter {

    private lateinit var view: FullScreenImageContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }

    override fun attach(view: FullScreenImageContract.View) {
        this.view = view
    }

}