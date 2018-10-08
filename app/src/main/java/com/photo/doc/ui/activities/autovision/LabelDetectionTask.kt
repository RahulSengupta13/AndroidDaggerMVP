package com.photo.doc.ui.activities.autovision

import android.app.AlertDialog
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.photo.doc.R
import com.photo.doc.ui.activities.autovision.AutoVisionActivity.Companion.TAG
import com.photo.doc.ui.activities.result.ResultActivity
import com.photo.doc.utils.ResultManager
import java.io.IOException
import java.lang.ref.WeakReference

class LabelDetectionTask constructor(activity: AutoVisionActivity, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, BatchAnnotateImagesResponse>() {

    private val mActivityWeakReference: WeakReference<AutoVisionActivity> = WeakReference(activity)

    private var alertDialog: AlertDialog? = null

    override fun onPreExecute() {
        super.onPreExecute()
        val activity = mActivityWeakReference.get()
        alertDialog = AlertDialog.Builder(activity)
                .apply { setView(R.layout.loading_dialog_layout) }
                .create()
        alertDialog?.show()
    }

    override fun doInBackground(vararg params: Any): BatchAnnotateImagesResponse? {
        try {
            Log.d(TAG, "created Cloud Vision request object, sending request")
            return mRequest.execute()

        } catch (e: GoogleJsonResponseException) {
            Log.d(TAG, "failed to make API request because " + e.content)
        } catch (e: IOException) {
            Log.d(TAG, "failed to make API request because of other IOException " + e.message)
        }

        return null
    }

    override fun onPostExecute(result: BatchAnnotateImagesResponse) {
        alertDialog?.dismiss()
        val activity = mActivityWeakReference.get()

        if (activity != null && !activity.isFinishing) {
            if (result.responses[0]?.labelAnnotations?.isNotEmpty() == true) {
                ResultManager.labelAnnotations = result.responses[0].labelAnnotations
                val predictionLayout = activity.findViewById<CardView>(R.id.labelLayout)
                predictionLayout.visibility = View.VISIBLE
                val textView = predictionLayout.findViewById<TextView>(R.id.predictionTextView)
                textView.text = activity.getString(R.string.result_predictions, result.responses[0].labelAnnotations.size)
                val detailsButton = activity.findViewById<Button>(R.id.predictionDetails)
                detailsButton.setOnClickListener {
                    activity.startActivity(ResultActivity.getIntent(activity, ResultActivity.ResultType.LABEL))
                }
            }

            if (result.responses[0]?.logoAnnotations?.isNotEmpty() == true) {
                ResultManager.logoAnnotations = result.responses[0].logoAnnotations
                val logoLayout = activity.findViewById<CardView>(R.id.logoLayout)
                logoLayout.visibility = View.VISIBLE
                val textView = logoLayout.findViewById<TextView>(R.id.logosTextView)
                textView.text = activity.getString(R.string.result_logos, result.responses[0].logoAnnotations.size)
                val detailsButton = activity.findViewById<Button>(R.id.logoDetails)
                detailsButton.setOnClickListener {
                    activity.startActivity(ResultActivity.getIntent(activity, ResultActivity.ResultType.LOGO))
                }
            }

            if (result.responses[0]?.webDetection?.pagesWithMatchingImages?.isNotEmpty() == true) {
                ResultManager.webAnnotation = result.responses[0].webDetection
                val webLayout = activity.findViewById<CardView>(R.id.webLayout)
                webLayout.visibility = View.VISIBLE
                val textView = activity.findViewById<TextView>(R.id.webTextView)
                textView.text = activity.getString(R.string.result_web, result.responses[0]?.webDetection?.pagesWithMatchingImages?.size)
                val detailsButton = activity.findViewById<Button>(R.id.webDetails)
                detailsButton.setOnClickListener {
                    activity.startActivity(ResultActivity.getIntent(activity, ResultActivity.ResultType.WEB_MATCHED))
                }
            }
        }
    }
}