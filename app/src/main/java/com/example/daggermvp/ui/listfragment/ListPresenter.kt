package com.example.daggermvp.ui.listfragment

import com.example.daggermvp.api.ApiServiceInterface
import com.example.daggermvp.models.Album
import com.example.daggermvp.models.DetailsViewModel
import com.example.daggermvp.models.Post
import com.example.daggermvp.models.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

class ListPresenter : ListContract.Presenter {

    private val disposables = CompositeDisposable()
    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private lateinit var view: ListContract.View

    override fun loadData() {
        val subscribe = api.getPostList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<Post>? ->
                    view.showProgress(false)
                    view.loadDataSuccess(list!!.take(10))
                }, { error ->
                    view.showProgress(false)
                    view.showErrorMessage(error.localizedMessage)
                })
        disposables.add(subscribe)
    }

    override fun loadDataAll() {
        val subscribe = Observable.zip(api.getPostList(), api.getUserList(), api.getAlbumList(),
                Function3<List<Post>, List<User>, List<Album>, DetailsViewModel> { posts, users, albums ->
                    createDetailsViewModel(posts, users, albums)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model: DetailsViewModel? ->
                    view.showProgress(false)
                    view.loadDataAllSuccess(model!!)
                }, { error ->
                    view.showProgress(false)
                    view.showErrorMessage(error.localizedMessage)
                })

        disposables.add(subscribe)
    }

    private fun createDetailsViewModel(posts: List<Post>, users: List<User>, albums: List<Album>): DetailsViewModel {
        val postList = posts.take(30)
        val userList = users.take(30)
        val albumList = albums.take(30)
        return DetailsViewModel(postList, userList, albumList)
    }

    override fun deleteItem(item: Post) {

    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun attach(view: ListContract.View) {
        this.view = view
    }

}