package com.example.daggermvp.ui.fragments.welcomefragment

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.view.PagerAdapter
import android.view.View

class WelcomePagerAdapter(private val activity: Context, private val layouts: Array<Int>) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        val view = layoutInflater!!.inflate(layouts[position], container, false)
        container.addView(view)

        return view
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}