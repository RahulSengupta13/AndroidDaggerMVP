package com.photo.doc.ui.activities.result

import com.photo.doc.ui.BaseContract

class ResultContract {

    interface View: BaseContract.View {
        fun dataSetChanged()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun dataSetChanged()
        fun loadImagesForUrls()
    }
}