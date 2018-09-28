package com.example.daggermvp.ui.listfragment

import com.example.daggermvp.models.DetailsViewModel
import com.example.daggermvp.models.Post
import com.example.daggermvp.ui.base.BaseContract

class ListContract {
    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun showErrorMessage(error: String)
        fun loadDataSuccess(list: List<Post>)
        fun loadDataAllSuccess(model: DetailsViewModel)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun loadData()
        fun loadDataAll()
        fun deleteItem(item: Post)
    }
}