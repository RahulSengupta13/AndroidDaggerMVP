package com.example.daggermvp.dagger.component

import com.example.daggermvp.dagger.module.WelcomeFragmentModule
import com.example.daggermvp.ui.fragments.aboutfragment.AboutFragment
import com.example.daggermvp.ui.fragments.welcomefragment.WelcomeFragment
import dagger.Component

@Component(modules = [WelcomeFragmentModule::class])
interface WelcomeFragmentComponent {
    fun inject(aboutFragment: AboutFragment)
    fun inject(welcomeFragment: WelcomeFragment)
}