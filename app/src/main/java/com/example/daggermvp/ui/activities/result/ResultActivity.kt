package com.example.daggermvp.ui.activities.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import com.example.daggermvp.R
import com.example.daggermvp.dagger.component.DaggerResultComponent
import com.example.daggermvp.dagger.module.ResultModule
import com.example.daggermvp.utils.BitmapUtils
import com.example.daggermvp.utils.ResultManager
import kotlinx.android.synthetic.main.activity_result.*
import javax.inject.Inject

class ResultActivity : AppCompatActivity() {

    companion object {
        private const val MAX_DIMENSION = 1200
        private const val INTENT_TYPE = "TYPE"

        fun getIntent(context: Context, resultType: ResultType) = Intent(context, ResultActivity::class.java).apply {
            putExtra(INTENT_TYPE, resultType)
        }
    }

    @Inject
    lateinit var presenter: ResultContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Results"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        injectDependency()

        val bitmap = BitmapUtils.scaleBitmapDown(
                MediaStore.Images.Media.getBitmap(contentResolver, ResultManager.photoUri),
                MAX_DIMENSION) ?: return
        resultImageView.setImageBitmap(bitmap)

        resultRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        when(intent?.getSerializableExtra(INTENT_TYPE)) {
            ResultType.LABEL -> {
                resultRecyclerView.adapter = ResultAdapter(this, ResultManager.labelAnnotations)
            }

            ResultType.LOGO -> {
                resultRecyclerView.adapter = ResultAdapter(this, ResultManager.logoAnnotations)
            }
        }
    }

    private fun injectDependency() {
        val component = DaggerResultComponent.builder()
                .resultModule(ResultModule(this))
                .build()
        component.inject(this)
    }

    enum class ResultType { LABEL, LOGO }
}
