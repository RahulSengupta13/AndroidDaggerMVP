package com.photo.doc.ui.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.photo.doc.R
import com.photo.doc.receivers.ConnectivityReceiver
import com.photo.doc.receivers.ConnectivityReceiver.Companion.connectivityReceiverListener

abstract class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var mSnackBar: Snackbar? = null
    private val connectivityReceiver = ConnectivityReceiver().apply { connectivityReceiverListener = this@BaseActivity }

    override fun onResume() {
        super.onResume()
        try {
            registerReceiver(connectivityReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        } catch (exception: Exception) {
            Log.e(TAG, "receiver already registered")
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(connectivityReceiver)
        } catch (exception: Exception) {
            Log.e(TAG, "receiver not registered")
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    private fun showMessage(isConnected: Boolean) {
        if (!isConnected) {
            val messageToUser = getString(R.string.connectivity_offline)
            mSnackBar = Snackbar.make(findViewById(R.id.rootLayout), messageToUser, Snackbar.LENGTH_LONG)
            mSnackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
        }
    }

    companion object {
        private const val TAG = "BaseActivity"
    }
}