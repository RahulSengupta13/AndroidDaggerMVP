package com.photo.doc.ui.activities.result

import android.app.Activity
import com.photo.doc.utils.ResultManager

class ResultPresenter constructor(private val activity: Activity) : ResultContract.Presenter {

    private lateinit var view: ResultContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }

    override fun attach(view: ResultContract.View) {
        this.view = view
    }

    override fun dataSetChanged() {
        view.dataSetChanged()
    }

    override fun loadImagesForUrls() {
        ResultManager.webAnnotation.pagesWithMatchingImages.forEach {
            ImageThumbnailTask(activity as ResultActivity, it, it.url).execute()
        }
    }
}