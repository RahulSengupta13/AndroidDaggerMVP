package com.example.daggermvp.ui.activities.fullscreenimage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.daggermvp.R
import com.example.daggermvp.dagger.component.DaggerFullScreenImageComponent
import com.example.daggermvp.dagger.module.FullScreenImageModule
import com.example.daggermvp.utils.ResultManager
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import kotlinx.android.synthetic.main.app_bar.*
import javax.inject.Inject

class FullScreenImageActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: FullScreenImageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        injectDependency()

        imageView.setImage(ImageSource.uri(ResultManager.photoUri))
    }

    private fun injectDependency() {
        val component = DaggerFullScreenImageComponent.builder()
                .fullScreenImageModule(FullScreenImageModule(this))
                .build()
        component.inject(this)

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, FullScreenImageActivity::class.java)
    }
}
