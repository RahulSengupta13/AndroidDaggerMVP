package com.example.daggermvp.ui.activities.result

class ResultPresenter : ResultContract.Presenter {

    private lateinit var view: ResultContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }

    override fun attach(view: ResultContract.View) {
        this.view = view
    }
}