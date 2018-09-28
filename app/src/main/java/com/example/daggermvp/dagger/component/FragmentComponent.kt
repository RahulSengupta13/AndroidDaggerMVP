package com.example.daggermvp.dagger.component

import com.example.daggermvp.dagger.module.FragmentModule
import com.example.daggermvp.ui.aboutfragment.AboutFragment
import com.example.daggermvp.ui.listfragment.ListFragment
import dagger.Component

@Component(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(aboutFragment: AboutFragment)

    fun inject(listFragment: ListFragment)
}