package com.photo.doc.ui.activities.result

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.photo.doc.R
import com.photo.doc.models.WebPageWithImage
import com.photo.doc.utils.LinkUtils.applyHtml
import com.photo.doc.utils.LinkUtils.wrapInHtmlLink
import kotlinx.android.synthetic.main.web_result_item_layout.view.*

class WebResultAdapter constructor(private val context: ResultActivity, private val dataSet: MutableList<WebPageWithImage>) : RecyclerView.Adapter<WebResultAdapter.WebResultHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WebResultAdapter.WebResultHolder {
        val layout = LayoutInflater.from(p0.context).inflate(R.layout.web_result_item_layout, p0, false)
        return WebResultHolder(layout)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: WebResultHolder, position: Int) {
        val webPageWithImage = dataSet[position]
        holder.view.websiteTitle.applyHtml(webPageWithImage.webPage.pageTitle)
        val wrapped = webPageWithImage.webPage.url.wrapInHtmlLink(webPageWithImage.webPage.url)
        holder.view.websiteUrl.applyHtml(wrapped)
        Glide.with(holder.view.websitePreview)
                .load(webPageWithImage.imageUrl)
                .into(holder.view.websitePreview)
        holder.view.websiteUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webPageWithImage.webPage.url))
            context.startActivity(intent)
        }
    }

    class WebResultHolder(val view: View) : RecyclerView.ViewHolder(view)
}