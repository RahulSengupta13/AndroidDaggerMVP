package com.photo.doc.utils

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

object LinkUtils {

    fun TextView.applyHtml(source: String) {
        val html = source.fromHtml()
        val ssb = SpannableStringBuilder(html)
        text = ssb
    }

    fun String.wrapInHtmlLink(url: String) = "<a href=\"$url\">$this</a>"

    fun TextView.applyClickableHtml(source: String, block: (span: URLSpan) -> Unit) {
        val html = source.fromHtml()

        val ssb = SpannableStringBuilder(html)
        val urls = ssb.getSpans(0, html.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(ssb, span, block)
        }

        text = ssb
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    private fun makeLinkClickable(ssb: SpannableStringBuilder, span: URLSpan?, block: (span: URLSpan) -> Unit) {
        val start = ssb.getSpanStart(span)
        val end = ssb.getSpanEnd(span)
        val flags = ssb.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                span?.let { block(it) }
            }
        }

        ssb.setSpan(clickable, start, end, flags)
        ssb.removeSpan(span)
    }

    private fun String.fromHtml(): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }
    }
}
