package com.example.daggermvp.ui.activities.autovision

import android.app.ProgressDialog
import android.os.AsyncTask
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.daggermvp.R
import com.example.daggermvp.ui.activities.autovision.AutoVisionActivity.Companion.TAG
import com.example.daggermvp.ui.activities.result.ResultActivity
import com.example.daggermvp.utils.ResultManager
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class LabelDetectionTask constructor(activity: AutoVisionActivity, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, BatchAnnotateImagesResponse>() {

    private val mActivityWeakReference: WeakReference<AutoVisionActivity> = WeakReference(activity)

    private var progressDialog: ProgressDialog? = null

    override fun onPreExecute() {
        super.onPreExecute()
        val activity = mActivityWeakReference.get()
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(activity?.getString(R.string.fetching_results))
        progressDialog?.show()
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
        progressDialog?.dismiss()
        val activity = mActivityWeakReference.get()

        if (activity != null && !activity.isFinishing) {
            if (result.responses[0]?.labelAnnotations?.isNotEmpty() == true) {
                ResultManager.labelAnnotations = result.responses[0].labelAnnotations
                val predictionLayout = activity.findViewById<CardView>(R.id.labelLayout)
                predictionLayout.visibility = View.VISIBLE
                val textView = predictionLayout.findViewById<TextView>(R.id.predictionTextView)
                textView.text = activity.getString(R.string.result_predictions, result.responses[0].labelAnnotations.size)
                predictionLayout.setOnClickListener {
                    mActivityWeakReference.get()?.startActivity(ResultActivity.getIntent(mActivityWeakReference.get()!!, ResultActivity.ResultType.LABEL))
                }
            }

            if (result.responses[0]?.logoAnnotations?.isNotEmpty() == true) {
                ResultManager.logoAnnotations = result.responses[0].logoAnnotations
                val logoLayout = activity.findViewById<CardView>(R.id.logoLayout)
                logoLayout.visibility = View.VISIBLE
                val textView = logoLayout.findViewById<TextView>(R.id.logosTextView)
                textView.text = activity.getString(R.string.result_logos, result.responses[0].logoAnnotations.size)
                logoLayout.setOnClickListener {
                    mActivityWeakReference.get()?.startActivity(ResultActivity.getIntent(mActivityWeakReference.get()!!, ResultActivity.ResultType.LOGO))
                }
            }

//            FIXME: Fix for web results
//            val a = result.responses[0]["webDetection"] as
//            if(result.responses[0].logoAnnotations.isNotEmpty()){
//                val predictionLayout = activity.findViewById<CardView>(R.id.predLayout)
//                predictionLayout.visibility = View.VISIBLE
//                val textView = predictionLayout.findViewById<TextView>(R.id.predictionTextView)
//                textView.text = activity.getString(R.string.result_predictions, result.responses[0].labelAnnotations.size)
//            }
        }
    }

    companion object {
        private fun convertResponseToString(response: BatchAnnotateImagesResponse): String {

            val message = StringBuilder("")

            val labels = response.responses[0].labelAnnotations
            ResultManager.labelAnnotations = labels

            if (labels != null) {
                message.append("\n\nThis image is related to:\n\n")
                labels.forEach { label ->
                    message.append(String.format(Locale.US, "%.3f: %s", label.score, label.description))
                    message.append("\n")
                }
            }

            val logos = response.responses[0].logoAnnotations
            ResultManager.logoAnnotations = logos

            if (logos != null) {
                message.append("\n\nI found the following logos in your image:\n\n")
                logos.forEach {
                    message.append(String.format(Locale.US, "%.3f: %s", it.score, it.description))
                    message.append("\n")
                }
            }

            val webResults = response.responses[0].imagePropertiesAnnotation

            if (message.toString().isEmpty()) {
                message.append("We are sorry, please try again!")
            }

            Log.d(TAG, message.toString())
            return message.toString()
        }
    }
}