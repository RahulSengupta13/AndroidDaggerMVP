package com.photo.doc.dagger.component

import com.photo.doc.dagger.module.WelcomeFragmentModule
import com.photo.doc.ui.fragments.aboutfragment.AboutFragment
import com.photo.doc.ui.fragments.welcomefragment.WelcomeFragment
import dagger.Component

@Component(modules = [WelcomeFragmentModule::class])
interface WelcomeFragmentComponent {
    fun inject(aboutFragment: AboutFragment)
    fun inject(welcomeFragment: WelcomeFragment)
}