package com.example.daggermvp.ui.activities.welcome

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import com.example.daggermvp.R
import com.example.daggermvp.dagger.component.DaggerWelcomeActivityComponent
import com.example.daggermvp.dagger.module.WelcomeActivityModule
import com.example.daggermvp.ui.fragments.aboutfragment.AboutFragment
import com.example.daggermvp.ui.activities.autovision.AutoVisionActivity
import com.example.daggermvp.ui.fragments.welcomefragment.WelcomeFragment
import javax.inject.Inject

class WelcomeActivity : AppCompatActivity(), WelcomeContract.View {

    @Inject
    lateinit var presenter: WelcomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        injectDependency()
        presenter.attach(this)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
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
        val activityComponent = DaggerWelcomeActivityComponent.builder()
                .welcomeActivityModule(WelcomeActivityModule(this))
                .build()
        activityComponent.inject(this)

    }

    override fun showWelcomeSlider() {
        supportFragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(AnimType.SLIDE.getAnimPair().first, AnimType.SLIDE.getAnimPair().second)
                .replace(R.id.frame, WelcomeFragment.newInstance(), WelcomeFragment.TAG)
                .commit()
    }

    override fun launchAutoVisionActivity() {
        startActivity(AutoVisionActivity.getIntent(this))
    }

    enum class AnimType {
        SLIDE,
        FADE;

        fun getAnimPair(): Pair<Int, Int> {
            return when (this) {
                SLIDE -> Pair(R.anim.slide_left, R.anim.slide_right)
                FADE -> Pair(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }
}
