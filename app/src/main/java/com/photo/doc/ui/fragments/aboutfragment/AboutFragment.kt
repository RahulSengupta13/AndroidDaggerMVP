package com.photo.doc.ui.fragments.aboutfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.photo.doc.R
import com.photo.doc.dagger.component.DaggerWelcomeFragmentComponent
import com.photo.doc.dagger.module.WelcomeFragmentModule
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

class AboutFragment : androidx.fragment.app.Fragment(), AboutContract.View {

    @Inject lateinit var presenter: AboutContract.Presenter
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_about, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    private fun injectDependency() {
        val aboutComponent = DaggerWelcomeFragmentComponent.builder()
                .welcomeFragmentModule(WelcomeFragmentModule())
                .build()
        aboutComponent.inject(this)
    }

    private fun initView() {
        presenter.loadMessage()
    }

    override fun showProgress(show: Boolean) = if (show) {
        progressBar.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.GONE
    }

    override fun loadMessageSuccess(message: String) {
        aboutText.text = getString(R.string.about_text)
        aboutText.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = AboutFragment()

        const val TAG: String = "About Fragment"
    }
}
