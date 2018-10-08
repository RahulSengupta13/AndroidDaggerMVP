package com.photo.doc.dagger.component

import com.photo.doc.dagger.module.ResultModule
import com.photo.doc.ui.activities.result.ResultActivity
import dagger.Component

@Component(modules = [ResultModule::class])
interface ResultComponent {
    fun inject(resultActivity: ResultActivity)
}