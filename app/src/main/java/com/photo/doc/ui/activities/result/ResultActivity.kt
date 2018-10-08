package com.photo.doc.ui.activities.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.photo.doc.R
import com.photo.doc.dagger.component.DaggerResultComponent
import com.photo.doc.dagger.module.ResultModule
import com.photo.doc.ui.activities.fullscreenimage.FullScreenImageActivity
import com.photo.doc.utils.BitmapUtils
import com.photo.doc.utils.ResultManager
import kotlinx.android.synthetic.main.activity_result.*
import javax.inject.Inject

class ResultActivity : AppCompatActivity(), ResultContract.View {
    @Inject
    lateinit var presenter: ResultContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val toolbar = findViewById<Toolbar>(R.id.resultToolbar)
        toolbar.title = "Results"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        injectDependency()
        presenter.attach(this)

        val bitmap = BitmapUtils.scaleBitmapDown(
                MediaStore.Images.Media.getBitmap(contentResolver, ResultManager.photoUri),
                MAX_DIMENSION) ?: return
        resultImageView.setImageBitmap(bitmap)
        resultImageView.setOnClickListener { startActivity(FullScreenImageActivity.getIntent(this)) }

        resultRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        when (intent?.getSerializableExtra(INTENT_TYPE)) {
            ResultType.LABEL -> {
                resultRecyclerView.adapter = ResultAdapter(this, ResultManager.labelAnnotations)
            }

            ResultType.LOGO -> {
                resultRecyclerView.adapter = ResultAdapter(this, ResultManager.logoAnnotations)
            }
            ResultType.WEB_MATCHED -> {
                presenter.loadImagesForUrls()
                resultRecyclerView.adapter = WebResultAdapter(this, ResultManager.matchingPagesWithImages)
            }
        }
    }

    override fun dataSetChanged() {
        progressIndicator.visibility = View.GONE
        resultRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun injectDependency() {
        val component = DaggerResultComponent.builder()
                .resultModule(ResultModule(this))
                .build()
        component.inject(this)
    }

    companion object {
        const val MAX_DIMENSION = 1200
        private const val INTENT_TYPE = "TYPE"

        fun getIntent(context: Context, resultType: ResultType) = Intent(context, ResultActivity::class.java).apply {
            putExtra(INTENT_TYPE, resultType)
        }
    }

    enum class ResultType { LABEL, LOGO, WEB_MATCHED }
}
