package com.example.daggermvp.ui.autovision

import android.app.ProgressDialog
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.Vision
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import com.example.daggermvp.ui.autovision.AutoVisionActivity.Companion.TAG
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import com.example.daggermvp.R

class LabelDetectionTask constructor(activity: AutoVisionActivity, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, String>() {

    private val mActivityWeakReference: WeakReference<AutoVisionActivity> = WeakReference(activity)

    private var progressDialog: ProgressDialog? = null

    override fun onPreExecute() {
        super.onPreExecute()
        val activity = mActivityWeakReference.get()
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(activity?.getString(R.string.fetching_results))
        progressDialog?.show()
    }

    override fun doInBackground(vararg params: Any): String {
        try {
            Log.d(TAG, "created Cloud Vision request object, sending request")
            val response = mRequest.execute()
            return convertResponseToString(response)

        } catch (e: GoogleJsonResponseException) {
            Log.d(TAG, "failed to make API request because " + e.content)
        } catch (e: IOException) {
            Log.d(TAG, "failed to make API request because of other IOException " + e.message)
        }

        return "Cloud Vision API request failed. Check logs for details."
    }

    override fun onPostExecute(result: String) {
        progressDialog?.dismiss()
        val activity = mActivityWeakReference.get()
        if (activity != null && !activity.isFinishing) {
            val imageDetail = activity.findViewById<TextView>(R.id.imageDetails)
            imageDetail.text = result
        }
    }

    companion object {
        private fun convertResponseToString(response: BatchAnnotateImagesResponse): String {
            val message = StringBuilder("I found these things:\n\n")

            val labels = response.responses[0].labelAnnotations
            if (labels != null) {
                for (label in labels) {
                    message.append(String.format(Locale.US, "%.3f: %s", label.score, label.description))
                    message.append("\n")
                }
            } else {
                message.append("nothing")
            }
            Log.d(TAG, message.toString())
            return message.toString()
        }
    }
}