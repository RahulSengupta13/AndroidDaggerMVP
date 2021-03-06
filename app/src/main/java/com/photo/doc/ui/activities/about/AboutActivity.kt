package com.photo.doc.ui.activities.about

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.URLSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.photo.doc.R
import com.photo.doc.dagger.component.DaggerAboutComponent
import com.photo.doc.dagger.module.AboutModule
import com.photo.doc.utils.LinkUtils.applyClickableHtml
import com.photo.doc.utils.LinkUtils.wrapInHtmlLink
import kotlinx.android.synthetic.main.activity_about.*
import javax.inject.Inject

class AboutActivity : AppCompatActivity(), AboutContract.View, AppBarLayout.OnOffsetChangedListener {

    private var mMaxScrollSize: Int = 0
    private var mIsAvatarShown = true

    @Inject
    lateinit var presenter: AboutContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.aboutToolbar)
        toolbar.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        aboutAppBarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = aboutAppBarLayout.totalScrollRange

        injectDependency()

        val email = getString(R.string.author_email).wrapInHtmlLink(getString(R.string.author_email))
        val formattedString = getString(R.string.support_text, email)
        supportDescription.applyClickableHtml(formattedString) { span -> onEmailClick(span) }

//        authorWebsite.setOnClickListener {
//            val intent = Intent(Intent.ACTION_WEB_SEARCH)
//            intent.putExtra(SearchManager.QUERY, authorWebsite.text)
//            startActivity(intent)
//        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.totalScrollRange

        val percentage = Math.abs(i) * 100 / mMaxScrollSize

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false
            aboutImage.animate()
                    .scaleY(0.25f).scaleX(0.25f)
                    .setDuration(200)
                    .start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true
            aboutImage.animate()
                    .scaleY(1f).scaleX(1f)
                    .start()
        }
    }

    private fun onEmailClick(span: URLSpan) {
        Toast.makeText(this, span.url, Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(span.url))
            putExtra(Intent.EXTRA_SUBJECT, "PhotoDoc Support")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun injectDependency() {
        val component = DaggerAboutComponent.builder()
                .aboutModule(AboutModule(this))
                .build()
        component.inject(this)
    }

    companion object {
        private const val PERCENTAGE_TO_ANIMATE_AVATAR = 60
        fun getIntent(context: Context) = Intent(context, AboutActivity::class.java)
    }
}
