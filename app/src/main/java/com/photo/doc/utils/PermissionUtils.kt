package com.photo.doc.utils

import android.app.Activity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtils {

    fun requestPermission(activity: Activity, requestCode: Int, permissions: Array<String>): Boolean {
        var granted = true
        val permissionsNeeded = ArrayList<String>()

        permissions.forEach { s ->
            val permissionCheck = ContextCompat.checkSelfPermission(activity, s)
            val hasPermission = permissionCheck == PackageManager.PERMISSION_GRANTED
            granted = granted and hasPermission
            if (!hasPermission) {
                permissionsNeeded.add(s)
            }
        }

        return if (granted) {
            true
        } else {
            ActivityCompat.requestPermissions(activity,
                    permissionsNeeded.toArray(arrayOfNulls<String>(permissionsNeeded.size)),
                    requestCode)
            false
        }
    }

    fun permissionGranted(requestCode: Int, permissionCode: Int, grantResults: IntArray): Boolean {
        return requestCode == permissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
}
