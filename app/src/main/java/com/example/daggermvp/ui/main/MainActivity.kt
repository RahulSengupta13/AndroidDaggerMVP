package com.example.daggermvp.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.daggermvp.R
import com.example.daggermvp.dagger.component.DaggerActivityComponent
import com.example.daggermvp.dagger.module.ActivityModule
import com.example.daggermvp.ui.aboutfragment.AboutFragment
import com.example.daggermvp.ui.listfragment.ListFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectDependency()
        presenter.attach(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId) {
            R.id.nav_item_info -> {
                presenter.onDrawerOptionAboutClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(AboutFragment.TAG)

        if (fragment == null) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()
        activityComponent.inject(this)
    }

    override fun showAboutFragment() {
        if (supportFragmentManager.findFragmentByTag(AboutFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(AnimType.FADE.getAnimPair().first, AnimType.FADE.getAnimPair().second)
                    .replace(R.id.frame, AboutFragment.newInstance(), AboutFragment.TAG)
                    .commit()
        }
    }

    override fun showListFragment() {
        supportFragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(AnimType.SLIDE.getAnimPair().first, AnimType.SLIDE.getAnimPair().second)
                .replace(R.id.frame, ListFragment.newInstance(), ListFragment.TAG)
                .commit()
    }

    enum class AnimType {
        SLIDE,
        FADE;
        fun getAnimPair(): Pair<Int, Int> {
            return when(this) {
                SLIDE -> Pair(R.anim.slide_left, R.anim.slide_right)
                FADE -> Pair(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }
}
