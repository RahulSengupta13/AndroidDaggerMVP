package com.example.daggermvp.ui.fragments.welcomefragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.daggermvp.dagger.component.DaggerWelcomeFragmentComponent
import com.example.daggermvp.dagger.module.WelcomeFragmentModule
import com.example.daggermvp.utils.SharedPreferencesManager
import javax.inject.Inject
import android.text.Html
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_welcome.*
import com.example.daggermvp.ui.activities.autovision.AutoVisionActivity
import com.example.daggermvp.R

class WelcomeFragment : Fragment(), WelcomeFragmentContract.View {

    @Inject
    lateinit var presenter: WelcomeFragmentContract.Presenter

    private lateinit var preferencesManager: SharedPreferencesManager
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectDependency()
        preferencesManager = SharedPreferencesManager(context ?: return)

        if (!preferencesManager.isFirstTimeLaunch()) {
            launchAutoVisionActivity()
            activity?.finish()
        }
    }

    private fun injectDependency() {
        val welcomeFragmentComponent = DaggerWelcomeFragmentComponent.builder()
                .welcomeFragmentModule(WelcomeFragmentModule())
                .build()
        welcomeFragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false)

        val viewPager = rootView.findViewById<ViewPager>(R.id.viewPager)
        val btnSkip = rootView.findViewById<Button>(R.id.buttonSkip)
        val btnNext = rootView.findViewById<Button>(R.id.buttonNext)

        val welcomePagerAdapter = WelcomePagerAdapter(context!!, screenLayouts)
        viewPager.adapter = welcomePagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        btnNext.setOnClickListener { presenter.setCurrentPage(getItem(+1), screenLayouts.size) }
        btnSkip.setOnClickListener { launchAutoVisionActivity() }

        return rootView
    }

    override fun setCurrentPage(current: Int, layoutSize: Int) {
        if (current < layoutSize) {
            viewPager.currentItem = current
        } else {
            launchAutoVisionActivity()
        }
    }

    private fun getItem(i: Int): Int = viewPager.currentItem + i

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(screenLayouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        layoutDots.removeAllViews()

        (0 until dots.size).forEach { i ->
            dots[i] = TextView(activity)
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 35F
            dots[i]?.setTextColor(colorsInactive[currentPage])
            layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]?.setTextColor(colorsActive[currentPage])
    }

    override fun launchAutoVisionActivity() {
        preferencesManager.setFirstTimeLaunch(false)
        startActivity(AutoVisionActivity.getIntent(context!!))
        activity?.finish()
    }

    override fun updateButtons(last: Boolean) {
        if (last) {
            buttonNext.text = getString(R.string.start)
            buttonSkip.visibility = View.GONE
        } else {
            buttonNext.text = getString(R.string.next)
            buttonSkip.visibility = View.VISIBLE
        }
    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            presenter.updateButtons(position == screenLayouts.size - 1)
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    companion object {
        fun newInstance() = WelcomeFragment()
        const val TAG: String = "Welcome Fragment"
        val screenLayouts = arrayOf(R.layout.welcome_screen_one, R.layout.welcome_screen_two, R.layout.welcome_screen_three)
    }
}
