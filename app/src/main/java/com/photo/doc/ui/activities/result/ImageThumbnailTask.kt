package com.photo.doc.ui.activities.result

import android.os.AsyncTask
import com.google.api.services.vision.v1.model.WebPage
import com.photo.doc.models.WebPageWithImage
import com.photo.doc.utils.ResultManager
import org.jsoup.Jsoup
import java.lang.ref.WeakReference

class ImageThumbnailTask constructor(activity: ResultActivity, private val webPage: WebPage, private val url: String) : AsyncTask<Any, Void, String>() {

    private val mActivityWeakReference: WeakReference<ResultActivity> = WeakReference(activity)

    override fun doInBackground(vararg params: Any?): String {

        try {
            val document = Jsoup.connect(url).get()
            val elements = document.select("meta")
            val element = elements.firstOrNull { it.attr("property").equals("og:image", true) }
            val imageUrl = element?.attr("content") ?: ""

            val model = WebPageWithImage(webPage, imageUrl)
            ResultManager.matchingPagesWithImages.add(model)

            return element?.attr("content") ?: ""
        } catch (exception: Exception) {

            val model = WebPageWithImage(webPage, webPage.fullMatchingImages?.get(0)?.url ?: "")
            ResultManager.matchingPagesWithImages.add(model)
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val activity = mActivityWeakReference.get() ?: return
        activity.presenter.dataSetChanged()
    }

}