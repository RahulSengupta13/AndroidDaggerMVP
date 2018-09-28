package com.example.daggermvp.ui.autovision

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.daggermvp.R
import com.example.daggermvp.dagger.component.DaggerAutoVisionComponent
import com.example.daggermvp.dagger.module.AutoVisionModule
import javax.inject.Inject

class AutoVisionActivity : AppCompatActivity(), AutoVisionContract.View {

    @Inject
    lateinit var presenter: AutoVisionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_vision)
        title = "AutoVision Activity"

        injectDependency()
        presenter.attach(this)
    }

    private fun injectDependency() {
        val autoVisionComponent = DaggerAutoVisionComponent.builder()
                .autoVisionModule(AutoVisionModule(this))
                .build()
        autoVisionComponent.inject(this)
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, AutoVisionActivity::class.java)
    }
}
