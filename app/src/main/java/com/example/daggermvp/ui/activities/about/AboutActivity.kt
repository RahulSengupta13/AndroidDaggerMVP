package com.example.daggermvp.ui.activities.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.daggermvp.R
import com.example.daggermvp.dagger.component.DaggerAboutComponent
import com.example.daggermvp.dagger.module.AboutModule
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_about.*
import javax.inject.Inject

class AboutActivity : AppCompatActivity(), AboutContract.View, AppBarLayout.OnOffsetChangedListener {

    private var mMaxScrollSize: Int = 0
    private var mIsAvatarShown = true

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.totalScrollRange

        val percentage = Math.abs(i) * 100 / mMaxScrollSize

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false
            aboutImage.animate()
                    .scaleY(0.45f).scaleX(0.45f)
                    .setDuration(200)
                    .start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true
            aboutImage.animate()
                    .scaleY(1f).scaleX(1f)
                    .start()
        }
    }

    @Inject
    lateinit var presenter: AboutContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.aboutToolbar)
        toolbar.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        aboutAppBarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = aboutAppBarLayout.totalScrollRange


        injectDependency()
    }

    private fun injectDependency() {
        val component = DaggerAboutComponent.builder()
                .aboutModule(AboutModule(this))
                .build()
        component.inject(this)
    }

    companion object {
        private const val PERCENTAGE_TO_ANIMATE_AVATAR = 20
        fun getIntent(context: Context) = Intent(context, AboutActivity::class.java)
    }
}
